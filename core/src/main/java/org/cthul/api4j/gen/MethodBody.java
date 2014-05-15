package org.cthul.api4j.gen;

import com.thoughtworks.qdox.model.impl.AbstractBaseMethod;
import groovy.lang.GroovyObjectSupport;

public class MethodBody extends GroovyObjectSupport {
    
    private AbstractBaseMethod m;

    public MethodBody(AbstractBaseMethod m) {
        this.m = m;
    }
    
    public void leftShift(Object o) {
        String s = m.getSourceCode();
        if (s == null) s = "";
        m.setSourceCode(s + o);
    }
}
