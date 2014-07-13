package org.cthul.api4j.api1;

import com.thoughtworks.qdox.model.*;
import com.thoughtworks.qdox.model.impl.AbstractBaseJavaEntity;
import com.thoughtworks.qdox.model.impl.AbstractBaseMethod;
import com.thoughtworks.qdox.model.impl.DefaultDocletTag;
import com.thoughtworks.qdox.model.impl.DefaultJavaClass;
import com.thoughtworks.qdox.model.impl.DefaultJavaField;
import com.thoughtworks.qdox.model.impl.DefaultJavaMethod;
import com.thoughtworks.qdox.model.impl.DefaultJavaParameter;
import com.thoughtworks.qdox.model.impl.DefaultJavaTypeVariable;
import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import java.util.*;
import org.cthul.api4j.api.PatternSearcher;
import org.cthul.api4j.gen.*;
import org.cthul.api4j.groovy.DslUtils;
import org.cthul.api4j.groovy.GroovyDsl;
import org.cthul.strings.JavaNames;

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
    
    public static GeneratedClass nestedClass(DefaultJavaClass jc, String name) {
        GeneratedClass gc = new GeneratedClass(name);
        jc.addClass(gc);
        gc.setParentClass(jc);
        return gc;
    }
    
    public Object nestedClass(DefaultJavaClass jc, String name, Closure<?> closure) {
        return nestedClass(jc, name).configure(api1.dsl(), closure);
    }
    
    public static GeneratedClass nestedInterface(DefaultJavaClass jc, String name) {
        GeneratedClass gc = nestedClass(jc, name);
        gc.setInterface(true);
        return gc;
    }
    
    public Object nestedInterface(DefaultJavaClass jc, String name, Closure<?> closure) {
        return nestedInterface(jc, name).configure(api1.dsl(), closure);
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
    
    public void docTags(AbstractBaseJavaEntity je, String... tags) {
        docTags(je, Arrays.asList(tags));
    }
    
    public void docTags(AbstractBaseJavaEntity je, List<String> tags) {
        List<DocletTag> newTags = new ArrayList<>();
        for (String s: tags) {
            String[] t = s.split(" ", 1);
            newTags.add(new DefaultDocletTag(t[0], t.length > 1 ? t[1] : ""));
        }
        je.setTags(newTags);
    }
    
    public static DefaultJavaField generateField(JavaClass jc, String name) {
        DefaultJavaField newField = new DefaultJavaField(name);
        newField.setModifiers(new ModifierList());
        newField.setParentClass(jc);
        jc.getFields().add(newField);
        return newField;
    }
    
    public DefaultJavaField generateField(JavaClass jc, Map<String, Object> map, String name) {
        DefaultJavaField newField = generateField(jc, name);
        GroovyObject go = (GroovyObject) dsl.wrap(newField);
        for (Map.Entry<String, Object> e: map.entrySet()) {
            go.setProperty(e.getKey(), e.getValue());
        }
        return newField;
    }
    
    public Object generateField(JavaClass jc, String name, Closure<?> cfg) {
        DefaultJavaField newField = generateField(jc, name);
        return DslUtils.configure(dsl, newField, cfg);
    }
    
    public Object generateField(JavaClass jc, Map<String, Object> map, String name, Closure<?> cfg) {
        DefaultJavaField newField = generateField(jc, map, name);
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
    
    public static GeneratedMethod generateMethod(JavaClass jc, String name) {
        GeneratedMethod gm = new GeneratedMethod(name);
        gm.setParentClass(jc);
        jc.getMethods().add(gm);
        return gm;
    }
    
    public GeneratedMethod generateMethod(JavaClass jc, String name, Closure<?> cfg) {
        GeneratedMethod gm = generateMethod(jc, name);
        DslUtils.configure(dsl, gm, cfg);
        return gm;
    }
    
    public static GeneratedMethod generateMethod(JavaClass jc, JavaMethod method) {
        GeneratedMethod gm = new GeneratedMethod(jc, method);
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
        body(m).leftShift(string);
    }
    
    public static MethodBody body(AbstractBaseMethod m) {
        return new MethodBody(m);
    }
    
    public static void modifiers(JavaMember jm, String modifiers) {
        jm.getModifiers().add(modifiers);
    }
    
    public void returns(DefaultJavaMethod method, String type) {
        JavaClass jc = api1.getConfiguration().getQdox().getClassByName(type);
        method.setReturns(jc);
    }
    
    public void type(DefaultJavaField field, String type) {
        JavaClass jc = api1.getConfiguration().getQdox().getClassByName(type);
        field.setType(jc);
    }
    
    public GeneratedMethod generateGetter(JavaField field) {
        return generateGetter(field, Collections.<String, String>emptyMap());
    }
    
    public GeneratedMethod generateGetter(JavaField field, Map<String, String> map) {
        JavaClass type = field.getType();
        String name;
        Object arg;
        arg = map.remove("name");
        if (arg != null) {
            name = arg.toString();
        } else {
            boolean isBool = false;
            arg = map.remove("isBool");
            if (arg != null) {
                isBool = (Boolean) arg;
            } else if (type.getName().equals("boolean") || type.getName().endsWith("Boolean")) {
                isBool = true;
            }
            name = JavaNames.CamelCase(field.getName());
            name = (isBool ? "is" : "get") + name;
        }
        GeneratedMethod getter = generateMethod(field.getDeclaringClass(), name);
        getter.setReturns(type);
        getter.setSourceCode(String.format("return this.%s;", field.getName()));
        arg = map.remove("modifiers");
        if (arg != null) {
            getter.getModifiers().add((String) arg);
        } else {
            getter.getModifiers().add("public");
        }
        return getter;
    }
    
    public Object generateGetter(JavaField field, Closure<?> cgf) {
        GeneratedMethod getter = generateGetter(field);
        return DslUtils.configure(dsl, getter, cgf);
    }
    
    public Object generateGetter(JavaField field, Map<String, String> map, Closure<?> cgf) {
        GeneratedMethod getter = generateGetter(field, map);
        return DslUtils.configure(dsl, getter, cgf);
    }
    
    public GeneratedMethod generateSetter(JavaField field) {
        return generateSetter(field, Collections.<String, String>emptyMap());
    }
    
    public GeneratedMethod generateSetter(JavaField field, Map<String, String> map) {
        Object arg;
        String name;
        boolean fluent = false;
        arg = map.remove("fluent");
        if (arg != null) {
            fluent = (Boolean) arg;
        }
        arg = map.remove("name");
        if (arg != null) {
            name = arg.toString();
        } else if (fluent) {
            name = field.getName();
        } else {
            name = "set" + JavaNames.CamelCase(field.getName());
        }
        GeneratedMethod setter = generateMethod(field.getDeclaringClass(), name);
        JavaParameter param = new DefaultJavaParameter(field.getType(), field.getName());
        setter.getParameters().add(param);
        setter.setSourceCode(String.format("this.%s = %<s;%n", field.getName()));
        arg = map.remove("modifiers");
        if (arg != null) {
            setter.getModifiers().add((String) arg);
        } else {
            setter.getModifiers().add("public");
        }
        if (fluent) {
            setter.setReturns(field.getDeclaringClass());
            body(setter).leftShift("return this;");
        }
        return setter;
    }
    
    public Object generateSetter(JavaField field, Closure<?> cgf) {
        GeneratedMethod setter = generateSetter(field);
        return DslUtils.configure(dsl, setter, cgf);
    }
    
    public Object generateSetter(JavaField field, Map<String, String> map, Closure<?> cgf) {
        GeneratedMethod setter = generateSetter(field, map);
        return DslUtils.configure(dsl, setter, cgf);
    }
    
    public void interfaces(DefaultJavaClass jc, Collection<?> c) {
        List<JavaClass> list = new ArrayList<>(c.size());
        for (Object o: c) {
            if (o instanceof JavaClass) {
                list.add((JavaClass) o);
            } else {
                JavaClass iface = api1.getConfiguration().getQdox().getClassByName(o.toString());
                list.add(iface);
            }
        }
        jc.setImplementz(list);
    }
    
    public void typeParameters(DefaultJavaClass jc, String... strings) {
        typeParameters(jc, Arrays.asList(strings));
    }
    
    public void typeParameters(DefaultJavaClass jc, Collection<?> c) {
        List<DefaultJavaTypeVariable<JavaClass>> list = new ArrayList<>(c.size());
        for (Object o: c) {
            if (o instanceof DefaultJavaTypeVariable) {
                list.add((DefaultJavaTypeVariable) o);
            } else {
                String def = o.toString();
                int iExtends = def.indexOf(" extends ");
                String name = iExtends > 0 ? def.substring(0, iExtends) : def;
                DefaultJavaTypeVariable<JavaClass> var = new DefaultJavaTypeVariable<JavaClass>(name, jc);
                if (iExtends > 0) {
                    for (String b: def.substring(iExtends+9).split("&")) {
                        JavaClass bound = api1.getConfiguration().getQdox().getClassByName(b);
                        var.getBounds().add(bound);
                    }
                }
                list.add(var);
            }
        }
        jc.setTypeParameters(list);
    }
}
