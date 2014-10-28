package org.cthul.api4j.api1;

import java.util.Locale;
import org.cthul.strings.Pluralizer;
import org.cthul.strings.plural.PluralizerRegistry;

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
    
}
