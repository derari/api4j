package org.cthul.api4j.groovy;

import groovy.lang.Closure;
import groovy.lang.MetaClass;
import groovy.lang.MissingMethodException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.cthul.api4j.groovy.DslUtils.unwrapArgs;
import org.cthul.objects.instance.InstanceMap;
import org.cthul.objects.reflection.Signatures;

public class GroovyDsl {
    
    private final Set<Class<?>> extensions = new HashSet<>();
    private final InstanceMap instances = new InstanceMap();
    private Map<String, Method[]> extensionMethods = new HashMap<>();
    private Map<Method, Class<?>> declaringClasses = new HashMap<>();

    public void addGlobal(Object g) {
        instances.put(g.getClass(), g);
    }
    
    public void addGlobals(Object... globals) {
        for (Object g: globals) {
            addGlobal(g);
        }
    }
    
    public Set<Class<?>> getExtensions() {
        return extensions;
    }
    
    public <T> T getExtension(Class<T> clazz) {
        return instances.getOrCreate(clazz);
    }
    
    private Method[] getExtensionMethods(String name) {
        Method[] result = extensionMethods.get(name);
        if (result == null) {
            List<Method> list = new ArrayList<>();
            for (Class<?> c: extensions) {
                Method[] methods =  Signatures.collectMethods(c, name);
                for (Method m: methods) {
                    declaringClasses.put(m, c);
                }
                list.addAll(Arrays.asList(methods));
            }
            result = list.toArray(new Method[list.size()]);
            extensionMethods.put(name, result);
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    public Object wrap(Object a) {
        if (a == null) {
            return null;
        }
        if (a instanceof DslObject) {
            return a;
        }
        if (a instanceof List) {
            return (DslList) new DslList(this, (List) a);
        }
//        if (a.getClass().getPackage().getName().startsWith("java.lang")) {
//            return a;
//        }
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
                result = invokeExtensionsNoWrap(o, mc, name, args);
            } catch (MissingMethodException e2) {
                throw e;
            }
        }
        return result;
    }
    
    public Object invokeExtensions(Object o, MetaClass mc, String name, Object[] args) {
        unwrapArgs(args);
        return wrap(invokeExtensionsNoWrap(o, mc, name, args));
    }
    
    public Object getExtensionsProperty(Object self, MetaClass mc, String name) {
        return invokeExtensionsNoWrap(self, mc, name, new Object[0]);
    }
    
    public Object invokeExtensionsNoWrap(Object self, MetaClass mc, String name, Object[] args) {
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
            return tryInvokeConfigure(self, mc, name, args);
        }
        try {
            Object ext = null;
            if ((m.getModifiers() & Modifier.STATIC) == 0) {
                ext = getExtension(declaringClasses.get(m));
            }
            return Signatures.invoke(ext, m, a2);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(m.toString(), e);
        }
    }   
    
    private Object tryInvokeConfigure(Object self, MetaClass mc, String name, Object[] args) {
        if (args != null 
                && args.length > 0 
                && args[args.length-1] instanceof Closure) {
            Closure<?> c = (Closure) args[args.length-1];
            args = Arrays.copyOf(args, args.length-1);
            Object o = invokeWithExtensionsNoWrap(self, mc, name, args);
            return DslUtils.configure(this, o, c);
        }
        throw new MissingMethodException(name, self.getClass(), args);
    }
}
