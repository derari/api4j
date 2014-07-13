package org.cthul.api4j.api1;

import com.thoughtworks.qdox.model.JavaAnnotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.impl.DefaultJavaAnnotation;
import groovy.lang.Closure;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cthul.api4j.Api4JConfiguration;
import org.cthul.api4j.gen.GeneratedClass;
import org.cthul.api4j.groovy.GlobalExtension;

public class GlobalExt {
//    
    private final Api1 api1;
//    private final ParameterStringDirective paramsStr = new ParameterStringDirective(false);
//    private final ParameterStringDirective argsStr = new ParameterStringDirective(true);
//
    public GlobalExt(Api1 api1) {
        this.api1 = api1;
    }

//    public Api4JConfiguration g() {
//        return api1.getGenerator();
//    }
//
//    public Templates templates(Object any) {
//        return api1.getTemplates();
//    }
//    
//    public Template fm(Object any, String template) {
//        return g().fm(template);
//    }
//    
//    public Template fmTemplate(Object any, String template) {
//        return g().fmTemplate(template);
//    }
//    
    public Api4JConfiguration cfg() {
        return api1.getConfiguration();
    }
    
    @GlobalExtension
    public List<JavaClass> classes(List<String> patterns) {
        List<JavaClass> result = new ArrayList<>();
        for (String s: patterns) {
            result.add(cl(s));
        }
        return result;
    }
    
    @GlobalExtension
    public List<JavaClass> classes(String... patterns) {
        return classes(Arrays.asList(patterns));
    }
    
    @GlobalExtension
    public JavaClass cl(String pattern) {
        int i = pattern.indexOf('<');
        if (i > 0) pattern = pattern.substring(0, i);
        return cfg().getQdox().getClassByName(pattern);
    }
//    public JavaClass asClass(Object any, String pattern) {
//        return classes(any, pattern).get(0);
//    } 
//    
//    public List<JavaClass> classes(Object any, String... patterns) {
//        return classes(null, Arrays.asList(patterns));
//    }
//    
    
    @GlobalExtension
    public Object generateClass(String name, Closure<?> c) {
        return api1.generateClass(name, c);
    }
    
    @GlobalExtension
    public GeneratedClass generateClass(String name) {
        return api1.generateClass(name);
    }
    
    @GlobalExtension
    public Object generateClass(Closure<?> c) {
        return api1.generateClass(c);
    }
    
    @GlobalExtension
    public GeneratedClass generateClass() {
        return api1.generateClass();
    }
    
    @GlobalExtension
    public Object generateClass(GenerateTask task) {
        return generateClass(task.getName(), task.getClosure());
    }

    @GlobalExtension
    public Object generateInterface(String name, Closure<?> c) {
        return generateInterface(name).configure(api1.dsl(), c);
    }
    
    @GlobalExtension
    public GeneratedClass generateInterface(String name) {
        GeneratedClass gc = generateClass(name);
        gc.setInterface(true);
        return gc;
    }
    
    @GlobalExtension
    public Object generateInterface(Closure<?> c) {
        return generateInterface().configure(api1.dsl(), c);
    }
    
    @GlobalExtension
    public GeneratedClass generateInterface() {
        GeneratedClass gc = generateClass();
        gc.setInterface(true);
        return gc;
    }
    
    @GlobalExtension
    public Object generateInterface(GenerateTask task) {
        return generateInterface(task.getName(), task.getClosure());
    }

    @GlobalExtension
    public JavaAnnotation at(String at) {
        JavaClass jc = cfg().getQdox().getClassByName(at);
        return new DefaultJavaAnnotation(jc, -1);
    }
    
    @GlobalExtension
    public File scriptFile(String path) {
        URI uri = api1.getContext().getRoot().toURI();
//        try {
            String s = uri.resolve(api1.getContext().getUri()).resolve(path).toString();
            if (s.startsWith("file:")) s = s.substring(5);
            while (s.startsWith("\\")) s = s.substring(1);
            return new File(s);
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
    }
    
//    public String paramsString(Object any, Object... args) throws TemplateModelException {
//        return paramsStr.exec(Arrays.asList(args)).toString();
//    }
//    
//    public String argsString(Object any, Object... args) throws TemplateModelException {
//        return argsStr.exec(Arrays.asList(args)).toString();
//    }
}
