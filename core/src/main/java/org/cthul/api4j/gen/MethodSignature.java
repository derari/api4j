package org.cthul.api4j.gen;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.model.impl.AbstractBaseMethod;
import com.thoughtworks.qdox.model.impl.DefaultJavaParameter;
import groovy.lang.GroovyObjectSupport;

public class MethodSignature extends GroovyObjectSupport {
    
    JavaProjectBuilder qdox;
    AbstractBaseMethod m;

    public MethodSignature(JavaProjectBuilder qdox, AbstractBaseMethod m) {
        this.qdox = qdox;
        this.m = m;
    }

    public void leftShift(String string) {
        for (String s: string.split(",")) {
            s = s.trim();
            if (!s.isEmpty()) {
                String[] def = s.split(" ");
                JavaClass jc = qdox.getClassByName(def[0]);
                JavaParameter jp = new DefaultJavaParameter(jc, def[1]);
                m.getParameters().add(jp);
            }
        }
    }
    
}
