(defproject    day8.re-frame/forward-events-fx "lein-git-inject/version"
  :description "A re-frame effects handler for listening-for and then post-processing dispatched events."
  :url         "https://github.com/day8/re-frame-forward-events-fx.git"
  :license     {:name "MIT"}

  :dependencies [[org.clojure/clojure       "1.10.2"   :scope "provided"]
                 [org.clojure/clojurescript "1.10.773" :scope "provided"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library
                               org.clojure/google-closure-library-third-party]]
                 [thheller/shadow-cljs      "2.11.18"  :scope "provided"]
                 [re-frame                  "1.2.0"    :scope "provided"]]

  :plugins      [[day8/lein-git-inject "0.0.14"]
                 [lein-shadow          "0.3.1"]]

  :middleware   [leiningen.git-inject/middleware]

  :profiles {:debug {:debug true}
             :dev   {:dependencies [[binaryage/devtools "1.0.2"]]
                     :plugins      [[lein-ancient "0.6.15"]
                                    [lein-shell   "0.5.0"]]}}

  :clean-targets [:target-path
                  "resources/public/js/test"
                  "shadow-cljs.edn"
                  "node_modules"]

  :resource-paths ["run/resources"]
  :jvm-opts ["-Xmx1g"]
  :source-paths ["src"]
  :test-paths ["test"]

  :shadow-cljs {:nrepl  {:port 8777}

                :builds {:browser-test
                         {:target    :browser-test
                          :ns-regexp "-test$"
                          :test-dir  "resources/public/js/test"
                          :devtools  {:http-root "resources/public/js/test"
                                      :http-port 8290
                                      :preloads  [devtools.preload]}}

                         :karma-test
                         {:target    :karma
                          :ns-regexp "-test$"
                          :output-to "target/karma-test.js"}}}

  :shell {:commands {"karma" {:windows         ["cmd" "/c" "karma"]
                              :default-command "karma"}
                     "open"  {:windows         ["cmd" "/c" "start"]
                              :macosx          "open"
                              :linux           "xdg-open"}}}

  :deploy-repositories [["clojars" {:sign-releases false
                                    :url           "https://clojars.org/repo"
                                    :username      :env/CLOJARS_USERNAME
                                    :password      :env/CLOJARS_TOKEN}]]

  :release-tasks [["deploy" "clojars"]]

  :aliases {"watch" ["do"
                     ["clean"]
                     ["shadow" "watch" "browser-test" "karma-test"]]

            "ci"    ["do"
                     ["clean"]
                     ["shadow" "compile" "karma-test"]
                     ["shell" "karma" "start" "--single-run" "--reporters" "junit,dots"]]})
