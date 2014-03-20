package org.cthul.api4j.groovy;

import groovy.lang.GroovyObjectSupport;
import groovy.lang.MissingMethodException;

public class DslNative extends GroovyObjectSupport implements DslObject<DslNative> {

    protected GroovyDsl dsl() {
        return null;
    }
    
    protected Object methodMissing(String name, Object arg) {
        Object[] args = (Object[]) arg;
        GroovyDsl dsl = dsl();
        if (dsl != null) {
            return dsl.invokeExtensions(this, getMetaClass(), name, args);
        }
        throw new MissingMethodException(name, getClass(), args);
    }
    
    @Override
    public DslNative __object() {
        return this;
    }
}
