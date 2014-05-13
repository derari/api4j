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
import static org.cthul.api4j.groovy.DslUtils.unwrapAll;
import org.cthul.objects.Boxing;
import org.cthul.objects.instance.InstanceMap;
import org.cthul.objects.reflection.Signatures;

public class GroovyDsl {
    
    private final Set<Class<?>> extensions = new HashSet<>();
    private final InstanceMap instances = new InstanceMap();
    private final Map<String, Method[]> extensionMethods = new HashMap<>();
    private final Map<String, Class<?>[][]> extensionSignatures = new HashMap<>();
    private final Map<String, boolean[]> extensionVarArgs = new HashMap<>();
    
    private final Map<Method, Class<?>> declaringClasses = new HashMap<>();
    private final Set<Method> globalExtensions = new HashSet<>();

    public synchronized void addGlobal(Object g) {
        instances.put(g.getClass(), g);
    }
    
    public synchronized void addGlobals(Object... globals) {
        for (Object g: globals) {
            addGlobal(g);
        }
    }
    
    public Set<Class<?>> getExtensions() {
        return extensions;
    }
    
    public synchronized <T> T getExtension(Class<T> clazz) {
        if (extensions.add(clazz)) {
            extensionMethods.clear();
            extensionSignatures.clear();
            extensionVarArgs.clear();
        }
        return instances.getOrCreate(clazz);
    }
    
    private synchronized Method[] getExtensionMethods(String name) {
        Class[][] SIGNATURES_ARRAY = {};
        Method[] result = extensionMethods.get(name);
        if (result == null) {
            List<Method> list = new ArrayList<>();
            List<Class<?>[]> signatures = new ArrayList<>();
            List<Boolean> varArgs = new ArrayList<>();
            for (Class<?> c: extensions) {
                Method[] methods =  Signatures.collectMethods(c, name);
                for (Method m: methods) {
                    analyzeExtensionMethod(c, m, signatures, varArgs);
                }
                list.addAll(Arrays.asList(methods));
            }
            result = list.toArray(new Method[list.size()]);
            extensionMethods.put(name, result);
            extensionSignatures.put(name, signatures.toArray(SIGNATURES_ARRAY));
            extensionVarArgs.put(name, Boxing.unboxBooleans(varArgs));
        }
        return result;
    }
    
    private synchronized Class<?>[][] getExtensionSignatures(String name) {
        Class<?>[][] result = extensionSignatures.get(name);
        if (result == null) {
            getExtensionMethods(name);
            result = extensionSignatures.get(name);
        }
        return result;
    }
    
    private synchronized boolean[] getExtensionVarArgs(String name) {
        boolean[] result = extensionVarArgs.get(name);
        if (result == null) {
            getExtensionMethods(name);
            result = extensionVarArgs.get(name);
        }
        return result;
    }
    
    private void analyzeExtensionMethod(Class<?> declaringClass, Method m, List<Class<?>[]> signatures, List<Boolean> varArgs) {
        if (declaringClasses.put(m, declaringClass) != null) {
            return;
        }
        Class<?>[] params = m.getParameterTypes();
        GlobalExtension ge = m.getAnnotation(GlobalExtension.class);
        if (ge == null) ge = declaringClass.getAnnotation(GlobalExtension.class);
        if (ge != null) {
            Class<?>[] newParams = new Class<?>[params.length + 1];
            newParams[0] = ge.value();
            System.arraycopy(params, 0, newParams, 1, params.length);
            params = newParams;
            globalExtensions.add(m);
        }
        signatures.add(params);
        varArgs.add(m.isVarArgs());
    }
    
    private synchronized boolean isGlobalExtension(Method m) {
        return globalExtensions.contains(m);
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
    
    /**
     * Invokes a method or a dsl extension.
     * @param o
     * @param mc
     * @param name
     * @param a
     * @return 
     */
    public Object invokeWithExtensions(Object o, MetaClass mc, String name, Object a) {
        Object[] args = (Object[]) a;
        DslUtils.unwrapAll(args);
        Object result = invokeWithExtensionsNoUnwrap(o, mc, name, args);
        return wrap(result);
    }
    
    /**
     * Invokes a method or a dsl extension.
     * Does not unwrap arguments.
     * @param o
     * @param mc
     * @param name
     * @param args
     * @return 
     */
    public Object invokeWithExtensionsNoUnwrap(Object o, MetaClass mc, String name, Object[] args) {
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
    
    /**
     * Invokes a dsl extension.
     * @param o
     * @param mc
     * @param name
     * @param args
     * @return 
     */
    public Object invokeExtensions(Object o, MetaClass mc, String name, Object[] args) {
        unwrapAll(args);
        return wrap(invokeExtensionsNoWrap(o, mc, name, args));
    }
    
    /**
     * Invokes a dsl extension.
     * @param self
     * @param mc
     * @param name
     * @return 
     */
    public Object getExtensionsProperty(Object self, MetaClass mc, String name) {
        return invokeExtensionsNoWrap(self, mc, name, new Object[0]);
    }
    
    /**
     * Invokes a dsl extension.
     * Does not unwrap arguments.
     * @param self
     * @param mc
     * @param name
     * @param args
     * @return 
     */
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
    
    private synchronized Method findExtensionMethod(String name, Object[] args) {
        Method[] methods = getExtensionMethods(name);
        Class<?>[][] signatures = getExtensionSignatures(name);
        int i = Signatures.bestm
    }
    
    private Object tryInvokeConfigure(Object self, MetaClass mc, String name, Object[] args) {
        if (args != null 
                && args.length > 0 
                && args[args.length-1] instanceof Closure) {
            Closure<?> c = (Closure) args[args.length-1];
            args = Arrays.copyOf(args, args.length-1);
            Object o = invokeWithExtensionsNoUnwrap(self, mc, name, args);
            return DslUtils.configure(this, o, c);
        }
        throw new MissingMethodException(name, self.getClass(), args);
    }
}
