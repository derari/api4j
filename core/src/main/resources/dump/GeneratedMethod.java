package org.cthul.api4j.gen;

import com.thoughtworks.qdox.model.*;
import com.thoughtworks.qdox.model.impl.DefaultJavaMethod;
import java.util.LinkedList;
import static org.cthul.api4j.gen.GeneratedModel.*;

public class GeneratedMethod extends DefaultJavaMethod {

    public GeneratedMethod() {
    }

    public GeneratedMethod(String name) {
        super(name);
    }

    public GeneratedMethod(JavaClass returns, String name) {
        super(returns, name);
    }
    
    @SuppressWarnings("LeakingThisInConstructor")
    public GeneratedMethod(JavaClass parent, JavaMethod source) {
        super(source.getName());
        setParentClass(parent);
        setAnnotations(copyAll(source.getAnnotations(), this, COPY_ANNOTATION));
        setExceptions(new LinkedList<>(source.getExceptions()));
        setModifiers(new LinkedList<>(source.getModifiers()));
        setParameters(copyAll(source.getParameters(), this, COPY_PARAMETER));
        setTags(copyAll(source.getTags(), this, COPY_TAG));
        setTypeParameters(copyMethodTypeVariables(source.getTypeParameters(), this));
    }

    
    
}
