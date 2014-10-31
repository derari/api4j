package org.cthul.api4j.gen;

import com.thoughtworks.qdox.model.*;
import com.thoughtworks.qdox.model.impl.AbstractBaseMethod;
import com.thoughtworks.qdox.model.impl.DefaultDocletTag;
import com.thoughtworks.qdox.model.impl.DefaultJavaMethod;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import org.cthul.api4j.api1.QdoxTools;
import static org.cthul.api4j.gen.GeneratedModel.*;

public class GeneratedMethod extends DefaultJavaMethod {
    
    {
        setModifiers(new ModifierList());
        setParameters(new LinkedList<>());        
        setTags(new DocTagList());
        setTypeParameters(new TypeParameterList<>(this));
        setAnnotations(new AnnotationList());
    }

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
//        setAnnotations(copyAll(source.getAnnotations(), this, COPY_ANNOTATION));
        setComment(source.getComment());
        setExceptions(new LinkedList<>(source.getExceptions()));
        getModifiers().addAll(source.getModifiers());
        getParameters().addAll(copyAll(source.getParameters(), this, COPY_PARAMETER));
        setReturns(source.getReturns());
        getTags().addAll(copyAll(source.getTags(), this, COPY_TAG));
        getTags().add(new DefaultDocletTag("see", QdoxTools.getDocReference(source)));
        getTypeParameters().addAll(copyMethodTypeVariables(source.getTypeParameters(), this));
    }

//    @Override
//    public List<String> getModifiers() {
//        return super.getModifiers();
//    }
    @Override
    public boolean isVarArgs() {
        if (super.isVarArgs()) return true;
        List<JavaParameter> params = getParameters();
        if (params.isEmpty()) return false;
        return params.get( params.size() -1 ).isVarArgs();
    }

    @Override
    public void setComment(String comment) {
        if (comment != null) comment = comment.replaceAll("<([^>]+)/>", "<$1>");
        super.setComment(comment);
    }
    
    @Override
    public void setParameters(List<JavaParameter> javaParameters) {
        if (javaParameters.isEmpty()) {
            // avoid bug in super.setParameters
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
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        }
    }
    
}
