(ns ohucode.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults api-defaults]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.stacktrace :refer [wrap-stacktrace]]
            [ring.middleware.lint :refer [wrap-lint]]
            [ring.logger.timbre :refer [wrap-with-logger]]
            [aleph.http :as http]
            [ohucode.view :as view]
            [ohucode.git :as git]
            [ohucode.git-http :as git-http])
  (:import [java.util Locale]))

(defn index [req] "한글 인덱스 뷰! 별도 함수 리로드 확인. 이건 알레프도 되요.")

(println "핸들러 로드")

(def project-routes
  (routes
   git-http/smart-http-routes
   (GET "/:user/:project" [user project] (str user "/" project))))

(def app-routes
  (routes
   (GET "/" [] index)
   (POST "/" [] "post test")
   project-routes
   (route/resources "/")
   (route/not-found "Page not found")))

(def app
  (wrap-defaults app-routes api-defaults))

(def app-dev
  (-> app
      (wrap-reload)
      (wrap-stacktrace)
      (wrap-with-logger)))

;; start-server로 시작한 내용은 리로드되지 않음. 왜?
(defn start []
  (Locale/setDefault Locale/US)
  (http/start-server app-dev {:port 10000}))
