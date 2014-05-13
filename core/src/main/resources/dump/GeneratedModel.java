package org.cthul.api4j.gen;

import com.thoughtworks.qdox.model.*;
import com.thoughtworks.qdox.model.impl.*;
import java.util.LinkedList;
import java.util.List;

public class GeneratedModel {

    public static interface Copy<T, C> {
        T copy(T source, C context);
    }
    
    public static <T> List<T> copyAll(List<T> source, Copy<T, ?> copy) {
        return copyAll(source, null, copy);
    }
    
    public static <T, C> List<T> copyAll(List<T> source, C context, Copy<T, C> copy) {
        List<T> result = new LinkedList<>();
        for (T s: source) {
            result.add(copy.copy(s, context));
        }
        return result;
    }
    
    public static List<JavaTypeVariable<JavaMethod>> copyMethodTypeVariables(List<? extends JavaTypeVariable<?>> vars, JavaMethod method) {
        return (List) copyAll(vars, method, (Copy) COPY_TYPE_VAR);
    }
    
    public static final Copy<JavaAnnotation, JavaAnnotatedElement> COPY_ANNOTATION = new Copy<JavaAnnotation, JavaAnnotatedElement>() {
        @Override
        public JavaAnnotation copy(JavaAnnotation source, JavaAnnotatedElement ctx) {
            DefaultJavaAnnotation at = new DefaultJavaAnnotation(
                    source.getType(), null, null, 0);
            at.getNamedParameterMap().putAll(source.getNamedParameterMap());
            at.getPropertyMap().putAll(source.getPropertyMap());
            at.setContext(ctx);
            return at;
        }
    };
    
    public static final Copy<JavaMethod, JavaClass> COPY_METHOD = new Copy<JavaMethod, JavaClass>() {
        @Override
        public JavaMethod copy(JavaMethod source, JavaClass parent) {
            GeneratedMethod m = new GeneratedMethod(parent, source);
            return m;
        }
    };
    
    public static final Copy<JavaParameter, JavaMethod> COPY_PARAMETER = new Copy<JavaParameter, JavaMethod>() {
        @Override
        public JavaParameter copy(JavaParameter source, JavaMethod parent) {
            DefaultJavaParameter p = new DefaultJavaParameter(
                    (JavaClass) source.getType(), source.getName(), source.isVarArgs());
            p.setParentMethod(parent);
            p.setAnnotations(copyAll(source.getAnnotations(), p, COPY_ANNOTATION));
            return p;
        }
    };
    
    public static final Copy<DocletTag, JavaAnnotatedElement> COPY_TAG = new Copy<DocletTag, JavaAnnotatedElement>() {
        @Override
        public DocletTag copy(DocletTag source, JavaAnnotatedElement parent) {
            DefaultDocletTag t = new DefaultDocletTag(source.getName(), source.getValue(), parent, -1);
            return t;
        }
    };
    
    public static final Copy<JavaTypeVariable<?>, JavaGenericDeclaration> COPY_TYPE_VAR = new Copy<JavaTypeVariable<?>, JavaGenericDeclaration>() {

        @Override
        public JavaTypeVariable<?> copy(JavaTypeVariable<?> source, JavaGenericDeclaration context) {
            DefaultJavaTypeVariable<?> v = new DefaultJavaTypeVariable<>(source.getName(), context);
            v.setBounds(source.getBounds());
            return v;
        }
    };
    
    
}
