package org.cthul.api4j.gen;

import com.thoughtworks.qdox.model.impl.DefaultJavaClass;
import groovy.lang.Closure;
import org.cthul.api4j.groovy.DslUtils;
import org.cthul.api4j.groovy.GroovyDsl;

public class GeneratedClass extends DefaultJavaClass implements AutoCloseable {

    private final GroovyDsl dsl;
    private final GeneratorBase file;

    public GeneratedClass(GroovyDsl dsl, GeneratorBase file) {
        this.dsl = dsl;
        this.file = file;
    }

    public GeneratedClass(GroovyDsl dsl, GeneratorBase file, String name) {
        super(name);
        this.dsl = dsl;
        this.file = file;
    }

    public <T> T configure(Closure<T> cfg) {
        return DslUtils.configure(dsl, this, cfg);
    }

    @Override
    public void close() throws Exception {
        
    }
}
