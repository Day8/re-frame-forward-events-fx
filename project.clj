(defproject day8.re-frame/forward-events-fx "0.0.7-SNAPSHOT"
  :description  "A re-frame effects handler for listening-for and then post-processing dispatched events."
  :url          "https://github.com/Day8/re-frame-forward-events-fx.git"
  :license      {:name "MIT"}
  :dependencies [[org.clojure/clojure "1.10.1" :scope "provided"]
                 [org.clojure/clojurescript "1.10.520" :scope "provided"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library]]
                 [thheller/shadow-cljs "2.8.52" :scope "provided"]
                 [re-frame "0.10.9" :scope "provided"]]

  :plugins [[lein-shadow "0.1.5"]]

  :profiles {:debug {:debug true}
             :dev   {:dependencies [[karma-reporter     "3.1.0"]
                                    [binaryage/devtools "0.9.10"]]
                     :plugins      [[lein-ancient       "0.6.15"]
                                    [lein-shell         "0.5.0"]]}}

  :clean-targets  [:target-path
                   "resources/public/js/test"
                   "shadow-cljs.edn"
                   "package.json"
                   "package-lock.json"]

  :resource-paths ["run/resources"]
  :jvm-opts       ["-Xmx1g"]
  :source-paths   ["src"]
  :test-paths     ["test"]

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

  :shell          {:commands {"open" {:windows ["cmd" "/c" "start"]
                                      :macosx  "open"
                                      :linux   "xdg-open"}}}

  :deploy-repositories [["clojars"  {:sign-releases false
                                     :url "https://clojars.org/repo"
                                     :username :env/CLOJARS_USERNAME
                                     :password :env/CLOJARS_PASSWORD}]]

  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag" "v" "--no-sign"]
                  ["deploy" "clojars"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]
                  ["vcs" "push"]]

  :aliases {"dev-auto" ["shadow" "watch" "browser-test"]
            "test-once" ["do"
                         ["shadow" "compile" "karma-test"]
                         ["shell" "karma" "start" "--single-run" "--reporters" "junit,dots"]]})
