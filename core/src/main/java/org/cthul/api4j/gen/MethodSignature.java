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

    public void leftShift(String s) {
        int n = 0;
        s = s.trim();
        while (n < s.length()) {
            int typeEnd = nextParameterType(s, n);
            int nameEnd = s.indexOf(',', typeEnd);
            if (nameEnd < 0) nameEnd = s.length();
            JavaClass jc = qdox.getClassByName(s.substring(n, typeEnd));
            JavaParameter jp = new DefaultJavaParameter(jc, s.substring(typeEnd+1, nameEnd));
            m.getParameters().add(jp);
            n = nameEnd+1;
        }
    }
    
    private int nextParameterType(String s, int n) {
        int iSpace = s.indexOf(' ');
        int iOpen = s.indexOf('<');
        if (iOpen > -1 && iOpen < iSpace) {
            int iClose = endOfGenericArg(s, iOpen);
            iSpace = s.indexOf(' ', iClose);
        }
        if (iSpace < 0) return s.length();
        return iSpace;
    }

    private int endOfGenericArg(String s, int iOpen) {
        int iClose = s.indexOf('>', iOpen);
        iOpen = s.indexOf('<', iOpen+1);
        while (iOpen > -1 && iOpen < iClose) {
            int end = endOfGenericArg(s, iOpen)+1;
            iClose = s.indexOf('>', end);
            iOpen = s.indexOf('<', end);
        }
        return iClose;
    }
}
