package org.cthul.api4j.groovy;

import groovy.lang.Closure;

public interface ClosureConfigurable {
    
    <V> V configure(Closure<V> closure);
}
