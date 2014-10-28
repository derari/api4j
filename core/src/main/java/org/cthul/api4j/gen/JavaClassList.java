package org.cthul.api4j.gen;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import org.cthul.api4j.api1.QdoxTools;

public class JavaClassList extends AutoParsingList<JavaClass> {
    
    JavaProjectBuilder qdox;

    public JavaClassList(JavaProjectBuilder qdox) {
        this.qdox = qdox;
    }

    public JavaClassList() {
    }

    public boolean add(String clazz) {
        return add(parse(clazz));
    }

    @Override
    protected JavaClass parse(String clazz) {
        if (qdox == null) {
            return QdoxTools.asClass(clazz);
        }
        return qdox.getClassByName(clazz);
    }
}
