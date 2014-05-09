package org.cthul.api4j.api1;

import groovy.lang.Closure;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cthul.api4j.api.Generator;
import org.cthul.api4j.api.Templates;
import org.cthul.api4j.gen.ClassGenerator;
import org.cthul.api4j.gen.GeneratedClass;
import org.cthul.api4j.groovy.DslNative;
import org.cthul.api4j.groovy.DslUtils;
import org.cthul.api4j.groovy.GroovyDsl;

public class Api1 extends DslNative implements AutoCloseable {
    
    private final Generator g;
    private final GroovyDsl dsl;
    private final Templates templates;
    private final List<Closeable> closeables = new ArrayList<>();

    @SuppressWarnings("LeakingThisInConstructor")
    public Api1(Generator g) {
        this.g = g;
        dsl = new GroovyDsl();
        dsl.addGlobals(g, dsl, this);
        dsl.getExtensions().addAll(Arrays.asList(
                GlobalExt.class,
                QdoxExt.class
                ));
        templates = new Templates(g.getTemplates());
        templates.set("staticDelegate", g.fmTemplate("org/cthul/api4j/api1/staticDelegate.ftl"));
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
    
    @Override
    public GroovyDsl dsl() {
        return dsl;
    }
    
    public Object configure(Closure<?> c) {
        return DslUtils.configure(dsl(), this, c);
    }
    
    public GeneratedClass generateClass(String name) {
        ClassGenerator cg = getGenerator().generateClass(dsl(), name);
        return new GeneratedClass(dsl(), cg, name);
    }

    @Override
    public void close() {
        Exception lastEx = null;
        for (Closeable c: closeables) {
            try {
                c.close();
            } catch (Exception e) {
                if (lastEx != null) {
                    e.addSuppressed(lastEx);
                }
                lastEx = e;
            }
        }
        if (lastEx != null) {
            throw new RuntimeException(lastEx);
        }
    }
}
