package model2;

import android.auto.value.AutoValue;
import android.os.Parcelable;

@AutoValue
public abstract class Address implements Parcelable {
  public abstract double[] coordinates();
  public abstract String cityName();

  public static Address create(double[] coordinates, String cityName) {
    return new AutoValue_Address(coordinates, cityName);
  }
}
