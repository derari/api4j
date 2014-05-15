package org.cthul.api4j.groovy;

import groovy.lang.Closure;

public interface DslConfigurable {
    
    <V> V configure(GroovyDsl dsl, Closure<V> closure);
}
