package model2;

import auto.parcel.AutoParcel;
import android.os.Parcelable;

@AutoParcel
public abstract class Address implements Parcelable {
  public abstract double[] coordinates();
  public abstract String cityName();

  public static Address create(double[] coordinates, String cityName) {
      return builder().coordinates(coordinates).cityName(cityName).build();
  }

  public static Builder builder() {
      return new AutoParcel_Address.Builder();
  }

  @AutoParcel.Builder
  public interface Builder {
      public Builder coordinates(double[] x);
      public Builder cityName(String x);
      public Address build();
  }

  @AutoParcel.Validate
  public void validate() {
      if (cityName().length() < 2) {
          throw new IllegalStateException("Not a city name");
      }
  }
}
