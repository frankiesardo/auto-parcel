(ns auto-parcel.core-test
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io])
  (:use auto-parcel.extension)
  (:import [com.google.auto.value.processor AutoValueProcessor]
           [javax.tools JavaFileObject]
           [com.google.testing.compile JavaFileObjects]
           [com.google.testing.compile JavaSourceSubjectFactory]
           [org.truth0 Truth]))


(defn- java-source []
  (JavaSourceSubjectFactory/javaSource))

(defn- auto-value-processors []
  [(AutoValueProcessor.)])

(defn- make-source [class]
  (let [file (str (str/replace class #"\." "/") ".java")
        content (slurp (io/resource file))]
    (JavaFileObjects/forSourceString class content)))

(defn- check-fails [input]
  (is (-> (Truth/ASSERT)
          (.about (java-source))
          (.that (make-source input))
          (.processedWith (auto-value-processors))
          (.failsToCompile))))

(defn- check-compiles
  ([input]
   (let [input-source (make-source input)]
     (is (-> (Truth/ASSERT)
             (.about (java-source))
             (.that input-source)
             (.processedWith (auto-value-processors))
             (.compilesWithoutError)))))
  ([input output & outputs]
   (let [[first & rest] (seq (map make-source (cons output outputs)))]
     (when-let [compiles (check-compiles input)]
       (-> compiles
           (.and)
           (.generatesSources first (into-array JavaFileObject rest)))))))

(deftest simple
  (testing "one property"
    (check-compiles
      "test.one.Test"
      "test.one.AutoValue_Test"))
  (testing "multiple properties"
    (check-compiles
      "test.two.Test"
      "test.two.AutoValue_Test")))
