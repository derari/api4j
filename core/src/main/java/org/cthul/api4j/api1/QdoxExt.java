package org.cthul.api4j.api1;

import com.thoughtworks.qdox.model.AbstractBaseJavaEntity;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.JavaClass;
import org.cthul.api4j.api.PatternSearcher;

public class QdoxExt {
    
    public static boolean hasAnnotation(AbstractBaseJavaEntity jm, String... patterns) {
        PatternSearcher searcher = PatternSearcher.forPatterns(patterns);
        for (Annotation at: jm.getAnnotations()) {
            JavaClass jc = at.getType().getJavaClass();
            if (searcher.eval(jc)) return true;
        }
        return false;
    }
}
