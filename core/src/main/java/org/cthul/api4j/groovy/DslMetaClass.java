package org.cthul.api4j.groovy;

import groovy.lang.DelegatingMetaClass;
import groovy.lang.MetaClass;
import groovy.lang.MetaClassRegistry;
import groovy.lang.MetaMethod;
import groovy.lang.MissingMethodException;

public class DslMetaClass extends DelegatingMetaClass {
    
    public static GroovyDsl DSL;

    public DslMetaClass(MetaClass delegate) {
        super(delegate);
    }

    @Override
    public Object invokeMethod(String name, Object args) {
        return super.invokeMethod(name, args);
    }

    @Override
    public Object invokeMissingMethod(Object instance, String methodName, Object[] arguments) {
        return super.invokeMissingMethod(instance, methodName, arguments);
    }

    @Override
    public Object invokeMethod(Class sender, Object receiver, String methodName, Object[] arguments, boolean isCallToSuper, boolean fromInsideClass) {
        return super.invokeMethod(sender, receiver, methodName, arguments, isCallToSuper, fromInsideClass);
    }

    @Override
    public Object invokeMethod(Object object, String methodName, Object[] arguments) {
        return super.invokeMethod(object, methodName, arguments);
    }

    @Override
    public Object invokeMethod(Object object, String methodName, Object arguments) {
        return super.invokeMethod(object, methodName, arguments);
    }
    
    
    
    public static MetaClassRegistry.MetaClassCreationHandle CREATION_HANDLE = new MetaClassRegistry.MetaClassCreationHandle() {
        @Override
        protected MetaClass createNormalMetaClass(Class theClass, MetaClassRegistry registry) {
            MetaClass mc =  super.createNormalMetaClass(theClass, registry);
            return new DslMetaClass(mc);
        }
    };
}
