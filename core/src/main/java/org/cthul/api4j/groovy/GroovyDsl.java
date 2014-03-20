package org.cthul.api4j.groovy;

import groovy.lang.MetaClass;
import groovy.lang.MissingMethodException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.cthul.api4j.groovy.DslUtils.unwrapArgs;
import org.cthul.objects.reflection.Signatures;

public class GroovyDsl {
    
    private final Set<Class<?>> extensions = new HashSet<>();
    private Map<String, Method[]> extensionMethods = new HashMap<>();

    public Set<Class<?>> getExtensions() {
        return extensions;
    }
    
    private Method[] getExtensionMethods(String name) {
        Method[] result = extensionMethods.get(name);
        if (result == null) {
            List<Method> list = new ArrayList<>();
            for (Class<?> c: extensions) {
                Method[] m =  Signatures.collectMethods(c, name, Signatures.STATIC | Signatures.PUBLIC, Signatures.NONE);
                list.addAll(Arrays.asList(m));
            }
            result = list.toArray(new Method[list.size()]);
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    public <T> DslObject<T> wrap(T a) {
        if (a instanceof DslObject) {
            return (DslObject<T>) a;
        }
        if (a instanceof List) {
            return new DslList<>(this, (List) a);
        }
        return new DslWrapper<>(this, a);
    }
    
    public Object invokeWithExtensions(Object o, MetaClass mc, String name, Object a) {
        Object[] args = (Object[]) a;
        DslUtils.unwrapArgs(args);
        Object result = invokeWithExtensionsNoWrap(o, mc, name, args);
        return wrap(result);
    }
    
    public Object invokeWithExtensionsNoWrap(Object o, MetaClass mc, String name, Object[] args) {
        Object result;
        try {
            result = mc.invokeMethod(o, name, args);
        } catch (MissingMethodException e) {
            try {
                result = invokeExtensionsNoWrap(o, name, args);
            } catch (MissingMethodException e2) {
                throw e;
            }
        }
        return result;
    }
    
    public Object invokeExtensions(Object o, MetaClass mc, String name, Object[] args) {
        unwrapArgs(args);
        return wrap(invokeExtensionsNoWrap(o, name, args));
    }
    
    public Object invokeExtensionsNoWrap(Object self, String name, Object[] args) {
        Object[] a2 = new Object[args.length+1];
        a2[0] = self;
        System.arraycopy(args, 0, a2, 1, args.length);
        Method[] methods = getExtensionMethods(name);
        Method m = Signatures.bestMethod(methods, a2);
        if (m == null) {
            a2 = new Object[]{self, name, args};
            methods = getExtensionMethods("methodMissing");
            m = Signatures.bestMethod(methods, a2);
        }
        if (m == null) {
            throw new MissingMethodException(name, self.getClass(), args);
        }
        try {
            return Signatures.invoke(null, m, a2);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }    
}
