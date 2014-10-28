package org.cthul.api4j.groovy;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MetaClass;
import groovy.util.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static jdk.nashorn.api.scripting.ScriptUtils.wrap;
import org.codehaus.groovy.runtime.GroovyCategorySupport;
import org.codehaus.groovy.runtime.InvokerHelper;

public class DslUtils {
    
    public static <T> T configure(Object o, Closure<T> c) {
        if (o instanceof ClosureConfigurable) {
            return ((ClosureConfigurable) o).configure(c);
        }
        return runClosureOn(o, c);
    }
    
    public static <T> T configureWith(Object o, Closure<T> c, Object arg) {
        return runClosureOn(o, c, arg);
    }
    
    public static <T> T runClosureOn(Object o, Closure<T> c) {
        return runClosureOn(o, c, o);
    }
    
    public static <T> T runClosureOn(Object o, Closure<T> c, Object arg) {
        c.setDelegate(o);
        c.setResolveStrategy(Closure.DELEGATE_FIRST);
        return c.call(arg);
    }
    
    public static <T> T runClosureOn(Class category, Object o, Closure<T> c) {
        return runClosureOn(o, c, category);
    }
    
    public static <T> T runClosureOn(Object o, Closure<T> c, Class... categories) {
        return runClosureOn(Arrays.asList(categories), o, c);
    }
    
    public static <T> T runClosureOn(List<Class> categories, Object o, Closure<T> c) {
        c.setDelegate(o);
        c.setResolveStrategy(Closure.DELEGATE_FIRST);
        return GroovyCategorySupport.use(categories, c);
    }

    public static <T> T tryClosureOn(AutoCloseable o, Closure<T> c) {
        return tryClosureOn(Collections.emptyList(), o, c);
    }
    
    public static <T> T tryClosureOn(Class category, AutoCloseable o, Closure<T> c) {
        return tryClosureOn(o, c, category);
    }
    
    public static <T> T tryClosureOn(AutoCloseable o, Closure<T> c, Class... categories) {
        return tryClosureOn(Arrays.asList(categories), o, c);
    }
    
    public static <T> T tryClosureOn(List<Class> categories, AutoCloseable o, Closure<T> c) {
        try {
            try (AutoCloseable ac = o) {
                c.setDelegate(o);
                c.setResolveStrategy(Closure.DELEGATE_FIRST);
                return GroovyCategorySupport.use(categories, c);
            }
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException(e);
        }
    }
    
    public static GroovyObject wrapAsGroovy(Object o) {
        if (o instanceof GroovyObject) {
            return (GroovyObject) o;
        }
        return new GroovyWrapper(o);
    }
    
    public static <T> T unwrap(Object o) {
        if (o instanceof GroovyWrapper) {
            return (T) ((GroovyWrapper) o).__object();
        }
        return (T) o;
    }
    
    public static void unwrapAll(Object[] objects) {
        for (int i = 0; i < objects.length; i++) {
            objects[i] = unwrap(objects[i]);
        }
    }
    
    public static void applyProperties(Object o, Map<String, Object> properties) {
        GroovyObject go = wrapAsGroovy(o);
        properties.entrySet().stream()
                .forEach((e) -> go.setProperty(e.getKey(), e.getValue()));
    }
    
    public static class GroovyWrapper extends GroovyObjectSupport {

        private final Object o;
        private final MetaClass mc;

        public GroovyWrapper(Object o) {
            this.o = o;
            mc = InvokerHelper.getMetaClass(o);
        }

        public Object __object() {
            return o;
        }

        protected Object methodMissing(String name, Object a) {
            Object[] args = (Object[]) a;
            unwrapAll(args);
            return mc.invokeMethod(o, name, args);
        }
    
        protected Object propertyMissing(String name) {
            return mc.getProperty(o, name);
        }

        protected Object propertyMissing(String name, Object value) {
            mc.setProperty(o, name, unwrap(value));
            return value;
        }

        @Override
        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        public boolean equals(Object obj) {
            return o.equals(unwrap(obj));
        }

        @Override
        public int hashCode() {
            return o.hashCode();
        }

        @Override
        public String toString() {
            return o.toString();
        }
    }
}