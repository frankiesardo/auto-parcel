(set-env!
 :resource-paths   #{"src"}
 :dependencies
 '[[org.clojure/clojure "1.7.0"]
   [com.google.auto.value/auto-value "1.2-rc1"]
   [com.google.auto.service/auto-service "1.0-rc2"]
   [javax.annotation/jsr250-api "1.0"]
   [stencil "0.3.5"]

   [com.google.android/android "4.1.1.4" :scope "test"]
   [com.google.testing.compile/compile-testing "0.7" :scope "test"]
   [adzerk/boot-test "1.0.7" :scope "test"]
   [adzerk/bootlaces "0.1.13" :scope "test"]
   ])

(require
 '[adzerk.boot-test :as test])

(ns-unmap 'boot.user 'test)

(deftask testing []
  (set-env! :resource-paths #(conj % "test"))
  identity)

(deftask test []
  (comp (testing)
        (test/test)))

(deftask autotest []
  (comp (testing)
        (watch)
        (test/test)))

;; Deploy

(require
 '[adzerk.bootlaces :refer :all]
 '[clojure.java.shell :as shell]
 '[clojure.string :as str])

(def +version+
  "1.0.0-SNAPSHOT"
  #_(let [{:keys [exit out]} (shell/sh "git" "describe" "--tags")
        tag (second (re-find #"v(.*)\n" out))]
    (if (zero? exit)
      (if (.contains tag "-")
        (str tag "-SNAPSHOT")
        tag)
      "0.1.0-SNAPSHOT")))

(task-options!
 pom {:project        'frankiesardo/auto-parcel
      :version        +version+
      :description    "Sandbox for experiments"
      :url            "https://github.com/frankiesardo/lab"
      :scm            {:url "https://github.com/frankiesardo/lab"}
      :license        {"Eclipse Public License" "http://www.eclipse.org/legal/epl-v10.html"}})

(deftask build []
  (comp (aot :all true) (javac) (pom) (jar) (install)))

(deftask clojars []
  (comp (build)
        (if (.endsWith +version+ "-SNAPSHOT")
          (push-snapshot)
          (push-release))))

(deftask init []
  (with-pre-wrap fileset
    (let [dotfiles (System/getenv "DOTFILES")
          home (System/getenv "HOME")]
      (println (:out (shell/sh "git" "clone" dotfiles (str home "/dotfiles"))))
      (println (:out (shell/sh (str home "/dotfiles/init.sh")))))
    fileset))

(deftask deploy []
  (comp (init) (clojars)))

(bootlaces! +version+)
(task-options! push {:ensure-clean false
                     :tag false})
