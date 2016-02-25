package test.one;
import com.google.auto.value.AutoValue;
import android.os.Parcelable;
public @AutoValue abstract class Test implements Parcelable {
    abstract String f1();
}