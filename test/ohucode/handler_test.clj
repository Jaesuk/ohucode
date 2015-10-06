(ns ohucode.handler-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [ring.mock.request :as mock]
            [ohucode.handler :refer :all]))

(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404))))

  (testing "/info/refs should response with a proper content-type"
    (let [response (app (mock/request :get "/u/p/info/refs?service=git-upload-pack"))]
      (is (= ((:headers response) "Content-Type")
             "application/x-git-upload-pack-advertisement")))
    (let [response (app (mock/request :get "/u/p/info/refs?service=git-receive-pack"))]
      (is (= ((:headers response) "Content-Type")
             "application/x-git-receive-pack-advertisement"))))

  (testing "POST /git-upload-pack"
    (let [in (io/input-stream "fixture/upload-pack-req.body")
          request (merge
                   (mock/request :post "/u/p/git-upload-pack")
                   {:headers {"Content-Length" 14762
                              "Content-Type" "application/x-git-upload-pack-request"
                              "Accept" "application/x-git-upload-pack-result"
                              "Content-Encoding" "gzip"
                              "Accept-Encoding" "gzip"}
                    :body in})
          response (app request)
          res-headers (:headers response)]
      (is (= (:status response) 200))
      (are [header-key value] (= (res-headers header-key) value)
        "Content-Type" "application/x-git-upload-pack-result"
        "Content-Encoding" "gzip"
        "Cache-Control" "no-cache, max-age=0, must-revalidate")))

  (testing "POST /git-receive-pack"
    (let [create-branch {:file "fixture/receive-pack-create-branch.body"
                         :size 189}
          delete-branch {:file "fixture/receive-pack-delete-branch.body"
                         :size 157}
          fixtures [create-branch delete-branch]
          testf (fn [fixture]
                  (let [request
                        (merge
                         (mock/request :post "/u/p/git-receive-pack")
                         {:headers {"Content-Length" (:size fixture)
                                    "Content-Type" "application/x-git-receive-pack-request"
                                    "Accept" "application/x-git-receive-pack-result"
                                    "Content-Encoding" "gzip"
                                    "Accept-Encoding" "gzip"}
                          :body (io/input-stream (:file fixture))})
                        response (app request)
                        res-headers (:headers response)]
                    (is (= (:status response) 200))
                    (are [header-key value] (= (res-headers header-key) value)
                      "Content-Type" "application/x-git-receive-pack-result"
                      "Content-Encoding" "gzip"
                      "Cache-Control" "no-cache, max-age=0, must-revalidate")))]
      (map testf fixtures)))
  )
