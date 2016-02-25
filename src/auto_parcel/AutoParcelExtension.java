package auto_parcel;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import com.google.auto.service.AutoService;
import com.google.auto.value.extension.AutoValueExtension;
import com.google.common.collect.Sets;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.Set;

@AutoService(AutoValueExtension.class)
public class AutoParcelExtension extends AutoValueExtension {

    private static IFn PROCESS;

    private static void loadClojureFn() {
        ClassLoader previous = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(AutoParcelExtension.class.getClassLoader());
        try {
            Object ns = Clojure.read("auto_parcel.extension");
            IFn require = Clojure.var("clojure.core", "require");
            require.invoke(ns);
            PROCESS = Clojure.var("auto-parcel.extension", "process");
        } finally {
            Thread.currentThread().setContextClassLoader(previous);
        }
    }

    static {
        loadClojureFn();
    }


    @Override
    public boolean applicable(Context context) {
        TypeElement parcelable = context.processingEnvironment().getElementUtils().getTypeElement("android.os.Parcelable");
        TypeMirror autoValueClass = context.autoValueClass().asType();
        return isClassOfType(context.processingEnvironment().getTypeUtils(), parcelable.asType(),
                autoValueClass);
    }

    boolean isClassOfType(Types typeUtils, TypeMirror type, TypeMirror cls) {
        return type != null && typeUtils.isAssignable(cls, type);
    }

    @Override
    public boolean mustBeFinal(Context context) {
        return true;
    }

    @Override
    public Set<String> consumeProperties(Context context) {
        return Sets.newHashSet("describeContents", "writeToParcel");
    }

    @Override
    public String generateClass(final Context context, final String className, final String classToExtend, boolean isFinal) {
        return (String) PROCESS.invoke(context, className, classToExtend, isFinal);
    }
}
