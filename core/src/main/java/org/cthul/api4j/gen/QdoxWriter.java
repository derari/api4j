package org.cthul.api4j.gen;

import com.thoughtworks.qdox.model.*;
import java.lang.reflect.GenericDeclaration;
import java.util.List;

public class QdoxWriter {
    
    private final GeneratorBase out;

    public QdoxWriter(GeneratorBase out) {
        this.out = out;
    }
    
    public void printJavaFile(JavaClass jc) {
        out.body("package ");
        out.body(jc.getPackageName());
        out.body(";\n\n");
        String s = jc.getCodeBlock();
        int i = s.indexOf(jc.getName()) + jc.getName().length();
        List<JavaTypeVariable<JavaGenericDeclaration>> typeParams = jc.getTypeParameters();
        if (typeParams.isEmpty() || s.charAt(i) == '<') {
            out.body(s);
            return;
        }
        out.body(s.substring(0, i));
        out.body("<");
        boolean first = true;
        for (JavaTypeVariable<?> p: typeParams) {
            if (first) first = false;
            else out.body(",");
            out.body(p.getGenericValue());
        }
        out.body(">");
        out.body(s.substring(i));
    }
}
