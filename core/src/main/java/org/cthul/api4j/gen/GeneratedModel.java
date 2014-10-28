package org.cthul.api4j.gen;

import com.thoughtworks.qdox.model.*;
import com.thoughtworks.qdox.model.impl.*;
import groovy.lang.Closure;
import java.util.*;
import org.cthul.api4j.groovy.DslUtils;

public class GeneratedModel {

    public static interface Copy<T, C> {
        T copy(T source, C context);
    }
    
    public static <T> List<T> copyAll(Collection<T> source, Copy<T, ?> copy) {
        return copyAll(source, null, copy);
    }
    
    public static <T, C> List<T> copyAll(Collection<T> source, C context, Copy<T, C> copy) {
        List<T> result = new LinkedList<>();
        source.stream().forEach((s) -> {
            result.add(copy.copy(s, context));
        });
        return result;
    }
    
    public static <T, C> List<T> copyAll(Collection<T> source, C context, Copy<T, C> copy, Closure<?> cfg) {
        List<T> result = new LinkedList<>();
        source.stream().map((s) -> {
            T c = copy.copy(s, context);
            DslUtils.runClosureOn(c, cfg, s);
            return c;
        }).forEach((c) -> {
            result.add(c);
        });
        return result;
    }
    
    public static List<JavaTypeVariable<JavaMethod>> copyMethodTypeVariables(List<? extends JavaTypeVariable<?>> vars, JavaMethod method) {
        return (List) copyAll(vars, method, (Copy) COPY_TYPE_VAR);
    }
    
    public static final Copy<JavaAnnotation, JavaAnnotatedElement> COPY_ANNOTATION = (source, ctx) -> {
        DefaultJavaAnnotation at = new DefaultJavaAnnotation(
                source.getType(), null, null, 0);
        at.getNamedParameterMap().putAll(source.getNamedParameterMap());
        at.getPropertyMap().putAll(source.getPropertyMap());
        at.setContext(ctx);
        return at;
    };
    
    public static final Copy<JavaMethod, JavaClass> COPY_METHOD = (source, parent) -> {
        GeneratedMethod m = new GeneratedMethod(parent, source);
        return m;
    };
    
    public static final Copy<JavaParameter, JavaMethod> COPY_PARAMETER = (source, parent) -> {
        DefaultJavaParameter p = new DefaultJavaParameter(
                (JavaClass) source.getType(), source.getName(), source.isVarArgs());
        p.setDeclarator(parent);
        p.setAnnotations(copyAll(source.getAnnotations(), p, COPY_ANNOTATION));
        return p;
    };
    
    public static final Copy<DocletTag, JavaAnnotatedElement> COPY_TAG = (source, parent) -> {
        DefaultDocletTag t = new DefaultDocletTag(source.getName(), source.getValue(), parent, -1);
        return t;
    };
    
    public static final Copy<JavaTypeVariable<?>, JavaGenericDeclaration> COPY_TYPE_VAR = (source, context) -> {
        DefaultJavaTypeVariable<?> v = new DefaultJavaTypeVariable<>(source.getName(), context);
        v.setBounds(source.getBounds());
        return v;
    };
}
