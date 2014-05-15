package org.cthul.api4j.gen;

import groovy.lang.Closure;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.cthul.api4j.api.Api4JConfiguration;
import org.cthul.api4j.groovy.DslUtils;
import org.cthul.api4j.groovy.GroovyDsl;

public class ClassGenerator extends SimpleGenerator {
    
    private static ThreadLocal<Deque<ClassGenerator>> genStack = new ThreadLocal<Deque<ClassGenerator>>() {
        @Override
        protected Deque<ClassGenerator> initialValue() {
            return new ArrayDeque<>();
        }
    };
    
    private static void __push(ClassGenerator cg) {
        genStack.get().push(cg);
    }
    
    private static void __pop() {
        genStack.get().pop();
    }
    
    public static ClassGenerator current() {
        return genStack.get().peek();
    }
    
    private static File javaFile(File out, String name) {
        return new File(out, name.replace('.', '/') + ".java");
    }
    
    private final String name;
    private final SimpleGenerator importGen;
    private final SimpleGenerator docGen;
    private final List<Object> interfaces = new ArrayList<>();
    private Object superclass = null;
    private boolean isAbstract = false;

    public ClassGenerator(GroovyDsl dsl, String name) {
        super(dsl);
        this.name = name.replace('/', '.');
        importGen = new SimpleGenerator(dsl());
        docGen = new SimpleGenerator(dsl());
    }
    
    public Object configure(Closure<?> c) {
        try {
            __push(this);
            return DslUtils.configure(dsl(), this, c);
        } finally {
            __pop();
        }
    }
    
    public JavaFile generateFile(final Api4JConfiguration g) {
        __push(this);
        return new JavaFile() {
            @Override
            public void close() throws IOException {
                __pop();
                g.writeJavaFile(ClassGenerator.this);
            }
        };
    }
    
    public String getPackage() {
        int i = name.lastIndexOf('.');
        return name.substring(0, i);
    }
    
    public String getSimpleName() {
        int i = name.lastIndexOf('.');
        return name.substring(i+1);
    }

    public String getName() {
        return name;
    }

    public List<Object> getInterfaces() {
        return interfaces;
    }

    public Object getSuperclass() {
        return superclass;
    }

    public void setSuperclass(Object superclass) {
        this.superclass = superclass;
    }

    public boolean getIsAbstract() {
        return isAbstract;
    }

    public void setIsAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }
    
    public GroovyDsl getDsl() {
        return dsl();
    }
    
    public File getFile(File out) {
        return javaFile(out, name);
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
        a.append("public ");
        if (isAbstract) a.append("abstract ");
        a.append("class ");
        a.append(getSimpleName());
        writeSuperclassTo(a);
        writeInterfacesTo(a);
        a.append(" {\n");
        
        SimpleGenerator classBody = new IndentedGenerator("    ", dsl());
        classBody.body(getBody());
        classBody.writeTo(a);
    }
    
    protected void writeInterfacesTo(Appendable a) throws IOException {
        if (interfaces.isEmpty()) return;
        a.append(" implements ");
        Set<String> ifaceSet = new LinkedHashSet<>();
        for (Object iface: interfaces) {
            ifaceSet.add(classObjectToString(iface));
        }
        boolean first = true;
        for (String s: ifaceSet) {
            if (first) first = false;
            else a.append(", ");
            a.append(s);
        }
    }
    
    protected void writeSuperclassTo(Appendable a) throws IOException {
        if (superclass != null) {
            a.append(" extends ");
            a.append(classObjectToString(superclass));
        }
    }
    
    public static String classObjectToString(Object o) {
        return o.toString();
    }

    @Override
    protected void writePostTo(Appendable a) throws IOException {
        super.writePostTo(a); 
        a.append("}\n");
    }
    
    public static interface JavaFile extends AutoCloseable {
        @Override
        void close() throws IOException;
    }
}
