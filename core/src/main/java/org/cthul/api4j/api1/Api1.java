package org.cthul.api4j.api1;

import groovy.lang.Closure;
import java.util.Arrays;
import org.cthul.api4j.api.Generator;
import org.cthul.api4j.api.Templates;
import org.cthul.api4j.gen.GeneratorUtils;
import org.cthul.api4j.groovy.DslNative;
import org.cthul.api4j.groovy.DslUtils;
import org.cthul.api4j.groovy.GroovyDsl;

public class Api1 extends DslNative {
    
    private final Generator g;
    private final GroovyDsl dsl;
    private final Templates templates;
    private final String uri;

    @SuppressWarnings("LeakingThisInConstructor")
    public Api1(Generator g, String uri) {
        this.g = g;
        this.uri = uri;
        dsl = new GroovyDsl();
        dsl.addGlobals(g, dsl, this);
        dsl.getExtensions().addAll(Arrays.asList(
                GlobalExt.class,
                QdoxExt.class
                ));
        templates = new Templates(g.getTemplates());
        String[] defTemplates = {"delegator", "fields", "full_comment", 
                    "getter", "initializer", "staticDelegator"};
        for (String t: defTemplates) {
            templates.set(t, g.fmTemplate("org/cthul/api4j/api1/" + t + ".ftl"));
        }
    }

    //    public DslList<JavaClass> classes(List<String> patterns) {
    //        DslList<JavaClass> result = new DslList<>(dsl());
    //        for (String s: patterns) {
    //            JavaClass jc = g.getQdox().getClassByName(s);
    //            result.addValue(jc);
    //        }
    //        return result;
    //    }
    //
    //    public DslList<JavaClass> classes(String... patterns) {
    ////        PatternSearcher s = PatternSearcher.forPatterns(patterns);
    ////        List<JavaClass> list = gen.getQdox().search(s);
    ////        return new DslList<>(dsl(), list);
    //        return classes(Arrays.asList(patterns));
    //    }
    @Override
    protected Object methodMissing(String name, Object arg) {
        Object[] args = (Object[]) arg;
        if (name.contains(".") 
                && args != null && args.length == 1
                && (args[0] instanceof Closure)) {
            return new GenerateTask(name, (Closure<?>) args[0]);
        }
        return super.methodMissing(name, arg); //To change body of generated methods, choose Tools | Templates.
    }

    public Generator getGenerator() {
        return g;
    }

    public Templates getTemplates() {
        return templates;
    }
    
    public String getDefaultClassName() {
        return GeneratorUtils.classNameForPath(uri);
    }
    
    @Override
    public GroovyDsl dsl() {
        return dsl;
    }
    
    public Object configure(Closure<?> c) {
        return DslUtils.configure(dsl(), this, c);
    }
}
