package org.cthul.api4j.api1;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.impl.DefaultJavaType;
import groovy.lang.GroovyObject;
import groovy.lang.MissingMethodException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.cthul.api4j.Api4JConfiguration;
import org.cthul.api4j.groovy.DslUtils;
import org.cthul.objects.instance.Inject;
import org.cthul.strings.JavaNames;
import org.cthul.strings.Strings;

public class GeneralTools {
    
    public static <T> Collection<T> all(Collection<? extends Collection<T>> collections) {
        final Collection<T> result = new ArrayList<>();
        collections.stream().forEach(c -> {result.addAll(c);});
        return result;
    }
    
    public static Collection<?> all(Collection<?> collection, String getter) {
        String getter2 = "get" + JavaNames.firstToUpper(getter);
        final Collection<Object> result = new ArrayList<>();
        collection.stream().forEach(o -> {
            GroovyObject go = DslUtils.wrapAsGroovy(o);
            Object value;
            try {
                value = go.invokeMethod(getter, null);
            } catch (MissingMethodException mme1) {
                try {
                    value = go.invokeMethod(getter2, null);
                } catch (MissingMethodException mme2) {
                    throw mme1;
                }
            }
            if (value instanceof Collection) {
                result.addAll((Collection) value);
            } else {
                result.add(value);
            }
        });
        return result;
    }
    
    public static List<JavaClass> classes(Object __, Collection<String> patterns) {
        return instance().classes(patterns);
    }
    
    public static List<JavaClass> classes(Object __, String... patterns) {
        return instance().classes(patterns);
    }
    
    public static JavaClass asClass(Object __, String pattern) {
        return instance().asClass(pattern);
    }
    
    public static List<JavaClass> types(Object __, Collection<String> patterns) {
        return instance().types(patterns);
    }
    
    public static List<JavaClass> types(Object __, String... patterns) {
        return instance().types(patterns);
    }
    
    public static JavaClass asType(Object __, String pattern) {
        return instance().asType(pattern);
    }
    
    public static String mod(String s, Collection<?> args) {
        return format(s, args.toArray());
    }
    
    public static String mod(String s, Object... args) {
        return format(s, args);
    }
    
    public static String format(String s, Object... args) {
        return Strings.format(s, args);
    }
    
    public static GeneralTools instance() {
        return Globals.get(GeneralTools.class);
    }
    
    private final Api4JConfiguration cfg;

    @Inject
    public GeneralTools(Api4JConfiguration configuration) {
        this.cfg = configuration;
    }
    
    public List<JavaClass> classes(Collection<String> patterns) {
        return patterns.stream().map(this::asClass).collect(Collectors.toList());
    }
    
    public List<JavaClass> classes(String... patterns) {
        return classes(Arrays.asList(patterns));
    }
    
    public List<JavaClass> types(Collection<String> patterns) {
        return patterns.stream().map(this::asType).collect(Collectors.toList());
    }
    
    public List<JavaClass> types(String... patterns) {
        return types(Arrays.asList(patterns));
    }
    
    public JavaClass asClass(String pattern) {
        int i = pattern.indexOf('<');
        if (i > 0) pattern = pattern.substring(0, i);
        return asType(pattern);
    }
    
    public JavaClass asType(String pattern) {
        return cfg.getQdox().getClassByName(pattern);
    }
}
