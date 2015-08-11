(ns auto-parcel.core-test
  (:require [clojure.test :refer :all]
            [clojure.string :as string])
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

(defn- make-source [[file content]]
  (JavaFileObjects/forSourceString file (string/join "\n" content)))

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
             #_(debug)
             (.generatesSources first (into-array JavaFileObject rest)))))))

(deftest simple
  (testing "one property"
    (check-compiles
     ["test.Test"
      ["package test;"
       "import com.google.auto.value.AutoValue;"
       "import android.os.Parcelable;"
       "@AutoValue abstract class Test implements Parcelable {"
       "  abstract String f1();"
       "  public int describeContents() {return 0;}"
       "}"]]
     ["test.AutoValue_Test"
      ["package test;"
       "import android.os.Parcel;"
       "import javax.annotation.Generated;"
       "import java.lang.ClassLoader;"
       "@Generated(\"auto_parcel.AutoParcelExtension\")"
       "final class AutoValue_Test extends $AutoValue_Test {"
       "  private final static ClassLoader CL = AutoValue_Test.class.getClassLoader();"
       "  public AutoValue_Test (java.lang.String f1) {"
       "    super(f1);"
       "  }"
       "  @Override"
       "  public int describeContents() {"
       "    return 0;"
       "  }"
       "  @Override"
       "  public void writeToParcel(Parcel dest, int flags) {"
       "    dest.writeValue(f1());"
       "  }"
       "  private AutoValue_Test(Parcel in) {"
       "    this("
       "      (java.lang.String) in.readValue(CL)"
       "    );"
       "  }"
       "  public static final Creator<AutoValue_Test> CREATOR = new Creator<AutoValue_Test>() {"
       "    @Override"
       "    public AutoValue_Test createFromParcel(Parcel in) {"
       "      return new AutoValue_Test(in);"
       "    }"
       "    @Override"
       "    public AutoValue_Test[] newArray(int size) {"
       "      return new AutoValue_Test[size];"
       "    }"
       "  };"
       "}"]]))
  (testing "multiple properties"
    (check-compiles
     ["test.Test"
      ["package test;"
       "import com.google.auto.value.AutoValue;"
       "import android.os.Parcelable;"
       "import java.util.*;"
       "@AutoValue abstract class Test implements Parcelable {"
       "  abstract Map<Double, List<String>> f1();"
       "  public int describeContents() {return 0;}"
       "}"]]
     ["test.AutoValue_Test"
      ["package test;"
       "import android.os.Parcel;"
       "import javax.annotation.Generated;"
       "import java.lang.ClassLoader;"
       "@Generated(\"auto_parcel.AutoParcelExtension\")"
       "final class AutoValue_Test extends $AutoValue_Test {"
       "  private final static ClassLoader CL = AutoValue_Test.class.getClassLoader();"
       "  public AutoValue_Test (java.util.Map<java.lang.Double, java.util.List<java.lang.String>> f1) {"
       "    super(f1);"
       "  }"
       "  @Override"
       "  public int describeContents() {"
       "    return 0;"
       "  }"
       "  @Override"
       "  public void writeToParcel(Parcel dest, int flags) {"
       "    dest.writeValue(f1());"
       "  }"
       "  private AutoValue_Test(Parcel in) {"
       "    this("
       "      (java.util.Map<java.lang.Double, java.util.List<java.lang.String>>) in.readValue(CL)"
       "    );"
       "  }"
       "  public static final Creator<AutoValue_Test> CREATOR = new Creator<AutoValue_Test>() {"
       "    @Override"
       "    public AutoValue_Test createFromParcel(Parcel in) {"
       "      return new AutoValue_Test(in);"
       "    }"
       "    @Override"
       "    public AutoValue_Test[] newArray(int size) {"
       "      return new AutoValue_Test[size];"
       "    }"
       "  };"
       "}"]])))
