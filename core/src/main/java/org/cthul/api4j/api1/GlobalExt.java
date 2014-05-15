package org.cthul.api4j.api1;

import com.thoughtworks.qdox.model.JavaClass;
import groovy.lang.Closure;
import java.util.ArrayList;
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
            int i = s.indexOf('<');
            if (i > 0) s = s.substring(0, i);
            JavaClass jc = cfg().getQdox().getClassByName(s);
            result.add(jc);
        }
        return result;
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
    public Object generateClass(GenerateTask task) {
        return generateClass(task.getName(), task.getClosure());
    }

//    public String paramsString(Object any, Object... args) throws TemplateModelException {
//        return paramsStr.exec(Arrays.asList(args)).toString();
//    }
//    
//    public String argsString(Object any, Object... args) throws TemplateModelException {
//        return argsStr.exec(Arrays.asList(args)).toString();
//    }
}
