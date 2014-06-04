package model2;

import auto.parcel.AutoParcel;
import android.os.Parcelable;

@AutoParcel
public abstract class Address implements Parcelable {
  public abstract double[] coordinates();
  public abstract String cityName();

  public static Address create(double[] coordinates, String cityName) {
    return new AutoParcel_Address(coordinates, cityName);
  }
}
