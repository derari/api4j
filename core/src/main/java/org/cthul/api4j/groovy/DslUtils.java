package org.cthul.api4j.groovy;

import groovy.lang.Closure;
import java.util.*;

public class DslUtils {
    
    public static void unwrapAll(Object[] args) {
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
        if (o instanceof List) {
            ListIterator<Object> it = ((List) o).listIterator();
            while (it.hasNext()) {
                Object e = it.next();
                Object e2 = unwrap(e);
                if (e != e2) it.set(e2);
            }
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
        if (o instanceof ClosureConfigurable) {
            return ((ClosureConfigurable) o).configure(c);
        }
        if (o instanceof DslConfigurable) {
            return ((DslConfigurable) o).configure(dsl, c);
        }
        return runClosureOn(dsl, o, c);
    }
    
    public static <T> T runClosureOn(GroovyDsl dsl, Object o, Closure<T> c) {
        c.setDelegate(dsl.wrap(o));
        c.setResolveStrategy(Closure.DELEGATE_FIRST);
        return c.call();
    }  

    public static <T> T runClosureOn(GroovyDsl dsl, Object o, Closure<T> c, Object arg) {
        c.setDelegate(dsl.wrap(o));
        c.setResolveStrategy(Closure.DELEGATE_FIRST);
        return c.call(dsl.wrap(arg));
    }  
}