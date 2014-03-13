package android.auto.value.sample;

import android.auto.value.AutoValue;

@AutoValue
public abstract class Person {
  public static Person create(String name, int id) {
    return new AutoValue_Person(name, id);
  }

  public abstract String name();
  public abstract int id();
}
