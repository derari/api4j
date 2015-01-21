package org.cthul.api4j.gen;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.library.ClassLibrary;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaPackage;
import com.thoughtworks.qdox.model.JavaType;
import com.thoughtworks.qdox.model.impl.DefaultJavaClass;
import com.thoughtworks.qdox.model.impl.DefaultJavaPackage;
import com.thoughtworks.qdox.model.impl.DefaultJavaTypeVariable;
import java.io.*;
import java.util.List;
import org.cthul.api4j.groovy.*;

public class GeneratedClass extends DefaultJavaClass implements AutoCloseable, ClosureConfigurable {

    private final File file;
    private JavaClassList implementz;
    
    private void init(JavaProjectBuilder qdox) {
        setModifiers(new ModifierList());
        setTypeParameters(new TypeParameterList<>(this));
        setImplementz(new JavaClassList(qdox));
        setTags(new DocTagList());
        setAnnotations(new AnnotationList(qdox));
    }
    
    public GeneratedClass(JavaProjectBuilder qdox, File file, String name) {
        super(name);
        init(qdox);
        this.file = file;
        setModelWriterFactory(FixedModelWriter.FACTORY);
        getModifiers().add("public");
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
        init(null);
        file = null;
    }

//    @Override
//    public void setParentClass(JavaClass parentClass) {
//        if (parentClass instanceof GeneratedClass) {
//            GeneratedClass gc = (GeneratedClass) parentClass;
//            ((JavaClassList) getInterfaces()).qdox = gc.qdox;
//        }
//        super.setParentClass(parentClass);
//    }
    @Override
    public void setComment(String comment) {
        if (comment != null) comment = comment.replaceAll("<([^>]+)/>", "<$1>");
        super.setComment(comment);
    }
    
    @Override
    public void setImplementz(List<JavaClass> implementz) {
        this.implementz = JavaClassList.wrap(implementz);
        super.setImplementz(this.implementz);
    }

    @Override
    public JavaClassList getImplementedInterfaces() {
        return implementz;
    }

    @Override
    public JavaClassList getInterfaces() {
        return implementz;
    }

    @Override
    public List<JavaType> getImplements() {
        return (List) implementz;
    }

    @Override
    public TypeParameterList<JavaClass, DefaultJavaTypeVariable<JavaClass>> getTypeParameters() {
        return TypeParameterList.wrap(this, super.getTypeParameters());
    }

    @Override
    public DocTagList getTags() {
        return DocTagList.wrap(super.getTags());
    }

    @Override
    public AnnotationList getAnnotations() {
        return AnnotationList.wrap(super.getAnnotations());
    }
    
    @Override
    public ClassLibrary getJavaClassLibrary() {
        if (getSource() != null) {
            ClassLibrary cl = super.getJavaClassLibrary();
            if (cl != null) return cl;
        }
        if (getPackage() != null) {
            ClassLibrary cl = getPackage().getJavaClassLibrary();
            if (cl != null) return cl;
        }
        if (getDeclaringClass() != null) {
            ClassLibrary cl = getDeclaringClass().getJavaClassLibrary();
            if (cl != null) return cl;
        }
        return null;
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
