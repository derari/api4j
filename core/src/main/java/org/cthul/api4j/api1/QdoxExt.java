package org.cthul.api4j.api1;

import com.thoughtworks.qdox.model.*;
import com.thoughtworks.qdox.model.impl.AbstractBaseMethod;
import com.thoughtworks.qdox.model.impl.DefaultJavaField;
import groovy.lang.Closure;
import java.util.*;
import org.cthul.api4j.api.PatternSearcher;
import org.cthul.api4j.gen.*;
import org.cthul.api4j.groovy.DslUtils;
import org.cthul.api4j.groovy.GroovyDsl;

public class QdoxExt {

    Api1 api1;
    GroovyDsl dsl;

    public QdoxExt(Api1 api1, GroovyDsl dsl) {
        this.api1 = api1;
        this.dsl = dsl;
    }

    public static boolean hasAnnotation(JavaAnnotatedElement jm, String... patterns) {
        PatternSearcher searcher = PatternSearcher.forPatterns(patterns);
        for (JavaAnnotation at: jm.getAnnotations()) {
            JavaClass jc = at.getType();
            if (searcher.eval(jc)) return true;
        }
        return false;
    }
    
    public static String argumentsString(JavaMethod jm) {
        return argumentsString(jm, Collections.<String,String>emptyMap());
    }
    
    public static String argumentsString(JavaMethod jm, Map<String, String> replace) {
        return ParameterStringDirective.ARGUMENTS.build(jm.getParameters(), replace);
    }
    
    public MethodSignature signature(AbstractBaseMethod m) {
        return new MethodSignature(api1.getConfiguration().getQdox(), m);
    }
    
    public void signature(AbstractBaseMethod m, String string) {
        m.getParameters().clear();
        signature(m).leftShift(string);
    }
    
    public static DefaultJavaField generateField(JavaClass jc, String name) {
        DefaultJavaField newField = new DefaultJavaField(name);
        newField.setModifiers(new ModifierList());
        newField.setParentClass(jc);
        jc.getFields().add(newField);
        return newField;
    }
    
    public Object generateField(JavaClass jc, String name, Closure<?> cfg) {
        DefaultJavaField newField = generateField(jc, name);
        return DslUtils.configure(dsl, newField, cfg);
    }
    
    public static GeneratedConstructor generateConstructor(JavaClass jc) {
        GeneratedConstructor gc = new GeneratedConstructor();        
        gc.setParentClass(jc);
        jc.getConstructors().add(gc);
        return gc;
    }
    
    public static GeneratedConstructor generateConstructor(JavaClass jc, JavaMethod m) {
        GeneratedConstructor gc = new GeneratedConstructor(m);
        gc.setParentClass(jc);
        jc.getConstructors().add(gc);
        return gc;
    }
    
    public Object generateConstructor(JavaClass jc, Closure<?> cfg) {
        GeneratedConstructor gc = generateConstructor(jc);
        return DslUtils.configure(dsl, gc, cfg);
    }
    
    public void generateConstructors(JavaClass jc, Collection<JavaMethod> methods, Closure<?> cfg) {
        for (JavaMethod jm: methods) {
            GeneratedConstructor gc = generateConstructor(jc, jm);
            DslUtils.runClosureOn(dsl, gc, cfg, jm);
        }
    }
    
    public GeneratedMethod generateMethod(JavaClass jc, String name) {
        GeneratedMethod gm = new GeneratedMethod(name);
        gm.setParentClass(jc);
        jc.getMethods().add(gm);
        return gm;
    }
    
    public void generateMethods(JavaClass jc, Collection<JavaMethod> methods, Closure<?> cfg) {
        List<JavaMethod> generated = GeneratedModel.copyAll(
                methods, jc, GeneratedModel.COPY_METHOD, dsl, cfg);
        jc.getMethods().addAll(generated);
    }
    
    public static void body(AbstractBaseMethod m, Object body) {
        if (body == null) {
            m.setSourceCode(null);
        } else {
            String text = body.toString().trim();
            if (!text.isEmpty()) text += "\n";
            m.setSourceCode(text);
        }
    }
    
    public static void body(AbstractBaseMethod m, String string, Object... args) {
        string = String.format(string, args);
        body(m, string);
    }
    
    public static MethodBody body(AbstractBaseMethod m) {
        return new MethodBody(m);
    }
    
    public static void modifiers(JavaMember jm, String modifiers) {
        jm.getModifiers().add(modifiers);
    }
    
    public void type(DefaultJavaField field, String type) {
        JavaClass jc = api1.getConfiguration().getQdox().getClassByName(type);
        field.setType(jc);
    }
    
    public void generateGetter(JavaField field) {
        Map<String, String> map = new HashMap<>();
        map.put("modifiers", "public");
        generateGetter(field, map);
    }
    
    public void generateGetter(JavaField field, Map<String, String> map) {
        JavaClass type = field.getType();
        boolean isBool = false;
        Object arg = map.remove("isBool");
        if (arg != null) {
            isBool = (Boolean) arg;
        } else if (type.getName().equals("boolean") || type.getName().endsWith("Boolean")) {
            isBool = true;
        }
        String name = field.getName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        name = (isBool ? "is" : "get") + name;
        GeneratedMethod getter = generateMethod(field.getDeclaringClass(), name);
        getter.setReturns(type);
        getter.setSourceCode(String.format("return this.%s;", field.getName()));
        arg = map.remove("modifiers");
        if (arg != null) {
            getter.getModifiers().add((String) arg);
        }
    }
    
}
