package org.cthul.api4j.gen;

import com.thoughtworks.qdox.model.*;
import com.thoughtworks.qdox.model.impl.DefaultJavaConstructor;
import java.util.*;
import static org.cthul.api4j.gen.GeneratedModel.COPY_TAG;
import static org.cthul.api4j.gen.GeneratedModel.copyAll;

public class GeneratedConstructor extends DefaultJavaConstructor {
    
    {
        setModifiers(new ModifierList());
        setParameters(new LinkedList<>());
        setTags(new DocTagList(true));
        setTypeParameters(new TypeParameterList<>(this));
        setAnnotations(new AnnotationList());
    }
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public GeneratedConstructor() {
        getModifiers().add("public");
    }
    
    @SuppressWarnings({"OverridableMethodCallInConstructor", "LeakingThisInConstructor"})
    public GeneratedConstructor(JavaMethod source) {
        this();
        setComment(source.getComment());
        getTags().addAll(copyAll(source.getTags(), this, COPY_TAG));
        getParameters().addAll(copyAll(source.getParameters(), GeneratedModel.COPY_PARAMETER));
    }

    @Override
    public void setParentClass(JavaClass parentClass) {
        super.setParentClass(parentClass);
        setName(parentClass.getName());
    }
    
    @Override
    public void setComment(String comment) {
        if (comment != null) comment = comment.replaceAll("<([^>]+)/>", "<$1>");
        super.setComment(comment);
    }
    
    @Override
    public void setParameters(List<JavaParameter> javaParameters) {
        if (javaParameters.isEmpty()) {
            GeneratedMethod.injectParameters(this, javaParameters);
        } else {
            super.setParameters(javaParameters);
        }
    }
    
    
    @Override
    public TypeParameterList<JavaConstructor, JavaTypeVariable<JavaConstructor>> getTypeParameters() {
        return TypeParameterList.wrap(this, super.getTypeParameters());
    }

    @Override
    public DocTagList getTags() {
        return DocTagList.wrap(super.getTags());
    }

    @Override
    public AnnotationList getAnnotations() {
        return AnnotationList.wrap(super.getAnnotations());
    }
}
