package org.cthul.api4j.gen;

import com.thoughtworks.qdox.model.*;
import java.io.IOException;
import java.util.List;

public class QdoxWriter {
    
    private final Appendable out;

    public QdoxWriter(Appendable out) {
        this.out = out;
    }
    
    public void printJavaFile(JavaClass jc) throws IOException {
        out.append("package ");
        out.append(jc.getPackageName());
        out.append(";\n\n");
        String s = jc.getCodeBlock();
        int i = s.indexOf(jc.getName()) + jc.getName().length();
        List<JavaTypeVariable<JavaGenericDeclaration>> typeParams = jc.getTypeParameters();
        if (typeParams.isEmpty() || s.charAt(i) == '<') {
            out.append(s);
            return;
        }
        // hotfix: insert type parameters after class name
        out.append(s.substring(0, i));
        out.append("<");
        boolean first = true;
        for (JavaTypeVariable<?> p: typeParams) {
            if (first) first = false;
            else out.append(",");
            out.append(p.getGenericValue());
        }
        out.append(">");
        out.append(s.substring(i));
    }
}
