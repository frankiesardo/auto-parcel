package auto_parcel;

import com.google.auto.common.MoreTypes;
import com.google.auto.value.AutoValueExtension;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import java.lang.reflect.ParameterizedType;
import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.*;


import clojure.java.api.Clojure;
import clojure.lang.IFn;
import clojure.lang.Compiler;

import com.google.auto.service.AutoService;
import com.google.auto.value.AutoValueExtension;

@AutoService(AutoValueExtension.class)
public class AutoParcelExtension implements AutoValueExtension {

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
        //        loadClojureFn();
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
  public boolean mustBeAtEnd(Context context) {
    return true;
  }

  @Override
  public String generateClass(final Context context, final String className, final String classToExtend, boolean isFinal) {
      loadClojureFn();
      return (String) PROCESS.invoke(context, className, classToExtend, isFinal);
  }
}
