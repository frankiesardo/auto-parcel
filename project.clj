(defproject frankiesardo/auto-parcel "1.0.0-SNAPSHOT"
  :description "Parcelable extension for AutoValue"
  :url "https://github.com/frankiesardo/auto-parcel"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.google.auto.value/auto-value "1.2-rc1"]
                 [com.google.auto.service/auto-service "1.0-rc2"]
                 [javax.annotation/jsr250-api "1.0"]
                 [stencil "0.3.5"]]
  :source-paths      ["src"]
  :java-source-paths ["src"]
  :javac-options ["-target" "1.6" "-source" "1.6"]
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[com.google.testing.compile/compile-testing "0.7"]]}
             :provided {:dependencies [[com.google.android/android "4.1.1.4"]]}})
