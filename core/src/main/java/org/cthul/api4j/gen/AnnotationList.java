package org.cthul.api4j.gen;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaAnnotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.impl.DefaultJavaAnnotation;
import org.cthul.api4j.api1.QdoxTools;

/**
 *
 */
public class AnnotationList extends AutoParsingList<JavaAnnotation> {

    private JavaProjectBuilder qdox;

    public AnnotationList() {
    }

    public AnnotationList(JavaProjectBuilder qdox) {
        this.qdox = qdox;
    }
    
    @Override
    protected JavaAnnotation parse(String s) {
        JavaClass jc;
        if (qdox == null) {
            jc = QdoxTools.asClass(s);
        } else {
            jc = qdox.getClassByName(s);
        }
        return new DefaultJavaAnnotation(jc, 0);
    }    
}
