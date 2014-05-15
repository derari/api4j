package org.cthul.api4j.gen;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.library.ClassLibrary;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaPackage;
import com.thoughtworks.qdox.model.impl.DefaultJavaClass;
import com.thoughtworks.qdox.model.impl.DefaultJavaPackage;
import groovy.lang.Closure;
import java.io.*;
import org.cthul.api4j.groovy.*;

public class GeneratedClass extends DefaultJavaClass implements AutoCloseable, DslConfigurable {

    private final File file;
    
    public GeneratedClass(JavaProjectBuilder qdox, File file, String name) {
        super(name);
        this.file = file;
        setModelWriterFactory(FixedModelWriter.FACTORY);
        setModifiers(new ModifierList("public"));
        int dot = name.lastIndexOf('.');
        if (dot > 0) {
            JavaPackage pkg = qdox.getPackageByName(name.substring(0, dot));
            if (pkg == null) {
                pkg = new DefaultJavaPackage(name.substring(0, dot));
                JavaClass jcObject = qdox.getClassByName("java.lang.Object");
                ((DefaultJavaPackage) pkg).setClassLibrary(((DefaultJavaClass) jcObject).getJavaClassLibrary());
            }
            setJavaPackage(pkg);
            setName(name.substring(dot+1));
        }
    }

    @Override
    public ClassLibrary getJavaClassLibrary() {
        if (getSource() != null) {
            ClassLibrary cl = super.getJavaClassLibrary();
            if (cl != null) return cl;
        }
        return getPackage().getJavaClassLibrary();
    }

    @Override
    public <V> V configure(GroovyDsl dsl, Closure<V> closure) {
        return DslUtils.runClosureOn(dsl, this, closure);
    }

    @Override
    public void close() throws IOException {
        file.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(file);
        try (OutputStreamWriter w = new OutputStreamWriter(fos, "UTF-8")) {
            new QdoxWriter(w).printJavaFile(this);
        }
    }
}
