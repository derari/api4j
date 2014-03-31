package org.cthul.api4j.gen;

import java.io.File;
import java.io.IOException;
import org.cthul.api4j.groovy.GroovyDsl;

public class ClassGenerator extends FileGenerator {
    
    private static File javaFile(File out, String name) {
        return new File(out, name.replace('.', '/') + ".java");
    }
    
    private final String name;
    private final SimpleGenerator importGen;
    private final SimpleGenerator docGen;

    public ClassGenerator(GroovyDsl dsl, File out, String name, String encoding) {
        super(dsl, javaFile(out, name), encoding);
        this.name = name.replace('/', '.');
        importGen = new SimpleGenerator(dsl());
        docGen = new SimpleGenerator(dsl());
    }
    
    public ClassGenerator(GroovyDsl dsl, File out, String name) {
        super(dsl, javaFile(out, name));
        this.name = name.replace('/', '.');
        importGen = new SimpleGenerator(dsl());
        docGen = new SimpleGenerator(dsl());
    }
    
    public String getPackage() {
        int i = name.lastIndexOf('.');
        return name.substring(0, i);
    }
    
    public String getSimpleName() {
        int i = name.lastIndexOf('.');
        return name.substring(i+1);
    }

    @Override
    protected void writePreTo(Appendable a) throws IOException {
        a.append("package ");
        a.append(getPackage());
        a.append(";\n\n");
        importGen.writeTo(a);
        super.writePreTo(a);
    }

    @Override
    protected void writeBodyTo(Appendable a) throws IOException {
        docGen.writeBodyTo(a);
        a.append("public class ");
        a.append(getSimpleName());
        a.append(" {\n");
        super.writeBodyTo(a);
    }

    @Override
    protected void writePostTo(Appendable a) throws IOException {
        super.writePostTo(a); 
        a.append("}\n");
    }
}
