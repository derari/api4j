package org.cthul.api4j.gen;

import com.thoughtworks.qdox.model.*;
import com.thoughtworks.qdox.model.impl.DefaultJavaConstructor;
import java.util.*;

public class GeneratedConstructor extends DefaultJavaConstructor {

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public GeneratedConstructor() {
        setModifiers(new ModifierList("public"));
        setTags(Collections.<DocletTag>emptyList());
        setParameters(new LinkedList<JavaParameter>());
    }
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public GeneratedConstructor(JavaMethod jm) {
        this();
        setComment(jm.getComment());
        setParameters(GeneratedModel.copyAll(jm.getParameters(), GeneratedModel.COPY_PARAMETER));
    }

    @Override
    public void setParentClass(JavaClass parentClass) {
        super.setParentClass(parentClass);
        setName(parentClass.getName());
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
    public void setTags(List<DocletTag> tagList) {
        tagList = new LinkedList<>(tagList);
        for (Iterator<DocletTag> it = tagList.iterator(); it.hasNext(); ) {
            DocletTag tag = it.next();
            if ("returns".equals(tag.getName())) {
                it.remove();
            }
        }
        super.setTags(tagList);
    }
}
