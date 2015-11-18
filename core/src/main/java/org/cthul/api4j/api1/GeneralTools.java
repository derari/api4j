package org.cthul.api4j.api1;

import com.thoughtworks.qdox.model.JavaClass;
import groovy.lang.GroovyObject;
import groovy.lang.MissingMethodException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.cthul.api4j.Api4JConfiguration;
import org.cthul.api4j.api.PatternSearcher;
import org.cthul.api4j.groovy.DslUtils;
import org.cthul.objects.instance.Inject;
import org.cthul.strings.JavaNames;
import org.cthul.strings.Strings;

/**
 * Some utility methods for Groovy scripts.
 * Also implements static methods of GeneralImports that require state.
 */
public class GeneralTools {
    
    /**
     * Flattens the collection
     * @param <T>
     * @param collections
     * @return collection
     */
    public static <T> Collection<T> all(Collection<? extends Collection<T>> collections) {
        final Collection<T> result = new ArrayList<>();
        collections.stream().forEach(c -> {result.addAll(c);});
        return result;
    }
    
    /**
     * Collects properties of each item and returns a flattened result.
     * @param collection
     * @param getter
     * @return collection
     */
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
    
    public static String mod(String s, Collection<?> args) {
        return format(s, args.toArray());
    }
    
    public static String mod(String s, Object... args) {
        return format(s, args);
    }
    
    public static String format(String s, Object... args) {
        return Strings.format(s, args);
    }
    
    public static List<String> parametersToArgs(List<String> list) {
        List<String> result = new ArrayList<>(list.size());
        for (String p: list) {
            result.add(parameterToArg(p));
        }
        return result;
    }
    
    public static String parameterToArg(String s) {
        int iSpace = s.indexOf(' ');
        if (iSpace < 0) return s;
        return s.substring(0, iSpace);
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
        return types(patterns.stream().map(this::typeToClass).collect(Collectors.toList()));
    }
    
    public List<JavaClass> classes(String... patterns) {
        return classes(Arrays.asList(patterns));
    }
    
    public List<JavaClass> types(Collection<String> patterns) {
        return types(patterns.toArray(new String[patterns.size()]));
    }
    
    public List<JavaClass> types(String... patterns) {
        List<JavaClass> result = new ArrayList<>();
        List<String> searchPatterns = null;
        for (String p: patterns) {
            if (PatternSearcher.isPattern(p)) {
                if (searchPatterns == null) searchPatterns = new ArrayList<>();
                searchPatterns.add(p);
            } else {
                JavaClass c = cfg.getQdox().getClassByName(p);
                if (c != null) result.add(c);
            }
        }
        if (searchPatterns != null) {
            PatternSearcher searcher = PatternSearcher.forPatterns(searchPatterns);
            result.addAll(cfg.getQdox().search(searcher));
        }
        return result;
    }
    
    private String typeToClass(String pattern) {
        int i = pattern.indexOf('<');
        if (i > 0) pattern = pattern.substring(0, i);
        return pattern;
    }
    
    public JavaClass asClass(String pattern) {
        return asType(typeToClass(pattern));
    }
    
    public JavaClass asType(String pattern) {
        List<JavaClass> result = types(pattern);
        if (result.isEmpty()) return null;
        if (result.size() > 1) {
            throw new IllegalArgumentException("Ambiguous result for  " + pattern + 
                    ":\n" + result);
        }
        return result.get(0);
    }
}
