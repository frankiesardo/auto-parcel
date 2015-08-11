package model2;

import com.google.auto.value.AutoValue;
import android.os.Parcelable;

@AutoValue
public abstract class Address implements Parcelable {
  public abstract double[] coordinates();
  public abstract String cityName();

  public static Address create(double[] coordinates, String cityName) {
      return builder().coordinates(coordinates).cityName(cityName).build();
  }

  public static Builder builder() {
      return new AutoValue_Address.Builder();
  }

    @Override
    public int describeContents() {
        return 0;
    }

    @AutoValue.Builder
  public interface Builder {
      public Builder coordinates(double[] x);
      public Builder cityName(String x);
      public Address build();
  }

}
