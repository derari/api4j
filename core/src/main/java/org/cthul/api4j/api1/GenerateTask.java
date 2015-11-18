package org.cthul.api4j.api1;

import groovy.lang.Closure;

/**
 * Helper class to allow `generatedClass "Foo" { ... }` in Groovy scripts.
 */
public class GenerateTask {
    
    private final String name;
    private final Closure<?> closure;

    public GenerateTask(String name, Closure<?> closure) {
        this.name = name;
        this.closure = closure;
    }

    public String getName() {
        return name;
    }

    public Closure<?> getClosure() {
        return closure;
    }
}
