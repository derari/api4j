package org.cthul.api4j.groovy;

import groovy.lang.Closure;

public interface ClosureConfigurable {
    
    default <V> V configure(Closure<V> closure) {
        return DslUtils.runClosureOn(this, closure);
    }
}
