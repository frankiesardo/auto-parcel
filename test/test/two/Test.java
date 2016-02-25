package test.two;

import com.google.auto.value.AutoValue;
import android.os.Parcelable;

import java.util.*;

@AutoValue
abstract class Test implements Parcelable {
    abstract Map<Double, List<String>> f1();
}