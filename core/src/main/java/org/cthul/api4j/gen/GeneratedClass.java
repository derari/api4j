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
        String pkgName = dot > 0 ? name.substring(0, dot) : "";
        JavaPackage pkg = qdox.getPackageByName(pkgName);
        if (pkg == null) {
            pkg = new DefaultJavaPackage(pkgName);
            JavaClass jcObject = qdox.getClassByName("java.lang.Object");
            ((DefaultJavaPackage) pkg).setClassLibrary(((DefaultJavaClass) jcObject).getJavaClassLibrary());
        }
        setJavaPackage(pkg);
        setName(name.substring(dot+1));
    }
    
    public GeneratedClass(String name) {
        super(name);
        file = null;
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
        if (file == null) return;
        file.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(file);
        try (OutputStreamWriter w = new OutputStreamWriter(fos, "UTF-8")) {
            new QdoxWriter(w).printJavaFile(this);
        }
    }
    
    

//    @Override
//    @SuppressWarnings("unchecked")
//    public List<JavaClass> getInterfaces() {
//        try {
//            return (List<JavaClass>) fImplements.get(this);
//        } catch (IllegalAccessException | IllegalArgumentException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    
//    private static final Field fImplements;
//    
//    static {
//        try {
//            Field fImpl = null;
//            for (Field f: DefaultJavaClass.class.getDeclaredFields()) {
//                if ("implementz".equals(f.getName())) {
//                    fImpl = f;
//                    break;
//                }
//            }
//            fImpl.setAccessible(true);
//            fImplements = fImpl;
//        } catch (SecurityException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
