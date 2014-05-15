package org.cthul.api4j.groovy;

import groovy.lang.*;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.typehandling.GroovyCastException;

public class DslWrapper<T> extends GroovyObjectSupport implements DslObject<T> {

    private final GroovyDsl dsl;
    private final T o;
    private final GroovyObject go;
    private final MetaClass mc;

    public DslWrapper(GroovyDsl dsl, T o) {
        this.dsl = dsl;
        this.o = o;
        if (o instanceof GroovyObject) {
            go = (GroovyObject) o;
            mc = go.getMetaClass();
        } else {
            go = null;
            mc = InvokerHelper.getMetaClass(o);
        }
    }

    @Override
    public T __object() {
        return o;
    }
    
    public boolean asBoolean() {
        if (o instanceof Boolean) {
            return ((Boolean) o).booleanValue();
        }
        Object b = methodMissing("asBoolean", null);
        return (boolean) DslUtils.unwrap(b);
    }
    
    protected Object methodMissing(String name, Object a) {
        Object result;
        Object[] args = (Object[]) a;
        DslUtils.unwrapAll(args);
        try {
            result = mc.invokeMethod(o, name, args);
        } catch (MissingMethodException e) {
            try {
                result = dsl.invokeExtensionsNoWrap(o, mc, name, args);
            } catch (MissingMethodException e2) {
                throw e;
            } 
        }
        return dsl.wrap(result);
    }
    
    protected Object propertyMissing(String name) {
        Object result;
        try {
            result = mc.getProperty(o, name);
        } catch (MissingPropertyException e) {
            try {
                result = dsl.getExtensionsProperty(o, mc, name);
            } catch (MissingMethodException e2) {
                throw e;
            } 
        }
        return dsl.wrap(result);
    }
    
    protected Object propertyMissing(String name, Object value) {
        Object unwrap = DslUtils.unwrap(value);
        try {
            mc.setProperty(o, name, unwrap);
        } catch (MissingPropertyException | GroovyCastException e) {
            try {
                dsl.setExtensionsProperty(o, mc, name, unwrap);
            } catch (MissingMethodException e2) {
                throw e;
            }
        }
        return value;
    }

    @Override
    public MetaClass getMetaClass() {
        return super.getMetaClass();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        obj = DslUtils.unwrap(obj);
        return o.equals(obj);
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
