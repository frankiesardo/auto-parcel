(ns auto-parcel.extension
  (:require [stencil.core :as mustache]))

(def template
  "package {{& package}};

import android.os.Parcel;
import javax.annotation.Generated;
import java.lang.ClassLoader;

@Generated(\"auto_parcel.AutoParcelExtension\")
final class {{& class-name}} extends {{& class-to-extend}} {

  private final static ClassLoader CL = {{& class-name}}.class.getClassLoader();

  public {{& class-name}} (
    {{#props}}
    {{& cast-type}} {{& name}}{{^last?}},{{/last?}}
    {{/props}}
  ) {
    super(
      {{#props}}
      {{& name}}{{^last?}},{{/last?}}
      {{/props}}
    );
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    {{#props}}
    dest.writeValue({{& name}}());
    {{/props}}
  }

  private {{& class-name}}(Parcel in) {
    this(
      {{#props}}
      ({{& cast-type}}) in.readValue(CL){{^last?}},{{/last?}}
      {{/props}}
    );
  }

  public static final Creator<{{& class-name}}> CREATOR = new Creator<{{& class-name}}>() {
    @Override
    public {{& class-name}} createFromParcel(Parcel in) {
      return new {{& class-name}}(in);
    }

    @Override
    public {{& class-name}}[] newArray(int size) {
      return new {{& class-name}}[size];
    }
  };
}")

(defn- ->prop [[k v :as prop]]
  {:name k
   :cast-type (.toString (.getReturnType v))})

(defn- add-last? [props]
  (assoc-in props [(dec (count props)) :last?] true))

(defn- generate [skeleton]
  (mustache/render-string template skeleton))

(defn process [context class-name class-to-extend final?]
  #_(binding [x (.processingEnvironment context)])
  (let [skeleton {:package (.packageName context)
                  :class-name class-name
                  :class-to-extend class-to-extend
                  :props (->> (.properties context)
                              (mapv ->prop)
                              (add-last?))}]
    (generate skeleton)))
