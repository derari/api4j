package org.cthul.api4j.gen;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaType;
import java.util.List;
import org.cthul.api4j.api1.QdoxTools;

public class JavaClassList extends AutoParsingList<JavaClass> {
    
    public static JavaClassList wrap(List<JavaClass> list) {
        return wrap(null, list);
    }
    
    public static JavaClassList wrap(JavaProjectBuilder qdox, List<JavaClass> list) {
        return wrap(list, JavaClassList.class, l -> new JavaClassList(qdox, l));
    }
    
    final JavaProjectBuilder qdox;

    public JavaClassList(JavaProjectBuilder qdox) {
        if (qdox == null) qdox = QdoxTools.getQdox();
        this.qdox = qdox;
    }

    public JavaClassList() {
        this(null);
    }

    public boolean add(JavaType e) {
        return super.add((JavaClass) e);
    }
    
    public JavaClassList(JavaProjectBuilder qdox, List<JavaClass> list) {
        super(list);
        this.qdox = qdox;
    }

    @Override
    protected JavaClass parse(String clazz) {
//        if (qdox == null) {
//            qdox = QdoxTools.getQdox();
//            JavaClass jc = QdoxTools.asClass(clazz);
//            int iOpen = clazz.indexOf('<');
//            if (iOpen < 0) return jc;
//            int iClose = clazz.lastIndexOf('>');
//            if (iClose < 0) iClose = clazz.length();
//            QdoxTools.withArgs(jc, clazz.substring(iOpen, iClose));
//        }
        if (qdox == null) {
            QdoxTools.asType(clazz);
        }
        return qdox.getClassByName(clazz);
    }
}
