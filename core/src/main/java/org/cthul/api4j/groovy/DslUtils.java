package org.cthul.api4j.groovy;

import groovy.lang.Closure;
import groovy.lang.MetaClass;
import groovy.lang.MissingMethodException;
import java.util.List;

public class DslUtils {
    
    public static void unwrapArgs(Object[] args) {
        if (args == null) return;
        for (int i = 0; i < args.length; i++) {
            Object o = args[i];
            if (o instanceof DslObject) {
                args[i] = ((DslObject<?>) o).__object();
            }
        }
    }
    
    public static <T> T configure(Object o, Closure<T> c) {
        c.setDelegate(o);
        return c.call();
    }
}
