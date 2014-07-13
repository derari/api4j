package org.cthul.api4j.gen;

import com.thoughtworks.qdox.model.*;
import com.thoughtworks.qdox.model.impl.AbstractBaseMethod;
import com.thoughtworks.qdox.model.impl.DefaultJavaMethod;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import static org.cthul.api4j.gen.GeneratedModel.*;

public class GeneratedMethod extends DefaultJavaMethod {

    public GeneratedMethod() {
        setModifiers(new ModifierList());
        setParameters(new LinkedList<JavaParameter>());
    }

    public GeneratedMethod(String name) {
        super(name);
        setModifiers(new ModifierList());
        setParameters(new LinkedList<JavaParameter>());
    }

    public GeneratedMethod(JavaClass returns, String name) {
        super(returns, name);
        setModifiers(new ModifierList());
        setParameters(new LinkedList<JavaParameter>());
    }
    
    @SuppressWarnings("LeakingThisInConstructor")
    public GeneratedMethod(JavaClass parent, JavaMethod source) {
        super(source.getName());
        setParentClass(parent);
//        setAnnotations(copyAll(source.getAnnotations(), this, COPY_ANNOTATION));
        setComment(source.getComment());
        setExceptions(new LinkedList<>(source.getExceptions()));
        setModifiers(new ModifierList(source.getModifiers()));
        setParameters(copyAll(source.getParameters(), this, COPY_PARAMETER));
        setReturns(source.getReturns());
        setTags(copyAll(source.getTags(), this, COPY_TAG));
        setTypeParameters(copyMethodTypeVariables(source.getTypeParameters(), this));
    }

    @Override
    public List<String> getModifiers() {
        return super.getModifiers();
    }
    
    

    @Override
    public void setParameters(List<JavaParameter> javaParameters) {
        if (javaParameters.isEmpty()) {
            injectParameters(this, javaParameters);
        } else {
            super.setParameters(javaParameters);
        }
    }
    
    static void injectParameters(AbstractBaseMethod m, List<JavaParameter> params) {
        try {
            fParameters.set(m, params);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Field fParameters;
    
    static {
        try {
            Field fParams = null;
            for (Field f: AbstractBaseMethod.class.getDeclaredFields()) {
                if (f.getName().equals("parameters")) {
                    fParams = f;
                    break;
                }
            }
            fParams.setAccessible(true);
            fParameters = fParams;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
