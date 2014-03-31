package org.cthul.api4j.gen;

import org.cthul.api4j.groovy.GroovyDsl;

public class SubGenerator extends GeneratorBase implements AutoCloseable {
    
    private final StringBuilder target;

    public SubGenerator(GroovyDsl dsl, StringBuilder target) {
        super(dsl);
        this.target = target;
    }
    
    @Override
    public void close() {
        writeTo(target);
    }
}
