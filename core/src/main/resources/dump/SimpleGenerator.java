package org.cthul.api4j.gen;

import java.io.IOException;
import org.cthul.api4j.groovy.GroovyDsl;

public class SimpleGenerator extends GeneratorBase implements SelfGenerating {

    public SimpleGenerator(GroovyDsl dsl) {
        super(dsl);
    }

    @Override
    public void writeTo(Appendable a) throws IOException {
        super.writeTo(a);
    }

    @Override
    public void writeTo(StringBuilder a) {
        super.writeTo(a);
    }
}
