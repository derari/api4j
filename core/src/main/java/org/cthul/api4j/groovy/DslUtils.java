package org.cthul.api4j.groovy;

import groovy.lang.Closure;
import java.io.IOException;
import java.util.Map;
import org.cthul.api4j.gen.SelfGenerating;

public class DslUtils {
    
    public static void unwrapArgs(Object[] args) {
        if (args == null) return;
        for (int i = 0; i < args.length; i++) {
            Object o = args[i];
            Object u = unwrap(o);
            if (u != o) args[i] = u;
        }
    }
    
    public static Object unwrap(Object o) {
        if (o == null) return null;
        if (o instanceof DslObject) {
            o = ((DslObject<?>) o).__object();
        }
        if (o instanceof Map) {
            for (Map.Entry<Object, Object> e: ((Map<Object, Object>) o).entrySet()) {
                Object v = e.getValue();
                Object v2 = unwrap(v);
                if (v != v2) e.setValue(v2);
            }
        }
        return o;
    }
    
    public static <T> T configure(GroovyDsl dsl, Object o, Closure<T> c) {
        c.setDelegate(dsl.wrap(o));
        c.setResolveStrategy(Closure.DELEGATE_FIRST);
        return c.call();
    }
    
    public static void uncheckedWriteTo(SelfGenerating sg, Appendable a) {
        try {
            sg.writeTo(a);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
