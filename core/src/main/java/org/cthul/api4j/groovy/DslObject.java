package org.cthul.api4j.groovy;

import groovy.lang.GroovyObject;

public interface DslObject<T> extends GroovyObject {
    
    T __object();
}
