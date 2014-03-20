package org.cthul.api4j.groovy;

import groovy.lang.GroovyObject;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MetaClass;
import groovy.lang.MissingMethodException;
import org.codehaus.groovy.runtime.InvokerHelper;

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
    
    protected Object methodMissing(String name, Object a) {
        Object result;
        Object[] args = (Object[]) a;
        DslUtils.unwrapArgs(args);
        try {
            result = mc.invokeMethod(o, name, args);
        } catch (MissingMethodException e) {
            try {
                result = dsl.invokeExtensionsNoWrap(o, name, args);
            } catch (MissingMethodException e2) {
                throw e;
            } 
        }
        return dsl.wrap(result);
    }
}
