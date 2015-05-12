package org.cthul.api4j.api1;

import com.thoughtworks.qdox.model.JavaClass;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import static org.cthul.api4j.api1.GeneralTools.instance;
import org.cthul.strings.Pluralizer;
import org.cthul.strings.plural.PluralizerRegistry;

/**
 * Makes some methods available to Groovy scripts.
 */
public class GeneralImports {
    
    private static Locale defaultLocale;
    private static Pluralizer defaultPluralizer;

    public static synchronized void setDefaultLocale(Locale defaultLocale) {
        GeneralImports.defaultLocale = defaultLocale;
        defaultPluralizer = PluralizerRegistry.INSTANCE.find(defaultLocale);
    }

    public static Locale getDefaultLocale() {
        return defaultLocale;
    }
    
    static {
        setDefaultLocale(Locale.getDefault());
    }
    
    public static String singularOf(String word) {
        return defaultPluralizer.singularOf(word);
    }
    
    public static String pluralOf(String word) {
        return defaultPluralizer.pluralOf(word);
    }
    
    public static List<JavaClass> classes(Collection<String> patterns) {
        return instance().classes(patterns);
    }
    
    public static List<JavaClass> classes(String... patterns) {
        return instance().classes(patterns);
    }
    
    public static JavaClass asClass(String pattern) {
        return instance().asClass(pattern);
    }
    
    public static List<JavaClass> types(Collection<String> patterns) {
        return instance().types(patterns);
    }
    
    public static List<JavaClass> types(String... patterns) {
        return instance().types(patterns);
    }
    
    public static JavaClass asType(String pattern) {
        return instance().asType(pattern);
    }
}
