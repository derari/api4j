package org.cthul.api4j.api1;

import com.thoughtworks.qdox.model.JavaClass;
import groovy.lang.Closure;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cthul.api4j.api.Generator;
import org.cthul.api4j.api.Templates;

public class GlobalExt {
    
    private final Api1 api1;

    public GlobalExt(Api1 api1) {
        this.api1 = api1;
    }

    public Generator g() {
        return api1.getGenerator();
    }

    public Templates templates(Object any) {
        return api1.getTemplates();
    }
    
    public List<JavaClass> classes(Object any, List<String> patterns) {
        List<JavaClass> result = new ArrayList<>();
        for (String s: patterns) {
            JavaClass jc = g().getQdox().getClassByName(s);
            result.add(jc);
        }
        return result;
    }
    
    public List<JavaClass> classes(Object any, String... patterns) {
        return classes(null, Arrays.asList(patterns));
    }
    
    public Object generateClass(Object any, String name, Closure<?> c) throws IOException {
        return g().generateClass(api1.dsl(), name, c);
    }
    
    public Object generateClass(Object any, GenerateTask task) throws IOException {
        return g().generateClass(api1.dsl(), task.getName(), task.getClosure());
    }
    
    public Object generateClass(Object any, Closure<?> c) throws IOException {
        return g().generateClass(api1.dsl(), api1.getDefaultClassName(), c);
    }
}
