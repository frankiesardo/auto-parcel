AutoParcel
============

[![Build Status](https://secure.travis-ci.org/frankiesardo/auto-parcel.png)](http://travis-ci.org/frankiesardo/auto-parcel)

AutoParcel is an [AutoValue]() extension that enables Parcelable values generation.

Just add `implements Parcelable` to your `@AutoValue` annotated models.

```java
@AutoValue
abstract class SomeModel implements Parcelable {
  abstract String name();
  abstract List<SomeSubModel> subModels();
  abstract Map<String, OtherSubModel> modelsMap();

  static SomeModel create(String name, List<SomeSubModel> subModels, Map<String, OtherSubModel> modelsMap) {
    return new AutoParcel_SomeModel(name, subModels, modelsMap);
  }
}
```

That's that simple. And you get `Parcelable`, `hashCode`, `equals` and `toString` implementations for free.

As your models evolve you don't need to worry about keeping all the boilerplate in sync with the new implementation, it's already taken care of.

Download
--------

Use the same dependency qualifier that you would use for AutoValue (e.g. `apt`)

```groovy
buildscript {
  repositories {
    mavenCentral()
    jcenter()
  }
  dependencies {
    classpath 'com.android.tools.build:gradle:0.14.4'
    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.4'
  }
}

apply plugin: 'com.android.application'
apply plugin: 'com.neenbedankt.android-apt'

repositories {
  mavenCentral()
  jcenter()
  maven {url "https://clojars.org/repo/"}
}

dependencies {
  apt 'frankiesardo:auto-parcel:1.0.0'
  apt 'com.google.auto:auto-value:1.0'
}

```
_Notice the clojars line in your maven repositories_

I recommend using the [`android-apt`](https://bitbucket.org/hvisser/android-apt) plugin so that Android Studio picks up the generated files.
Check out the sample project for a working example.

Roadmap
--------

- Dismantle instanceof checks speeding up runtime serialization [ff]
- Walking up superclasses to ensure the object is Parcelable [gh]
