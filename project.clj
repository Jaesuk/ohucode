(defproject ohucode "0.0.1"
  :description "오후코드 실험버전"
  :url "https://github.com/ohucode/ohucode"
  :min-lein-version "2.1.2"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.nrepl "0.2.12"]
                 [cider/cider-nrepl "0.11.0-SNAPSHOT"]
                 [org.clojure/data.json "0.2.6"]
                 [misaeng "0.1.0"]

                 [compojure "1.4.0"]
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [ring-logger-timbre "0.7.4"]
                 [aleph "0.4.0"]
                 [hiccup "1.0.5"]
                 [korma "0.4.2"]
                 [ragtime "0.5.2"]
                 [prone "0.8.2"]
                 [com.taoensso/timbre "4.2.1"]
                 [org.slf4j/slf4j-api "1.7.14"]
                 [com.fzakaria/slf4j-timbre "0.3.0"]

                 [org.postgresql/postgresql "9.4-1203-jdbc42"]
                 [org.eclipse.jgit/org.eclipse.jgit "4.2.0.201601211800-r"]
                 [amazonica "0.3.49"]

                 [org.clojure/clojurescript "1.7.228"]
                 [reagent "0.6.0-alpha"]
                 [secretary "1.2.3"]
                 [cljsjs/jquery "2.1.4-0"]
                 [cljsjs/marked "0.3.5-0"]
                 [cljsjs/highlight "8.4-0"]]

  :plugins [[lein-figwheel "0.5.0-6"]]
  :ring {:handler 오후코드.핸들러/app-dev}
  :main 오후코드.서버/시작
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]
                        [org.clojure/tools.namespace "0.2.11"]]}}
  :repl-options {:init-ns user
                 :init (set! *print-length* 50)}
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src-cljs/"]
                        :figwheel true
                        :compiler {:main "ohucode.main"
                                   :optimizations :none
                                   :asset-path "cljs"
                                   :output-to "resources/public/cljs/main.js"
                                   :output-dir "resources/public/cljs"}}
                       {:id "prod"
                        :source-paths ["src-cljs/"]
                        :compiler {:main "ohucode.main"
                                   :optimizations :advanced
                                   :asset-path "cljs"
                                   :output-to "resources/public/cljs.min/main.js"
                                   :output-dir "resources/public/cljs.min"}}]})
