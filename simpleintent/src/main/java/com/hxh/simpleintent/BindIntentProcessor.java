package com.hxh.simpleintent;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * @author huangxiaohui
 * @date 2019/11/14
 */
@AutoService(Processor.class)
public class BindIntentProcessor extends AbstractProcessor {
    private Elements mElementUtils;
    private Messager mMessager;
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElementUtils = processingEnvironment.getElementUtils();    // 元素操作辅助工具
        mMessager = processingEnvironment.getMessager();            // 日志辅助工具
        mFiler = processingEnvironment.getFiler();                  // 文件操作辅助工具
        log("init");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        /*
           1. set：携带getSupportedAnnotationTypes()中的注解类型，一般不需要用到。
           2. roundEnvironment：processor将扫描到的信息存储到roundEnvironment中，从这里取出所有使用Sgetter注解的字段。
          */
        Set<? extends Element> sgetterElements = roundEnvironment.getElementsAnnotatedWith(BindIntent.class);
        Map<String, ClassInfo> classes = new HashMap<>();
        if (!sgetterElements.isEmpty()) {
//            log("----------------------------------");
        }
        for (Element element : sgetterElements) {
//            log("process element [" + element.getSimpleName().toString() + "]");

            // 获取注解目标所在的包，在本例中，即使用Sgetter注解的字段所在的类所在的包
            PackageElement packageElement = mElementUtils.getPackageOf(element);
            String pkgName = packageElement.getQualifiedName().toString();
//            log("pkg=" + pkgName);

            // 获取包装类类型，在本例中，即使用Sgetter注解的字段所在的类
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            String enclosingName = enclosingElement.getQualifiedName().toString(); // enclosingName为完整类名
            String simpleName = enclosingElement.getSimpleName().toString();
//            log("class=" + enclosingName);

            BindIntent ano = element.getAnnotation(BindIntent.class);
            // 获取字段信息，因为Sgetter只作用于字段，因此这里可以直接强转
            VariableElement variableElement = (VariableElement) element;
            String fieldname = variableElement.getSimpleName().toString();  // 获取字段名
            String fieldtype = variableElement.asType().toString();         // 获取字段类型
//            log("field name=" + fieldname + ", type=" + fieldtype);

            ClassInfo classInfo = classes.get(enclosingName);
            if (classInfo == null) {
                classInfo = new ClassInfo();
                classInfo.pkgName = pkgName;
                classInfo.classname = enclosingName;
                classInfo.fields = new LinkedList<>();
                classes.put(enclosingName, classInfo);
            }
            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.name = fieldname;
            fieldInfo.type = fieldtype;
            fieldInfo.key = ano.key();
            classInfo.fields.add(fieldInfo);

            log("fieldInfo:" + fieldname + " " + fieldtype + " " + ano.key());
//            log("----------------------------------");
        }

        for (ClassInfo classInfo : classes.values()) {
            generateJavaFile(classInfo);

        }

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(BindIntent.class.getCanonicalName());
        log("types=" + types);
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        SourceVersion version = SourceVersion.RELEASE_7;
        //    SourceVersion version = SourceVersion.latestSupported();
        log("version=" + version);
        return version;
    }

    private void log(String msg) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "BindIntentProcessor xxx " + msg);
        System.out.println("BindIntentProcessor " + msg);
    }

    /**
     * 使用javapoet生成java文件
     * @param classInfo
     */
    private void generateJavaFile(ClassInfo classInfo) {
        try {
            TypeSpec.Builder builder = TypeSpec.classBuilder(splitClassName(classInfo.classname)[1] + "_BindIntent")
                    .addModifiers(Modifier.PUBLIC);
            // 生成target字段
            TypeName targetClass = getClassName(classInfo.classname);

            FieldSpec targetField = FieldSpec.builder(targetClass, "target")
                    .addModifiers(Modifier.PRIVATE)
                    .build();
            builder.addField(targetField);
            FieldSpec intentField = FieldSpec.builder(ClassName.get("android.content", "Intent"), "intent")
                    .addModifiers(Modifier.PRIVATE)
                    .build();
            builder.addField(intentField);

            // 生成构造函数
            MethodSpec constructor = MethodSpec.constructorBuilder()
                    .addParameter(ParameterSpec.builder(targetClass, "target").build())
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("this.target = target")
                    .addStatement("this.intent = target.getIntent()")
                    .build();
            builder.addMethod(constructor);

            MethodSpec.Builder setMethod = MethodSpec.methodBuilder("bind")
                    .addModifiers(Modifier.PUBLIC);
            setMethod.addStatement("GetSetter getSetter = (GetSetter) intent.getSerializableExtra(\"key\");");
            for(FieldInfo info : classInfo.fields) {
                StringBuilder sb = new StringBuilder();
                sb.append("this.target.")
                        .append(info.name)
                        .append("=")
                        .append("getSetter.get")
                        .append(info.name.substring(0, 1).toUpperCase())
                        .append(info.name.substring(1)).append("()");
                setMethod.addStatement(sb.toString());
            }

            builder.addMethod(setMethod.build());


            builder.addType(
                    addGetSet(classInfo)
            );

            JavaFile javaFile = JavaFile.builder(classInfo.pkgName, builder.build()).build();
            javaFile.writeTo(mFiler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TypeSpec addGetSet(ClassInfo classInfo) {

        TypeSpec.Builder builder = TypeSpec.classBuilder("GetSetter")
                .addModifiers(Modifier.STATIC)
                .addModifiers(Modifier.PUBLIC)
                ;
        builder.addSuperinterface(ClassName.get("java.io", "Serializable"));
        for(FieldInfo info : classInfo.fields) {
            FieldSpec.Builder fieldBuild = FieldSpec.builder(getClassName(info.type), info.name);
            fieldBuild.addModifiers(Modifier.PRIVATE);
            builder.addField(fieldBuild.build());

            String nameWithAlpha = info.name.substring(0, 1).toUpperCase() + info.name.substring(1);

            MethodSpec.Builder setMethod = MethodSpec.methodBuilder("set" + nameWithAlpha)
                    .addModifiers(Modifier.PUBLIC);
            setMethod.addParameter(getClassName(info.type), info.name);
            StringBuilder sbSet = new StringBuilder();
            sbSet.append("this.").append(info.name).append("=").append(info.name);
            setMethod.addStatement(sbSet.toString());
            builder.addMethod(setMethod.build());


            MethodSpec.Builder getMethod = MethodSpec.methodBuilder("get" + nameWithAlpha)
                    .addModifiers(Modifier.PUBLIC);
            getMethod.returns(getClassName(info.type));
            StringBuilder sbGet = new StringBuilder();
            sbGet.append("return this.").append(info.name);
            getMethod.addStatement(sbGet.toString());
            builder.addMethod(getMethod.build());
        }

        return builder.build();
    }


    private class FieldInfo {
        String name;
        String type;
        String key;
    }

    private class ClassInfo {
        String pkgName;
        String classname;
        List<FieldInfo> fields;
    }

    private TypeName getClassName(String classname) {
        switch (classname) {
            case "void":
                return TypeName.VOID;
            case "boolean":
                return TypeName.BOOLEAN;
            case "byte":
                return TypeName.BYTE;
            case "short":
                return TypeName.SHORT;
            case "int":
                return TypeName.INT;
            case "long":
                return TypeName.LONG;
            case "char":
                return TypeName.CHAR;
            case "float":
                return TypeName.FLOAT;
            case "double":
                return TypeName.DOUBLE;
            default:
                String[] fields = splitClassName(classname);
                return ClassName.get(fields[0], fields[1]);

        }
    }

    private String[] splitClassName(String classname) {
        int pos = classname.lastIndexOf('.');
        if(pos == -1) { // int等基础类型
            return null;
        }
        return new String[]{classname.substring(0, pos), classname.substring(pos + 1)};
    }
}