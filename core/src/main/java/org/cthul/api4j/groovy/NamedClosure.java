package org.cthul.api4j.groovy;

import groovy.lang.Closure;

public class NamedClosure<V> {
    
    private final String name;
    private final Closure<V> closure;

    public NamedClosure(String name, Closure<V> closure) {
        this.name = name;
        this.closure = closure;
    }

    public String getName() {
        return name;
    }

    public Closure<V> getClosure() {
        return closure;
    }
}
