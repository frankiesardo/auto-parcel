/*
 * Copyright (C) 2012 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package auto.parcel.processor;

import org.apache.velocity.runtime.parser.node.SimpleNode;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * The variables to substitute into the autoparcel.vm template.
 *
 * @author emcmanus@google.com (Éamonn McManus)
 */
@SuppressWarnings("unused")  // the fields in this class are only read via reflection
class AutoParcelTemplateVars extends TemplateVars {

  private static final String templateStr =
      "## Template for each generated AutoParcel_Foo class.\n" +
              "## This template uses the Apache Velocity Template Language (VTL).\n" +
              "## The variables ($pkg, $props, and so on) are defined by the fields of AutoParcelTemplateVars.\n" +
              "##\n" +
              "## Comments, like this one, begin with ##. The comment text extends up to and including the newline\n" +
              "## character at the end of the line. So comments also serve to join a line to the next one.\n" +
              "## Velocity deletes a newline after a directive (#if, #foreach, #end etc) so ## is not needed there.\n" +
              "## That does mean that we sometimes need an extra blank line after such a directive.\n" +
              "##\n" +
              "## A post-processing step will remove unwanted spaces and blank lines, but will not join two lines.\n" +
              "\n" +
              "#if (!$pkg.empty)\n" +
              "package $pkg;\n" +
              "#end\n" +
              "\n" +
              "#foreach ($i in $imports)\n" +
              "import $i;\n" +
              "#end\n" +
              "\n" +
              "${gwtCompatibleAnnotation}\n" +
              "final class $subclass$formalTypes extends $origClass$actualTypes {\n" +
              "\n" +
              "## Fields\n" +
              "\n" +
              "#foreach ($p in $props)\n" +
              "  private final $p.type $p;\n" +
              "#end\n" +
              "\n" +
              "## Constructor\n" +
              "\n" +
              "#if ($builderTypeName != \"\")\n" +
              "  private ##\n" +
              "#end\n" +
              "  $subclass(\n" +
              "#foreach ($p in $props)\n" +
              "\n" +
              "      $p.type $p #if ($foreach.hasNext) , #end\n" +
              "#end ) {\n" +
              "#foreach ($p in $props)\n" +
              "  #if (!$p.kind.primitive && !$p.nullable)\n" +
              "\n" +
              "    if ($p == null) {\n" +
              "      throw new NullPointerException(\"Null $p.name\");\n" +
              "    }\n" +
              "\n" +
              "  #end\n" +
              "\n" +
              "    this.$p = $p;\n" +
              "#end\n" +
              "  }\n" +
              "\n" +
              "## Property getters\n" +
              "\n" +
              "#foreach ($p in $props)\n" +
              "\n" +
              "  #foreach ($a in ${p.annotations})\n" +
              "\n" +
              "  ${a}##\n" +
              "  #end\n" +
              "\n" +
              "  @Override\n" +
              "  ${p.access}${p.type} ${p.getter}() {\n" +
              "\n" +
              "  #if ($p.kind == \"ARRAY\")\n" +
              "\n" +
              "    #if ($p.nullable)\n" +
              "\n" +
              "    return $p == null ? null : ${p}.clone();\n" +
              "\n" +
              "    #else\n" +
              "\n" +
              "    return ${p}.clone();\n" +
              "\n" +
              "    #end\n" +
              "\n" +
              "  #else\n" +
              "\n" +
              "    return $p;\n" +
              "\n" +
              "  #end\n" +
              "\n" +
              "  }\n" +
              "\n" +
              "#end\n" +
              "\n" +
              "#if ($toString)\n" +
              "\n" +
              "  @Override\n" +
              "  public String toString() {\n" +
              "    return \"$simpleClassName{\"\n" +
              "\n" +
              "  #foreach ($p in $props)\n" +
              "\n" +
              "        + \"$p.name=\" ##\n" +
              "        + #if ($p.kind == \"ARRAY\") ${arrays}.toString($p) #else $p #end\n" +
              "        #if ($foreach.hasNext) + \", \" #end\n" +
              "\n" +
              "  #end\n" +
              "\n" +
              "        + \"}\";\n" +
              "  }\n" +
              "\n" +
              "#end\n" +
              "\n" +
              "#if ($equals)\n" +
              "  #macro (equalsThatExpression $p)\n" +
              "    #if ($p.kind == \"FLOAT\")\n" +
              "      Float.floatToIntBits(this.$p) == Float.floatToIntBits(that.${p.getter}()) ##\n" +
              "    #elseif ($p.kind == \"DOUBLE\")\n" +
              "      Double.doubleToLongBits(this.$p) == Double.doubleToLongBits(that.${p.getter}()) ##\n" +
              "    #elseif ($p.kind.primitive)\n" +
              "      this.$p == that.${p.getter}() ##\n" +
              "    #elseif ($p.kind == \"ARRAY\")\n" +
              "      ${arrays}.equals(this.$p, ##\n" +
              "          (that instanceof $subclass) ? (($subclass) that).$p : that.${p.getter}()) ##\n" +
              "    #else\n" +
              "      #if ($p.nullable) (this.$p == null) ? (that.${p.getter}() == null) : #end ##\n" +
              "          this.${p}.equals(that.${p.getter}()) ##\n" +
              "    #end\n" +
              "  #end\n" +
              "\n" +
              "  @Override\n" +
              "  public boolean equals(Object o) {\n" +
              "    if (o == this) {\n" +
              "      return true;\n" +
              "    }\n" +
              "    if (o instanceof $origClass) {\n" +
              "\n" +
              "  #if ($props.empty)\n" +
              "\n" +
              "      return true;\n" +
              "\n" +
              "  #else\n" +
              "\n" +
              "      $origClass$wildcardTypes that = ($origClass$wildcardTypes) o;\n" +
              "      return ##\n" +
              "           #foreach ($p in $props)\n" +
              "           (#equalsThatExpression ($p))##\n" +
              "             #if ($foreach.hasNext)\n" +
              "\n" +
              "           && ##\n" +
              "             #end\n" +
              "           #end\n" +
              "           ;\n" +
              "  #end\n" +
              "\n" +
              "    }\n" +
              "    return false;\n" +
              "  }\n" +
              "\n" +
              "#end\n" +
              "\n" +
              "#if ($hashCode)\n" +
              "  #macro (hashCodeExpression $p)\n" +
              "    #if ($p.kind == \"BYTE\" || $p.kind == \"SHORT\" || $p.kind == \"CHAR\" || $p.kind == \"INT\")\n" +
              "      $p ##\n" +
              "    #elseif ($p.kind == \"LONG\")\n" +
              "      ($p >>> 32) ^ $p ##\n" +
              "    #elseif ($p.kind == \"FLOAT\")\n" +
              "      Float.floatToIntBits($p) ##\n" +
              "    #elseif ($p.kind == \"DOUBLE\")\n" +
              "      (Double.doubleToLongBits($p) >>> 32) ^ Double.doubleToLongBits($p) ##\n" +
              "    #elseif ($p.kind == \"BOOLEAN\")\n" +
              "      $p ? 1231 : 1237 ##\n" +
              "    #elseif ($p.kind == \"ARRAY\")\n" +
              "      ${arrays}.hashCode($p) ##\n" +
              "    #else\n" +
              "      #if ($p.nullable) ($p == null) ? 0 : #end ${p}.hashCode() ##\n" +
              "    #end\n" +
              "  #end\n" +
              "\n" +
              "  @Override\n" +
              "  public int hashCode() {\n" +
              "    int h = 1;\n" +
              "\n" +
              "  #foreach ($p in $props)\n" +
              "\n" +
              "    h *= 1000003;\n" +
              "    h ^= #hashCodeExpression($p);\n" +
              "\n" +
              "  #end\n" +
              "\n" +
              "    return h;\n" +
              "  }\n" +
              "#end\n" +
              "\n" +
              "#if ($parcelable)\n" +
              "  public static final android.os.Parcelable.Creator<$subclass> CREATOR = new android.os.Parcelable.Creator<$subclass>() {\n" +
              "    @Override\n" +
              "    public $subclass createFromParcel(android.os.Parcel in) {\n" +
              "      return new $subclass(in);\n" +
              "    }\n" +
              "\n" +
              "    @Override\n" +
              "    public ${subclass}[] newArray(int size) {\n" +
              "      return new ${subclass}[size];\n" +
              "    }\n" +
              "  };\n" +
              "\n" +
              "  private final static java.lang.ClassLoader CL = ${subclass}.class.getClassLoader();\n" +
              "\n" +
              "  private $subclass(android.os.Parcel in) {\n" +
              "    this(#foreach ($p in $props)\n" +
              "      ($p.castType) in.readValue(CL)#if ($foreach.hasNext), #end\n" +
              "    #end);\n" +
              "  }\n" +
              "\n" +
              "  @Override\n" +
              "  public void writeToParcel(android.os.Parcel dest, int flags) {\n" +
              "    #foreach ($p in $props)\n" +
              "      dest.writeValue($p);\n" +
              "    #end\n" +
              "  }\n" +
              "\n" +
              "  @Override\n" +
              "  public int describeContents() {\n" +
              "    return 0;\n" +
              "  }\n" +
              "#end\n" +
              "\n" +
              "#if (!$serialVersionUID.empty)\n" +
              "  private static final long serialVersionUID = $serialVersionUID;\n" +
              "#end\n" +
              "\n" +
              "#if ($builderTypeName != \"\")\n" +
              "\n" +
              "  #foreach ($m in $toBuilderMethods)\n" +
              "\n" +
              "  @Override\n" +
              "  public ${builderTypeName}${builderActualTypes} ${m}() {\n" +
              "    return new Builder${builderActualTypes}(this);\n" +
              "  }\n" +
              "\n" +
              "  #end\n" +
              "\n" +
              "  static final class Builder${builderFormalTypes} ##\n" +
              "  #if ($builderIsInterface) implements #else extends #end\n" +
              "      ${builderTypeName}${builderActualTypes} {\n" +
              "\n" +
              "    private final $bitSet set$ = new ${bitSet}();\n" +
              "\n" +
              "    #foreach ($p in $props)\n" +
              "\n" +
              "    private $p.type $p;\n" +
              "\n" +
              "    #end\n" +
              "\n" +
              "    Builder() {\n" +
              "    }\n" +
              "\n" +
              "    Builder(${origClass}${actualTypes} source) {\n" +
              "\n" +
              "      #foreach ($p in $props)\n" +
              "\n" +
              "      $builderSetterNames[$p.name](source.${p.getter}());\n" +
              "      ## We use the setter methods rather than assigning the fields directly so we don't have\n" +
              "      ## to duplicate the logic for cloning arrays. Not cloning arrays would conceivably be\n" +
              "      ## a security problem.\n" +
              "\n" +
              "      #end\n" +
              "\n" +
              "    }\n" +
              "\n" +
              "#set ($index = 0)\n" +
              "#foreach ($p in $props)\n" +
              "\n" +
              "    @Override\n" +
              "    public ${builderTypeName}${builderActualTypes} $builderSetterNames[$p.name]($p.type $p) {\n" +
              "      #if ($p.kind == \"ARRAY\")\n" +
              "        #if ($p.nullable)\n" +
              "\n" +
              "      this.$p = ($p == null) ? null : ${p}.clone();\n" +
              "\n" +
              "        #else\n" +
              "\n" +
              "      this.$p = ${p}.clone();\n" +
              "\n" +
              "        #end\n" +
              "      #else\n" +
              "\n" +
              "      this.$p = $p;\n" +
              "\n" +
              "      #end\n" +
              "\n" +
              "      #if (!$p.nullable)\n" +
              "\n" +
              "      set$.set($index);\n" +
              "      #set ($index = $index + 1)\n" +
              "\n" +
              "      #end\n" +
              "      return this;\n" +
              "    }\n" +
              "\n" +
              "    #end\n" +
              "\n" +
              "    @Override\n" +
              "    public ${origClass}${actualTypes} ${buildMethodName}() {\n" +
              "      if (set$.cardinality() < $index) {\n" +
              "        String[] propertyNames = {\n" +
              "\n" +
              "         #foreach ($p in $props) #if (!$p.nullable) \"$p\", #end #end\n" +
              "\n" +
              "        };\n" +
              "        StringBuilder missing = new StringBuilder();\n" +
              "        for (int i = 0; i < $index; i++) {\n" +
              "          if (!set$.get(i)) {\n" +
              "            missing.append(' ').append(propertyNames[i]);\n" +
              "          }\n" +
              "        }\n" +
              "        throw new IllegalStateException(\"Missing required properties:\" + missing);\n" +
              "      }\n" +
              "      ${origClass}${actualTypes} result = new ${subclass}${actualTypes}(\n" +
              "    #foreach ($p in $props)\n" +
              "\n" +
              "          this.$p #if ($foreach.hasNext) , #end\n" +
              "    #end );\n" +
              "\n" +
              "    #foreach ($v in $validators)\n" +
              "\n" +
              "      result.${v}();\n" +
              "\n" +
              "    #end\n" +
              "\n" +
              "      return result;\n" +
              "    }\n" +
              "  }\n" +
              "#end\n" +
              "}\n";

  /** The properties defined by the parent class's abstract methods. */
  List<AutoParcelProcessor.Property> props;

  /** Whether to generate an equals(Object) method. */
  Boolean equals;
  /** Whether to generate a hashCode() method. */
  Boolean hashCode;
  /** Whether to generate a toString() method. */
  Boolean toString;

  /** Whether to generate a Parcelable creator. */
  Boolean parcelable;

  /** The fully-qualified names of the classes to be imported in the generated class. */
  SortedSet<String> imports;

  /** The spelling of the java.util.Arrays class: Arrays or java.util.Arrays. */
  String arrays;

  /** The spelling of the java.util.BitSet class: BitSet or java.util.BitSet. */
  String bitSet;

  /**
   * The full spelling of the {@code @GwtCompatible} annotation to add to this class, or an empty
   * string if there is none. A non-empty value might look something like
   * {@code "@com.google.common.annotations.GwtCompatible(serializable = true)"}.
   */
  String gwtCompatibleAnnotation;

  /** The text of the serialVersionUID constant, or empty if there is none. */
  String serialVersionUID;

  /**
   * The package of the class with the {@code @AutoParcel} annotation and its generated subclass.
   */
  String pkg;
  /**
   * The name of the class with the {@code @AutoParcel} annotation, including containing
   * classes but not including the package name.
   */
  String origClass;
  /** The simple name of the class with the {@code @AutoParcel} annotation. */
  String simpleClassName;
  /** The simple name of the generated subclass. */
  String subclass;

  /**
   * The formal generic signature of the class with the {@code @AutoParcel} annotation and its
   * generated subclass. This is empty, or contains type variables with optional bounds,
   * for example {@code <K, V extends K>}.
   */
  String formalTypes;
  /**
   * The generic signature used by the generated subclass for its superclass reference.
   * This is empty, or contains only type variables with no bounds, for example
   * {@code <K, V>}.
   */
  String actualTypes;
  /**
   * The generic signature in {@link #actualTypes} where every variable has been replaced
   * by a wildcard, for example {@code <?, ?>}.
   */
  String wildcardTypes;

  /**
   * The name of the builder type as it should appear in source code, or empty if there is no
   * builder type. If class {@code Address} contains {@code @AutoParcel.Builder} class Builder
   * then this will typically be {@code "Address.Builder"}.
   */
  String builderTypeName = "";

  /**
   * The formal generic signature of the {@code AutoParcel.Builder} class. This is empty, or contains
   * type variables with optional bounds, for example {@code <K, V extends K>}.
   */
  String builderFormalTypes = "";
  /**
   * The generic signature used by the generated builder subclass for its superclass reference.
   * This is empty, or contains only type variables with no bounds, for example
   * {@code <K, V>}.
   */
  String builderActualTypes = "";

  /**
   * True if the builder being implemented is an interface, false if it is an abstract class.
   */
  Boolean builderIsInterface = false;

  /**
   * The simple name of the builder's build method, often {@code "build"}.
   */
  String buildMethodName = "";

  /**
   * A map from property names (like foo) to the corresponding setter method names (foo or setFoo).
   */
  Map<String, String> builderSetterNames = Collections.emptyMap();

  /**
   * The names of any {@code toBuilder()} methods, that is methods that return the builder type.
   */
  List<String> toBuilderMethods;

  /**
   * The simple names of validation methods (marked {@code @AutoParcel.Validate}) in the AutoParcel
   * class. (Currently, this set is either empty or a singleton.)
   */
  Set<String> validators = Collections.emptySet();

  private static final SimpleNode TEMPLATE = parsedTemplateForResource(templateStr, "autoparcel.vm");

  @Override
  SimpleNode parsedTemplate() {
    return TEMPLATE;
  }
}
