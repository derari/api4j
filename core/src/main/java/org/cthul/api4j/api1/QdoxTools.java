package org.cthul.api4j.api1;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.*;
import com.thoughtworks.qdox.model.impl.AbstractBaseJavaEntity;
import com.thoughtworks.qdox.model.impl.AbstractBaseMethod;
import com.thoughtworks.qdox.model.impl.AbstractJavaEntity;
import com.thoughtworks.qdox.model.impl.DefaultDocletTag;
import com.thoughtworks.qdox.model.impl.DefaultJavaClass;
import com.thoughtworks.qdox.model.impl.DefaultJavaConstructor;
import com.thoughtworks.qdox.model.impl.DefaultJavaField;
import com.thoughtworks.qdox.model.impl.DefaultJavaMethod;
import com.thoughtworks.qdox.model.impl.DefaultJavaParameter;
import com.thoughtworks.qdox.model.impl.DefaultJavaParameterizedType;
import com.thoughtworks.qdox.model.impl.DefaultJavaTypeVariable;
import groovy.lang.Closure;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.cthul.api4j.Api4JConfiguration;
import org.cthul.api4j.api.PatternSearcher;
import org.cthul.api4j.gen.*;
import org.cthul.api4j.groovy.DslUtils;
import org.cthul.api4j.groovy.NamedClosure;
import org.cthul.strings.JavaNames;

/**
 * Utility methods related to the Java class model, 
 * intended for static import in Groovy and Java scripts.
 */
public class QdoxTools {
    
    /**
     * Returns the current Qdox instance.
     * @return java project builder
     */
    public static JavaProjectBuilder getQdox() {
        return cfg().getQdox();
    }
    
    private static Api4JConfiguration cfg() {
        return Globals.getExisting(Api4JConfiguration.class);
    }
    
    private static Api1 api1() {
        Api1 api1 = Globals.getExisting(Api1.class);
        return api1;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Collection Utils">
    /**
     * @see GeneralTools#asClass(java.lang.String) 
     */
    public static JavaClass asClass(String className) {
        return GeneralTools.instance().asClass(className);
    }
    
    /**
     * @see GeneralTools#classes(java.util.Collection) 
     */
    public static List<JavaClass> asClasses(Collection<?> classNames) {
        return GeneralTools.instance().classes(
                classNames.stream()
                        .map(Object::toString)
                        .collect(Collectors.toList()));
    }
    
    /**
     * @see GeneralTools#asType(java.lang.String) 
     */
    public static JavaClass asType(String className) {
        return GeneralTools.instance().asType(className);
    }
    
    /**
     * @see GeneralTools#types(java.util.Collection) 
     */
    public static List<JavaClass> asTypes(Collection<?> classNames) {
        return GeneralTools.instance().types(
                classNames.stream()
                        .map(Object::toString)
                        .collect(Collectors.toList()));
    }
    
    /**
     * Returns a flat list of all methods of {@code classes}
     * @param classes
     * @return methods
     */
    public static List<JavaMethod> allMethods(Collection<JavaClass> classes) {
        return classes.stream().flatMap((jc) -> jc.getMethods(false).stream())
                .collect(ArrayList::new, List::add, List::addAll);
    }
    
    /**
     * Returns a flat list of all constructors of {@code classes}
     * @param classes
     * @return constructors
     */
    public static List<JavaConstructor> allConstructors(Collection<JavaClass> classes) {
        return classes.stream().flatMap((jc) -> jc.getConstructors().stream())
                .collect(ArrayList::new, List::add, List::addAll);
    }
    
    /**
     * Returns a flat list of all fields of {@code classes}
     * @param classes
     * @return fields
     */
    public static List<JavaField> allFields(Collection<JavaClass> classes) {
        return classes.stream().flatMap((jc) -> jc.getFields().stream())
                .collect(ArrayList::new, List::add, List::addAll);
    }
    
    public static JavaClass getAsClass(String className) {
        return asClass(className);
    }
    
    public static Collection<JavaClass> getAsClasses(Collection<?> classNames) {
        return asClasses(classNames);
    }
    
    public static Collection<JavaMethod> getAllMethods(JavaClass clazz) {
        return clazz.getMethods(false);
    }
    
    public static Collection<JavaMethod> getAllMethods(Collection<JavaClass> classes) {
        return allMethods(classes);
    }
    
    public static Collection<JavaConstructor> getAllConstructors(JavaClass clazz) {
        return clazz.getConstructors();
    }
    
    public static Collection<JavaConstructor> getAllConstructors(Collection<JavaClass> classes) {
        return allConstructors(classes);
    }
    
    public static Collection<JavaField> getFields(Collection<JavaClass> classes) {
        return allFields(classes);
    }    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Model Generators">
    
    /**
     * Generates a class using the default name.
     * @param api1
     * @return generated class
     */
    public static GeneratedClass generatedClass(Api1 api1) {
        return api1.createClass();
    }
    
    /**
     * Generates a class with the given name.
     * @param api1
     * @param name
     * @return generated class
     */
    public static GeneratedClass generatedClass(Api1 api1, String name) {
        return api1.createClass(name);
    }
    
    /**
     * Generates a class with the given name and applies the closure.
     * @param api1
     * @param nc
     * @return generated class
     */
    public static GeneratedClass generatedClass(Api1 api1, NamedClosure<?> nc) {
        return generatedClass(api1, nc.getName(), nc.getClosure());
    }
    
    private static MultiConfigFluent<GeneratedClass> cfgGeneratedClass(Api1 api1, Object... args) {
        return new ConfigArgs<>(String.class, args)
                .all(() -> generatedClass(api1), (s) -> generatedClass(api1, s))
                .withString(QdoxTools::expectNoString)
                .withProperties(QdoxTools::classProperties)
                .closureWithSelf(DslUtils::configureWith);
    }
    
    /**
     * @see #generatedClass(java.lang.Object...) 
     */
    public static GeneratedClass generatedClass(Api1 api1, Object... args) {
        return cfgGeneratedClass(api1, args).getSingle();
    }
    
    /**
     * @see #generatedClasses(java.lang.Object...) 
     */
    public static List<GeneratedClass> generatedClasses(Api1 api1, Object... args) {
        return cfgGeneratedClass(api1, args).getResult();
    }
    
    /**
     * Generates and initializes a class.
     * A string argument will be the class name, 
     * a map will be used to initialize properties,
     * a closure will be run with the class as delegatee.
     * @param args
     * @return generated class.
     */
    public static GeneratedClass generatedClass(Object... args) {
        return generatedClass(api1(), args);
    }

    /**
     * Generates and initializes classes from a collection of class names.
     * A map will be used to initialize properties,
     * a closure will be run with each class as delegatee.
     * @param args
     * @return generated class.
     */
    public static List<GeneratedClass> generatedClasses(Object... args) {
        return generatedClasses(api1(), args);
    }
    
    /**
     * Generates an interface.
     * @see #generatedClass(org.cthul.api4j.api1.Api1) 
     */
    public static GeneratedClass generatedInterface(Api1 api1) {
        GeneratedClass gc = generatedClass(api1);
        gc.setInterface(true);
        return gc;
    }
    
    /**
     * Generates an interface.
     * @see #generatedClass(org.cthul.api4j.api1.Api1, String) 
     */
    public static GeneratedClass generatedInterface(Api1 api1, String name) {
        GeneratedClass gc = generatedClass(api1, name);
        gc.setInterface(true);
        return gc;
    }
    
    /**
     * Generates an interface.
     * @see #generatedClass(org.cthul.api4j.api1.Api1, NamedClosure) 
     */
    public static GeneratedClass generatedInterface(Api1 api1, NamedClosure<?> nc) {
        return generatedInterface(api1, nc.getName(), nc.getClosure());
    }
    
    private static MultiConfigFluent<GeneratedClass> cfgGeneratedInterface(Api1 api1, Object... args) {
        return new ConfigArgs<>(String.class, args)
                .all(() -> generatedInterface(api1), (s) -> generatedInterface(api1, s))
                .withString(QdoxTools::expectNoString)
                .withProperties(QdoxTools::classProperties)
                .closureWithSelf(DslUtils::configureWith);
    }
    
    /**
     * Generates an interface.
     * @see #generatedClass(org.cthul.api4j.api1.Api1, java.lang.Object...) 
     */
    public static GeneratedClass generatedInterface(Api1 api1, Object... args) {
        return cfgGeneratedInterface(api1, args).getSingle();
    }
    
    /**
     * Generates interfaces.
     * @see #generatedClasses(org.cthul.api4j.api1.Api1, java.lang.Object...) 
     */
    public static List<GeneratedClass> generatedInterfaces(Api1 api1, Object... args) {
        return cfgGeneratedInterface(api1, args).getResult();
    }
    
    /**
     * Generates an interface.
     * @see #generatedClass(java.lang.Object...) 
     */
    public static GeneratedClass generatedInterface(Object... args) {
        return generatedInterface(api1(), args);
    }
    
    /**
     * Generates interfacees.
     * @see #generatedClasses(java.lang.Object...) 
     */
    public static List<GeneratedClass> generatedInterfaces(Object... args) {
        return generatedInterfaces(api1(), args);
    }
    
    /**
     * Adds a nested class to {@code parent}.
     * @param parent
     * @return nested class
     */
    public static GeneratedClass nestedClass(DefaultJavaClass parent) {
        return nestedClass(parent, (String) null);
    }
    
    /**
     * Adds a nested class to {@code parent}.
     * If no name is given, a unique name will be generated.
     * @param parent
     * @param name
     * @return nested class
     */
    public static GeneratedClass nestedClass(DefaultJavaClass parent, String name) {
        if (name == null) {
            int i = 0;
            while (parent.getNestedClassByName("__" + i) != null) i++;
            name = "__" + i;
        }
        GeneratedClass gc = new GeneratedClass(name);
        parent.addClass(gc);
        gc.setParentClass(parent);
        return gc;
    }
    
    private static MultiConfigFluent<GeneratedClass> cfgNestedClass(DefaultJavaClass jc, Object... args) {
        return new ConfigArgs<>(String.class, args)
                .all(() -> nestedClass(jc), (s) -> nestedClass(jc, s))
                .withString(QdoxTools::expectNoString)
                .withProperties(QdoxTools::classProperties)
                .closureWithSelf(DslUtils::configureWith);
    }
    
    /**
     * Generates a nested class.
     * @param parent
     * @param args
     * @return nested class
     * @see #generatedClass(java.lang.Object...) 
     */
    public static GeneratedClass nestedClass(DefaultJavaClass parent, Object... args) {
        return cfgNestedClass(parent, args).getSingle();
    }
    
    /**
     * Generates nested classes from templates.
     * @param parent
     * @param args
     * @return nested classes
     * @see #generatedClasses(java.lang.Object...) 
     */
    public static List<GeneratedClass> nestedClasses(DefaultJavaClass parent, Object... args) {
        return cfgNestedClass(parent, args).getResult();
    }
    
    /**
     * Generates a nested interface.
     * @param parent
     * @return nested interface
     */
    public static GeneratedClass nestedInterface(DefaultJavaClass parent) {
        GeneratedClass gc = nestedClass(parent);
        gc.setInterface(true);
        return gc;
    }
    
    /**
     * Generates a nested interface.
     * @param parent
     * @param name
     * @return nested interface
     */
    public static GeneratedClass nestedInterface(DefaultJavaClass parent, String name) {
        GeneratedClass gc = nestedClass(parent, name);
        gc.setInterface(true);
        return gc;
    }
    
    private static MultiConfigFluent<GeneratedClass> cfgNestedInterface(DefaultJavaClass jc, Object... args) {
        return new ConfigArgs<>(String.class, args)
                .all(() -> nestedInterface(jc), (s) -> nestedInterface(jc, s))
                .withString(QdoxTools::expectNoString)
                .withProperties(QdoxTools::classProperties)
                .closureWithSelf(DslUtils::configureWith);
    }
    
    /**
     * Generates a nested interface.
     * @param parent
     * @param args
     * @return nested interface
     * @see #generatedClass(java.lang.Object...) 
     */
    public static GeneratedClass nestedInterface(DefaultJavaClass parent, Object... args) {
        return cfgNestedInterface(parent, args).getSingle();
    }
    
    /**
     * Generates nested interfaces from templates.
     * @param jc
     * @param args
     * @return nested interfaces
     * @see #generatedClasses(java.lang.Object...) 
     */
    public static List<GeneratedClass> nestedInterfacees(DefaultJavaClass jc, Object... args) {
        return cfgNestedInterface(jc, args).getResult();
    }
    
    /**
     * Adds a field to the class.
     * @param jc
     * @return generated field
     */
    public static DefaultJavaField field(JavaClass jc) {
        DefaultJavaField newField = new DefaultJavaField();
        newField.setModifiers(new ModifierList());
        newField.setParentClass(jc);
        jc.getFields().add(newField);
        return newField;
    }
    
    /**
     * Adds a field to the class.
     * @param jc
     * @param declaration
     * @return generated field
     */
    public static DefaultJavaField field(JavaClass jc, String declaration) {
        DefaultJavaField newField = field(jc);
        setDeclaration(newField, declaration);
        return newField;
    }
    
    private static MultiConfigFluent<DefaultJavaField> cfgField(JavaClass jc, Object... args) {
        return new ConfigArgs<>(String.class, args)
                .all(() -> field(jc), (s) -> field(jc, s))
                .withString(QdoxTools::expectNoString)
                .withProperties(DslUtils::applyProperties)
                .closureWithSelf(DslUtils::configureWith);
    }
    
    /**
     * Adds a field to the class.
     * A string argument will be the field's declaration, 
     * a map will be used to initialize properties,
     * a closure will be run with the field as delegatee.
     * @param jc
     * @param args
     * @return generated field
     */
    public static DefaultJavaField field(JavaClass jc, Object... args) {
        return cfgField(jc, args).getSingle();
    }
    
    /**
     * Adds fields to the class from a collection of field declaration strings.
     * @param jc
     * @param args
     * @return generated fields
     */
    public static List<DefaultJavaField> fields(JavaClass jc, Object... args) {
        return cfgField(jc, args).getResult();
    }
    
    public static GeneratedConstructor constructor(JavaClass jc) {
        GeneratedConstructor gc = new GeneratedConstructor();
        gc.setParentClass(jc);
        jc.getConstructors().add(gc);
        return gc;
    }
    
    public static GeneratedConstructor constructor(JavaClass jc, JavaMethod m) {
        GeneratedConstructor gc = new GeneratedConstructor(m);
        gc.setParentClass(jc);
        jc.getConstructors().add(gc);
        return gc;
    }
    
    private static GeneratedConstructor cfgNewConstructor(JavaClass jc, Object o) {
        if (o instanceof JavaMethod) {
            return constructor(jc, (JavaMethod) o);
        } else if(o instanceof String) {
            GeneratedConstructor gc = constructor(jc);
            setDeclaration(gc, (String) o);
            return gc;
        } else {
            throw new IllegalArgumentException("" + o);
        }
    }
    
    private static MultiConfigFluent<GeneratedConstructor> cfgConstructor(JavaClass jc, Object... args) {
        return new ConfigArgs<>(JavaMethod.class, args)
                .all(() -> constructor(jc), (Object jm) -> cfgNewConstructor(jc, jm))
                .withString(QdoxTools::setDeclaration)
                .withProperties(QdoxTools::constructorProperties)
                .closureWithTemplate(DslUtils::configureWith);
    }
    
    public static GeneratedConstructor constructor(JavaClass jc, Object... args) {
        return cfgConstructor(jc, args).getSingle();
    }
    
    public static List<GeneratedConstructor> constructors(JavaClass jc, Object... args) {
        return cfgConstructor(jc, args).getResult();
    }
    
    public static GeneratedMethod method(JavaClass jc) {
        GeneratedMethod gm = new GeneratedMethod();
        gm.setParentClass(jc);
        jc.getMethods().add(gm);
        return gm;
    }
    
    public static GeneratedMethod method(JavaClass jc, JavaMethod method) {
        GeneratedMethod gm = new GeneratedMethod(jc, method);
        jc.getMethods().add(gm);
        return gm;
    }
    
    private static GeneratedMethod cfgNewMethod(JavaClass jc, Object o) {
        if (o instanceof JavaMethod) {
            return method(jc, (JavaMethod) o);
        } else if(o instanceof String) {
            GeneratedMethod gm = method(jc);
            setDeclaration(gm, (String) o);
            return gm;
        } else {
            throw new IllegalArgumentException("" + o);
        }
    }
    
    private static MultiConfigFluent<GeneratedMethod> cfgMethod(JavaClass jc, Object... args) {
        return new ConfigArgs<>(JavaMethod.class, args)
                .all(() -> method(jc), (Object jm) -> cfgNewMethod(jc, jm))
                .withString(QdoxTools::setDeclaration)
                .withProperties(QdoxTools::methodProperties)
                .closureWithTemplate(DslUtils::configureWith);
    }
    
    public static GeneratedMethod method(JavaClass jc, Object... args) {
        return cfgMethod(jc, args).getSingle();
    }
    
    public static List<GeneratedMethod> methods(JavaClass jc, Object... args) {
        return cfgMethod(jc, args).getResult();
    }
    
    public static GeneratedMethod getter(JavaField jf) {
        JavaClass type = jf.getType();
        boolean isBool = type.getName().equals("boolean") || type.getName().endsWith("Boolean");
        String name = (isBool ? "is" : "get") + JavaNames.CamelCase(jf.getName());
        return getter(jf, name);
    }
    
    public static GeneratedMethod getter(JavaField jf, String name) {
        JavaClass type = jf.getType();
        GeneratedMethod getter = method(jf.getDeclaringClass(), name);
        getter.setReturns(type);
        setBody(getter, "return this.%s;", jf.getName());
        getter.getModifiers().add("public");
        return getter;
    }
    
    private static MultiConfigFluent<GeneratedMethod> cfgGetter(JavaField jf, Object... args) {
        return new ConfigArgs<>(String.class, args)
                .all(() -> getter(jf), (s) -> getter(jf, s))
                .withString(QdoxTools::expectNoString)
                .withProperties(QdoxTools::getterProperties)
                .closureWithSelf(DslUtils::configureWith);
    }
    
    public static GeneratedMethod getter(JavaField jf, Object... args) {
        return cfgGetter(jf, args).getSingle();
    }
    
    public static List<GeneratedMethod> getters(JavaField jf, Object... args) {
        return cfgGetter(jf, args).getResult();
    }
    
    public static GeneratedMethod setter(JavaField jf) {
        String name = "set" + JavaNames.CamelCase(jf.getName());
        return setter(jf, name);
    }
    
    public static GeneratedMethod setter(JavaField jf, String name) {
        GeneratedMethod setter = method(jf.getDeclaringClass(), name);
        JavaParameter param = new DefaultJavaParameter(jf.getType(), jf.getName());
        setter.getParameters().add(param);
        setter.setSourceCode(String.format("this.%s = %<s;%n", jf.getName()));
        setter.getModifiers().add("public");
        return setter;
    }
    
    public static MultiConfigFluent<GeneratedMethod> cfgSetter(JavaField jf, Object... args) {
        return new ConfigArgs<>(String.class, args)
                .all(() -> setter(jf), (s) -> setter(jf, s))
                .withString(QdoxTools::expectNoString)
                .withProperties(QdoxTools::setterProperties)
                .closureWithSelf(DslUtils::configureWith);
    }
    
    public static GeneratedMethod setter(JavaField jf, Object... args) {
        return cfgSetter(jf, args).getSingle();
    }
    
    public static List<GeneratedMethod> setters(JavaField jf, Object... args) {
        return cfgSetter(jf, args).getResult();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Special Setters">
    
    /**
     * Applies a map of properties to a class.
     * @param clazz
     * @param properties 
     */
    public static void classProperties(GeneratedClass clazz, Map<String, Object> properties) {
        new PropertyFluent<>(clazz, properties)
                .property("declaration", QdoxTools::setDeclaration)
                .property("modifiers", QdoxTools::setModifiers)
                .remaining(DslUtils::applyProperties);
    }
    
    /**
     * Applies a map of properties to a field.
     * @param field
     * @param properties 
     */
    public static void fieldProperties(DefaultJavaField field, Map<String, Object> properties) {
        new PropertyFluent<>(field, properties)
                .property("declaration", QdoxTools::setDeclaration)
                .property("type", QdoxTools::setType)
                .property("modifiers", QdoxTools::setModifiers)
                .remaining(DslUtils::applyProperties);
    }
    
    public static void constructorProperties(DefaultJavaConstructor con, Map<String, Object> properties) {
        new PropertyFluent<>(con, properties)
                .property("declaration", QdoxTools::setDeclaration)
                .property("modifiers", QdoxTools::setModifiers)
                .remaining(DslUtils::applyProperties);
    }
    
    /**
     * Applies a map of properties to a method.
     * @param meth
     * @param properties 
     */
    public static void methodProperties(DefaultJavaMethod meth, Map<String, Object> properties) {
        new PropertyFluent<>(meth, properties)
                .property("declaration", QdoxTools::setDeclaration)
                .property("modifiers", QdoxTools::setModifiers)
                .property("returns", QdoxTools::setReturns)
                .property("body", QdoxTools::setBody)
                .property("see", QdoxTools::methodSeeTag)
                .remaining(DslUtils::applyProperties);
    }
    
    private static void methodSeeTag(DefaultJavaMethod meth, Object value) {
        if (value == Boolean.FALSE) {
            meth.getTags().remove(meth.getTags().size()-1);
        }
    }
    
    public static void getterProperties(DefaultJavaMethod getter, Map<String, Object> properties) {
        new PropertyFluent<>(getter, properties)
                .property("declaration", QdoxTools::setDeclaration)
                .property("isBool", (g, b) -> {
                    if (b != null && b != Boolean.FALSE) {
                        String name = g.getName();
                        if (name.startsWith("get")) name = name.substring(3);
                        if (!name.startsWith("is")) name = "is" + JavaNames.CamelCase(name);
                        g.setName(name);
                    }
                })
                .property("modifiers", QdoxTools::setModifiers)
                .property("returns", QdoxTools::setReturns)
                .property("body", QdoxTools::setBody)
                .remaining(DslUtils::applyProperties);
    }
    
    public static void setterProperties(DefaultJavaMethod setter, Map<String, Object> properties) {
        new PropertyFluent<>(setter, properties)
                .property("declaration", QdoxTools::setDeclaration)
                .property("body", QdoxTools::setBody)
                .property("fluent", (s, f) -> {
                    if (f == Boolean.FALSE) return;
                    if (f instanceof String[]) {
                        String[] ff = (String[]) f;
                        makeFluent(s, ff[0], ff[1]);
                    } else {
                        makeFluent(s);
                        if (f instanceof String) {
                            setter.setName((String) f);
                        }
                    }
                })
                .property("modifiers", QdoxTools::setModifiers)
                .property("returns", QdoxTools::setReturns)
                .remaining(DslUtils::applyProperties);
    }

    /**
     * Sets the declaration of a method, constructor, or field.
     * The string may contain modifiers, the name, the type for methods and fields,
     * and the signature for methods and constructors.
     * @param entity
     * @param decl 
     */
    public static void setDeclaration(JavaModel entity, String decl) {
        int iOpen = decl.indexOf('(');
        if (iOpen > -1) {
            int iClose = decl.indexOf(')');
            if (iClose < 0) iClose = decl.length();
            String sig = decl.substring(iOpen+1, iClose);
            setSignature((AbstractBaseMethod) entity, sig);
            decl = decl.substring(0, iOpen);
        }
        int iSpace = decl.length();
        if (!(entity instanceof JavaConstructor) && (entity instanceof AbstractJavaEntity)) {
            iSpace = decl.lastIndexOf(' ');
            String name = decl.substring(iSpace+1);
            if (isModifier(name)) {
                iSpace += name.length()+1;
            } else {
                ((AbstractJavaEntity) entity).setName(name);
                if (iSpace > 0) {
                    int end = iSpace;
                    iSpace = decl.lastIndexOf(' ', iSpace-1);
                    if (end - iSpace - 1 > 0) {
                        name = decl.substring(iSpace+1, end);
                        if (isModifier(name)) {
                            iSpace = end;
                        } else if (entity instanceof DefaultJavaField) {
                            setType((DefaultJavaField) entity, name);
                        } else if (entity instanceof DefaultJavaMethod) {
                            setReturns((DefaultJavaMethod) entity, name);
                        } else {
                            throw new IllegalArgumentException("Can't set type of " + entity);
                        }
                    }
                }
            }
        }
        if (iSpace > 0) {
            setModifiers(entity, decl.substring(0, iSpace));
        }
    }
    
    public static void setModifiers(JavaModel jm, String modifiers) {
        final List<String> modList;
        if (jm instanceof JavaMember) {
            modList = ((JavaMember) jm).getModifiers();
        } else if (jm instanceof JavaClass) {
            modList = ((JavaClass) jm).getModifiers();
        } else {
            throw new IllegalArgumentException(
                    "Expected class or member, got " + jm);
        }
        modList.clear();
        modList.add(modifiers);
    }
    
    public static void setModifier(JavaModel jm, String modifiers) {
        setModifiers(jm, modifiers);
    }
    
    public static boolean isModifier(String s) {
        return ModifierList.isModifier(s);
    }
    
    public static void setSignature(AbstractBaseMethod m, String string) {
        m.getParameters().clear();
        getSignature(m).leftShift(string);
    }

    public static MethodSignature getSignature(AbstractBaseMethod m) {
        return new MethodSignature(cfg().getQdox(), m);
    }
    
    public static void setBody(AbstractBaseMethod m, Object body) {
        if (body == null) {
            m.setSourceCode(null);
        } else {
            String text = body.toString().trim();
            if (text.isEmpty()) text = " ";
            if (!text.isEmpty()) text += "\n";
            m.setSourceCode(text);
        }
    }
    
    public static void setBody(AbstractBaseMethod m, String string, Object... args) {
        string = GeneralTools.format(string, args);
        setBody(m, string);
    }
    
    public static MethodBody getBody(AbstractBaseMethod m) {
        return new MethodBody(m);
    }
    
    public static void setReturns(DefaultJavaMethod method, Object type) {
        if (type instanceof JavaClass) {
            method.setReturns((JavaClass) type);
        } else {
            JavaClass jc = cfg().getQdox().getClassByName(type.toString());
            method.setReturns(jc);
        }
    }
    
    public static void setType(DefaultJavaField field, String type) {
        JavaClass jc = cfg().getQdox().getClassByName(type);
        field.setType(jc);
    }
    
    public static void makeFluent(DefaultJavaMethod gm) {
        makeFluent(gm, gm.getDeclaringClass(), "this");
    }
    
    public static void makeFluent(DefaultJavaMethod gm, String type, String flValue) {
        makeFluent(gm, asClass(type), flValue);
    }
    
    public static void makeFluent(DefaultJavaMethod gm, JavaClass type, String flValue) {
        gm.setReturns(type);
        getBody(gm).leftShift("return " + flValue + ";");
        String name = gm.getName();
        if (name.startsWith("set")) {
            name = name.substring(3, 4).toLowerCase() + name.substring(4);
            gm.setName(name);
        }
    }
    
    public static void setInterfaces(DefaultJavaClass jc, Collection<?> c) {
        List<JavaClass> list = new ArrayList<>(c.size());
        c.stream().forEach((o) -> {
            if (o instanceof JavaClass) {
                list.add((JavaClass) o);
            } else {
                JavaClass iface = cfg().getQdox().getClassByName(o.toString());
                list.add(iface);
            }
        });
        jc.setImplementz(list);
    }
    
    public void setParameters(DefaultJavaClass jc, String... strings) {
        setParameters(jc, Arrays.asList(strings));
    }
    
    public void setParameters(DefaultJavaClass jc, Collection<?> c) {
        List<DefaultJavaTypeVariable<JavaClass>> list = new ArrayList<>(c.size());
        c.stream().forEach((o) -> {
            if (o instanceof DefaultJavaTypeVariable) {
                list.add((DefaultJavaTypeVariable) o);
            } else {
                String def = o.toString();
                int iExtends = def.indexOf(" extends ");
                String name = iExtends > 0 ? def.substring(0, iExtends) : def;
                DefaultJavaTypeVariable<JavaClass> var = new DefaultJavaTypeVariable<>(name, jc);
                if (iExtends > 0) {
                    for (String b: def.substring(iExtends+9).split("&")) {
                        JavaClass bound = cfg().getQdox().getClassByName(b);
                        var.getBounds().add(bound);
                    }
                }
                list.add(var);
            }
        });
        jc.setTypeParameters(list);
    }
    
    public static void setDocTags(AbstractBaseJavaEntity je, String... tags) {
        setDocTags(je, Arrays.asList(tags));
    }
    
    public static void setDocTags(AbstractBaseJavaEntity je, List<String> tags) {
        List<DocletTag> newTags = new ArrayList<>();
        tags.stream().map((s) -> s.split(" ", 1)).forEach((t) -> {
            newTags.add(new DefaultDocletTag(t[0], t.length > 1 ? t[1] : ""));
        });
        je.setTags(newTags);
    }
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Special Getters">
    
    public static boolean hasAnnotation(JavaAnnotatedElement jm, String... patterns) {
        PatternSearcher searcher = PatternSearcher.forPatterns(patterns);
        return jm.getAnnotations().stream()
                .map(JavaAnnotation::getType)
                .anyMatch(searcher);
    }
    
    public static String getArgumentsString(JavaMethod jm) {
        return getArgumentsString(jm, Collections.<String,String>emptyMap());
    }
    
    public static String getArgumentsString(JavaMethod jm, Map<String, String> replace) {
        return ParameterStringDirective.ARGUMENTS.build(jm.getParameters(), replace);
    }

    public static String getArgumentsString(JavaConstructor jc) {
        return getArgumentsString(jc, Collections.<String,String>emptyMap());
    }
    
    public static String getArgumentsString(JavaConstructor jc, Map<String, String> replace) {
        return ParameterStringDirective.ARGUMENTS.build(jc.getParameters(), replace);
    }
    
    public static String getSignatureString(JavaMethod jm) {
        return getSignatureString(jm, Collections.<String,String>emptyMap());
    }
    
    public static String getSignatureString(JavaMethod jm, Map<String, String> replace) {
        return ParameterStringDirective.SIGNATURE.build(jm.getParameters(), replace);
    }

    public static String getSignatureString(JavaConstructor jc) {
        return getSignatureString(jc, Collections.<String,String>emptyMap());
    }
    
    public static String getSignatureString(JavaConstructor jc, Map<String, String> replace) {
        return ParameterStringDirective.SIGNATURE.build(jc.getParameters(), replace);
    }
    
    public static String getShortDocReference(JavaMember jm) {
        String name = "#" + jm.getName();
        if (!(jm instanceof ParameterDeclarator)) return name;
        List<JavaParameter> params;
        List<JavaTypeVariable<?>> vars = new ArrayList<>();
        if (jm instanceof JavaMethod) {
            params = ((JavaMethod) jm).getParameters();
            vars.addAll(((JavaMethod) jm).getTypeParameters());
            vars.addAll(((JavaMethod) jm).getDeclaringClass().getTypeParameters());
        } else {
            params = ((JavaConstructor) jm).getParameters();
            vars.addAll(((JavaConstructor) jm).getTypeParameters());
            vars.addAll(((JavaConstructor) jm).getDeclaringClass().getTypeParameters());
        }
        if (params.isEmpty()) return name + "()";
        StringBuilder sig = new StringBuilder(name).append('(');
        for (JavaParameter jp: params) {
            JavaType jt = jp.getType();
            String pType = jt.getCanonicalName();
            boolean generic = false;
            if (!vars.isEmpty() && !pType.contains(".")) {
                for (JavaTypeVariable<?> v: vars) {
                    if (v.getName().equals(pType)) {
                        List<JavaType> bounds = v.getBounds();
                        if (bounds == null || bounds.isEmpty()) sig.append("java.lang.Object");
                        else sig.append(bounds.get(0).getCanonicalName());
                        for (int i = 0; i < ((JavaClass) jt).getDimensions(); i++) {
                            sig.append("[]");
                        }
                        generic = true;
                        break;
                    }
                }
            }
            if (!generic) {
                sig.append(jt.getCanonicalName());
            }
            if (jp.isVarArgs()) {
                sig.append("...");
            }
            
            sig.append(',');
        }
        sig.setLength(sig.length()-1);
        return sig.append(')').toString();
    }

    public static String getDocReference(JavaMember jm) {
        return jm.getDeclaringClass().getCanonicalName() + getShortDocReference(jm);
    }
    
    //</editor-fold>
    
    /**
     * Forces a string into a list; the list should be known to support this.
     * @param autoParsingList
     * @param string 
     */
    public static void add(List<?> autoParsingList, String string) {
        ((List) autoParsingList).add(string);
    }
    
    /**
     * Forces strings into a list; the list should be known to support this.
     * @param autoParsingList
     * @param string 
     */
    public static void addAll(List<?> autoParsingList, Collection<String> string) {
        ((List) autoParsingList).addAll(string);
    }
    
    /**
     * Forces strings into a list; the list should be known to support this.
     * @param autoParsingList
     * @param string 
     */
    public static void addAll(List<?> autoParsingList, String... string) {
        addAll(autoParsingList, Arrays.asList(string));
    }
    
    public static JavaType withArgs(JavaClass jc, Collection<?> args) {
        return withArgs(jc, args.toArray());
    }
    
    /**
     * Adds type arguments to a java class.
     * @param jc
     * @param args
     * @return java type (or java class)
     */
    public static JavaType withArgs(JavaClass jc, Object... args) {
        if (args.length == 0) return jc;
        StringBuilder sb = new StringBuilder(jc.getCanonicalName());
        sb.append('<');
        for (Object a: args) {
            if (a instanceof JavaClass) {
                a = ((JavaClass) a).getGenericFullyQualifiedName();
            }
            sb.append(a).append(", ");
        }
        sb.setLength(sb.length()-2);
        sb.append('>');
        return asType(sb.toString());
    }
    
    /**
     * Resolves the upper bound of the {@code parameterIndex}th type parameter
     * of {@code parentType} in the {@code actualType}.
     * 
     * <p>Example: <pre>{@code
     *     class ToStringFunction<T> implements Function<T, String> {}
     * 
     *     resolveTypeArgument(StringFunction.class, Function.class, 1) // returns ´String´
     * }</pre>
     * @param actualType
     * @param parentType
     * @param parameterIndex
     * @return argument type
     */
    public static JavaType resolveTypeArgument(JavaClass actualType, JavaClass parentType, int parameterIndex) {
        if (!actualType.isA(parentType)) {
            throw new IllegalArgumentException(
                    actualType + " is not a " + parentType);
        } 
        return resolveTypeArg(actualType, parentType, parameterIndex).getType();
    }
    
    /**
     * Resolves the upper bound of the {@code parameterIndex}th type parameter
     * of {@code parentType} in the return type of {@code method}.
     * 
     * <p> This is different from calling {@code resolveTypeArgument(method.getReturns(), ...)},
     * for this method also considers the type parameters of {@code method}.
     * 
     * <p>Example: <pre>{@code
     *     
     *     public <T extends Number> Function<String, T> parser() { ... }
     * 
     *     JavaMethod mParser = // ´#parser()´
     *     resolveTypeArgument(mParser, Function.class, 1) // returns ´Number´
     * }</pre>
     * @param method
     * @param parentType
     * @param parameterIndex
     * @return 
     */
    public static JavaType resolveReturnTypeArgument(JavaMethod method, JavaClass parentType, int parameterIndex) {
        JavaClass actualType = method.getReturns();
        if (!actualType.isA(parentType)) {
            throw new IllegalArgumentException(
                    actualType + " is not a " + parentType);
        } 
        TypeArgument result = resolveTypeArg(actualType, parentType, parameterIndex);
        return result.resolve(method).getType();
    }
    
    private static TypeArgument resolveTypeArg(JavaClass actualType, JavaClass parentType, int parameterIndex) {
        if (actualType.getFullyQualifiedName().equals(parentType.getFullyQualifiedName())) {
            // actual type is parent type,
            // just look up the parameter at index
            DefaultJavaParameterizedType jp = (DefaultJavaParameterizedType) actualType;
            JavaType t = jp.getActualTypeArguments().get(parameterIndex);
            return new TypeArgument(t);
        } else {
            // first, try to resolve in super class or interface
            TypeArgument r = null;
            JavaClass sup = actualType.getSuperJavaClass();
            if (sup != null && sup.isA(parentType)) {
                r = resolveTypeArg(sup, parentType, parameterIndex);
            }
            if (r == null) {
                for (JavaClass iface: actualType.getInterfaces()) {
                    if (iface.isA(parentType)) {
                        r = resolveTypeArg(iface, parentType, parameterIndex);
                        break;
                    }
                }
            }
            if (r == null) {
                // if nothing found, it cannot be resolved
                return new TypeArgument(asClass("java.lang.Object"));
            }
            // resolve the result from super against the actual type
            return r.resolve(actualType);
        }
    }
    
    /**
     * Represents a type argument, the result of a resolving operation.
     */
    static class TypeArgument {
        JavaType type;
        public TypeArgument(JavaType type) {
            this.type = type;
        }

        /**
         * Returns the upper bound of this type argument.
         * @return type
         */
        public JavaType getType() {
            String name = type.getFullyQualifiedName();
            if (name.startsWith("? extends ")) {
                name = name.substring("? extends ".length()).trim();
                return asType(name);
            }
//            int iExtends = name.indexOf(" extends ");
//            if (iExtends > 0) {
//                return asType(name.substring(iExtends+9));
//            }
            if (name.contains(" super ")) {
                return asClass("java.lang.Object");
            }
            return type;
        }

        /**
         * Resolves this type argument against a type.
         * If this is a concrete type, return unchanged.
         * If this is a type variable, determine the corresponding type parameter
         * of {@code actualType}.
         * @param actualType
         * @return resolved type
         */
        public TypeArgument resolve(JavaClass actualType) {
            String name = type.getFullyQualifiedName();
            if (name.contains(".")) return this;
            int i = parameterIndex(name, actualType.getTypeParameters());
            if (i < 0) return this;
            DefaultJavaParameterizedType jp = (DefaultJavaParameterizedType) actualType;
            JavaType jt = jp.getActualTypeArguments().get(i);
            return new TypeArgument(jt);
        }

        /**
         * Resolves this type argument against a type.
         * If this is a concrete type, return unchanged.
         * If this is a type variable, determine the corresponding type parameter
         * of {@code method}.
         * @param method
         * @return resolved type
         */
        private TypeArgument resolve(JavaMethod method) {
            String name = type.getFullyQualifiedName();
            if (name.contains(".")) return this;
            int i = parameterIndex(name, method.getTypeParameters());
            if (i < 0) return this;
            JavaTypeVariable<?> v = method.getTypeParameters().get(i);
            JavaType bound;
            if (v.getBounds() == null) {
                bound = asClass("java.lang.Object");
            } else {
                bound = v.getBounds().get(0);
            }
            if (v.getName().equals("?")) {
                return new TypeArgument(bound);
            }
            return new TypeArgument(asClass(v.getName() + " extends " + bound.getGenericFullyQualifiedName()));
        }
        
        private int parameterIndex(String name, List<? extends JavaTypeVariable<?>> typeParams) {
            int i = 0;
            for (JavaTypeVariable<?> tv: typeParams) {
                String varName = tv.getName().trim();
                int iSpace = varName.indexOf(' ');
                if (iSpace > 0) varName = varName.substring(0, iSpace);
                if (name.equals(varName)) {
                    return i;
                }
                i++;
            }
            return -1;
        }
    }
    
    private static void expectNoString(Object o, String s) {
        throw new IllegalArgumentException(
                "Internal error, no string expected: " + o + " -> " + s);
    }
    
    public static class ConfigArgs<T> {
        
        private static final Class[] ARG_TYPES = {String.class, Map.class, Closure.class, Collection.class, Object.class};
        private static final int I_TEMPLATES = 3;
        private static final int I_TEMPLATE  = 4;
        private final String string;
        private final Map<String, Object> properties;
        private final Closure<?> closure;
        private final Collection<T> templates;
        private final T template;

        public ConfigArgs(Object[] args) {
            this(Object.class, args);
        }
        
        public ConfigArgs(Class<? super T> templateClass, Object[] args) {
            int[] i = scanIndices(templateClass, args);
            this.string = i[0] < 0 ? null : (String) args[i[0]];
            this.properties = i[1] < 0 ? null : (Map) args[i[1]];
            this.closure = i[2] < 0 ? null : (Closure) args[i[2]];
            this.templates = i[I_TEMPLATES] < 0 ? null : (Collection) args[i[I_TEMPLATES]];
            this.template = i[I_TEMPLATE] < 0 ? null : (T) args[i[I_TEMPLATE]];
        }
        
        private Class[] argTypes(Class<? super T> templateClass) {
            final Class[] argTypes = ARG_TYPES.clone();
            argTypes[I_TEMPLATE] = templateClass;
            for (int i = 0; i < I_TEMPLATE; i++) {
                if (argTypes[i].isAssignableFrom(templateClass)) {
                    argTypes[i] = Void.class;
                }
            }
            return argTypes;
        }
        
        private int[] scanIndices(Class<? super T> templateClass, Object[] args) {
            final Class[] argTypes = argTypes(templateClass);
            List<T> moreTemplates = null;
            int templatesIndex = -1;
//            List<String> moreStrings = null;
//            int stringsIndex = -1;
            int[] result = new int[argTypes.length];
            Arrays.fill(result, -1);
            for (int a = 0; a < args.length; a++) {
                for (int t = 0; t < argTypes.length; t++) {
                    if (argTypes[t].isInstance(args[a])) {
                        if (result[t] < 0) {
                            result[t] = a;
                        } else if (t == I_TEMPLATE) {
                            if (moreTemplates == null) {
                                moreTemplates = new ArrayList<>();
                                templatesIndex = a;
                            }
                            moreTemplates.add((T) args[a]);
                        } else {
                            throw new IllegalArgumentException(
                                    String.format("Duplicate %s parameter. (%d: %s; %d: %s)", 
                                            argTypes[t].getSimpleName(),
                                            result[t]+1, args[result[t]],
                                            a+1, args[a]));
                        }
                        break;
                    }
                }
            }
            if (moreTemplates != null) {
                if (result[I_TEMPLATES] > -1) {
                    moreTemplates.addAll((Collection<T>) args[result[I_TEMPLATES]]);
                    args[result[I_TEMPLATES]] = moreTemplates;
                } else {
                    args[templatesIndex] = moreTemplates;
                    result[I_TEMPLATES] = templatesIndex;
                }
            }
            return result;
        }

        public String getString() {
            return string;
        }

        public Map<String, Object> getProperties() {
            return properties;
        }

        public Closure<?> getClosure() {
            return closure;
        }

        public Collection<T> getTemplates() {
            if (template != null) {
                if (templates != null) {
                    List<T> l = new ArrayList<>(templates);
                    l.add(template);
                    return l;
                }
                return Arrays.asList(template);
            }
            return templates;
        }

        public boolean hasString() {
            return string != null;
        }
        
        public boolean hasProperties() {
            return properties != null;
        }
        
        public boolean hasClosure() {
            return closure != null;
        }
        
        public boolean hasTemplates() {
            return templates != null || template != null;
        }
        
        public boolean hasSingleTemplate() {
            return template != null && templates == null;
        }
        
        public <R> MultiConfigFluent<R> all(Supplier<R> supp, Function<? super T, R> func) {
            Collection<T> allTemplates = getTemplates();
            Stream<R> result;
            if (hasTemplates()) {
                result = allTemplates.stream().map(func);
            } else {
                result = Arrays.asList(supp.get()).stream();
            }
            return new MultiConfigFluent<>(result, this, allTemplates);
        }
    }
    
    public static class MultiConfigFluent<R> {
        
        private final ConfigArgs<?> args;
        private Stream<R> result;
        private final Collection<?> templates;

        public MultiConfigFluent(Stream<R> result, ConfigArgs<?> args, Collection<?> templates) {
            this.args = args;
            this.result = result;
            this.templates = templates;
        }

        protected <A> void apply(BiConsumer<R, A> f, A arg) {
            result = result.map((e) -> { f.accept(e, arg); return e; });
        }

        public List<R> getResult() {
            return result.collect(Collectors.toList());
        }
        
        public R getSingle() {
            List<R> list = getResult();
            if (list.isEmpty()) return null;
            return list.get(0);
        }
        
        public MultiConfigFluent<R> withString(BiConsumer<R, String> f) {
            if (args.hasString()) apply(f, args.getString());
            return this;
        }
        
        public MultiConfigFluent<R> withProperties(BiConsumer<R, Map<String, Object>> f) {
            if (args.hasProperties()) apply(f, args.getProperties());
            return this;
        }
        
        public <T> MultiConfigFluent<R> closureWithTemplate(TriConsumer<R, Closure<?>, T> f) {
            if (args.hasClosure()) {
                Iterator<T> it;
                if (templates == null) {
                    it = null;
                } else {
                    it = (Iterator) templates.iterator();
                }
                result = result.map((e) -> { 
                    T t = it == null ? (T) e : it.next();
                    f.accept(e, args.getClosure(), t); 
                    return e; 
                });
            }
            return this;
        }
        
        public <T> MultiConfigFluent<R> closureWithSelf(TriConsumer<R, Closure<?>, R> f) {
            if (args.hasClosure()) {
                result = result.map((e) -> { 
                    f.accept(e, args.getClosure(), e); 
                    return e; 
                });
            }
            return this;
        }
    }
    
    public static interface TriConsumer<A,B,C> {
        void accept(A a, B b, C c);
    }
    
    public static class PropertyFluent<T> {
        
        private Stream<T> values;
        private final Map<String, Object> properties;

        public PropertyFluent(T value, Map<String, Object> properties) {
            this(Arrays.asList(value).stream(), properties);
        }
        
        public PropertyFluent(Stream<T> values, Map<String, Object> properties) {
            this.values = values;
            this.properties = new HashMap<>(properties);
        }

        public <P> PropertyFluent<T> property(String p, BiConsumer<? super T, P> con) {
            Object value = properties.remove(p);
            if (value != null) {
                values = values.map((e) -> { con.accept(e, (P) value); return e;});
            }
            return this;
        }
        
        public void remaining(BiConsumer<? super T, ? super Map<String, Object>> con) {
            values.forEach((e) -> con.accept(e, properties));
        }
    }
}
