package test.zero;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.ClassLoader;

final class AutoValue_Test extends $AutoValue_Test {
    public static final Parcelable.Creator<AutoValue_Test> CREATOR = new Parcelable.Creator<AutoValue_Test>() {
        @Override
        public AutoValue_Test createFromParcel(Parcel in) {
            return new AutoValue_Test(in);
        }

        @Override
        public AutoValue_Test[] newArray(int size) {
            return new AutoValue_Test[size];
        }
    };
    private final static ClassLoader CL = AutoValue_Test.class.getClassLoader();

    public AutoValue_Test() {
        super();
    }

    private AutoValue_Test(Parcel in) {
        this();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
