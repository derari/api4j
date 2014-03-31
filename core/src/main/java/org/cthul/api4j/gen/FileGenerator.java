package org.cthul.api4j.gen;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.cthul.api4j.groovy.GroovyDsl;

public class FileGenerator extends GeneratorBase implements AutoCloseable {

    private final File f;
    private final String encoding;

    public FileGenerator(GroovyDsl dsl, File f, String encoding) {
        super(dsl);
        this.f = f;
        this.encoding = encoding;
    }

    public FileGenerator(GroovyDsl dsl, File f) {
        this(dsl, f, "utf-8");
    }

    @Override
    public void close() throws IOException {
        f.getParentFile().mkdirs();
        try (PrintWriter pw = new PrintWriter(f, encoding)) {
            writeTo(pw);
        }
    }
}
