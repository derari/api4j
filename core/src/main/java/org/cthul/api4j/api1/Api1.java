package org.cthul.api4j.api1;

import org.cthul.api4j.Api4JConfiguration;
import groovy.lang.Closure;
import java.io.Closeable;
import java.util.*;
import org.cthul.api4j.api.*;
import org.cthul.api4j.gen.GeneratedClass;
import org.cthul.api4j.groovy.*;
import org.cthul.strings.JavaNames;
import org.cthul.strings.plural.DefaultEnglishPluralizer;

public class Api1 extends DslNative implements AutoCloseable {
    
    private final ScriptContext ctx;
    private final GroovyDsl dsl;
    private final List<AutoCloseable> closeables = new ArrayList<>();
    private final Templates templates;

    @SuppressWarnings("LeakingThisInConstructor")
    public Api1(ScriptContext ctx) {
        this.ctx = ctx;
        Api4JConfiguration cfg = ctx.getConfiguration();
        dsl = new GroovyDsl();
        dsl.addGlobals(ctx, dsl, this);
        dsl.getExtensions().addAll(Arrays.asList(
                GlobalExt.class,
                QdoxExt.class
            ));
        dsl.getGlobalExtensions().addAll(Arrays.asList(
                JavaNames.class,
                DefaultEnglishPluralizer.class
            ));
        
        templates = new Templates(ctx.getTemplates(), true);
        String[] defaultTemplates = {
            "call", "staticCall",
            "delegator", "staticDelegator"};
        for (String t: defaultTemplates) {
            templates.set(t, cfg.fmTemplate("org/cthul/api4j/api1/" + t + ".ftl"));
        }
    }

    public Templates getTemplates() {
        return templates;
    }

    @Override
    protected Object methodMissing(String name, Object arg) {
        Object[] args = (Object[]) arg;
        if (name.contains(".") 
                && args != null && args.length == 1
                && (args[0] instanceof Closure)) {
            return new GenerateTask(name, (Closure<?>) args[0]);
        }
        return super.methodMissing(name, arg);
    }

    public ScriptContext getContext() {
        return ctx;
    }
    
    public Api4JConfiguration getConfiguration() {
        return ctx.getConfiguration();
    }

    @Override
    public GroovyDsl dsl() {
        return dsl;
    }
    
    public Object configure(Closure<?> c) {
        try {
            return DslUtils.configure(dsl(), this, c);
        } finally {
            close();
        }
    }
    
//    public GeneratedClass generateClass(String name) {
//        ClassGenerator cg = getConfiguration().generateClass(dsl(), name);
//        return new GeneratedClass(dsl(), cg, name);
//    }
    
    public GeneratedClass generateClass(String name) {
        GeneratedClass gc = getConfiguration().generateClass(name);
        closeEventually(gc);
        return gc;
    }
    
    public Object generateClass(String name, Closure<?> closure) {
        return generateClass(name).configure(dsl(), closure);
    }

    public GeneratedClass generateClass() {
        String uri = ctx.getUri();
        int dot = uri.indexOf('.');
        if (dot > 0) {
            return generateClass(uri.substring(0, dot));
        } else {
            return generateClass(uri);
        }
    }
    
    public Object generateClass(Closure<?> closure) {
        return generateClass().configure(dsl(), closure);
    }

    public void closeEventually(AutoCloseable c) {
        closeables.add(c);
    }
    
    @Override
    public void close() {
        Exception lastEx = null;
        for (AutoCloseable c: closeables) {
            try {
                c.close();
            } catch (Exception e) {
                if (lastEx != null) {
                    e.addSuppressed(lastEx);
                }
                lastEx = e;
            }
        }
        closeables.clear();
        if (lastEx != null) {
            throw new RuntimeException(lastEx);
        }
    }
}
//=======
//package org.cthul.api4j.api1;
//
//import groovy.lang.Closure;
//import java.util.Arrays;
//import org.cthul.api4j.api.Generator;
//import org.cthul.api4j.api.Templates;
//import org.cthul.api4j.gen.GeneratorUtils;
//import org.cthul.api4j.groovy.DslNative;
//import org.cthul.api4j.groovy.DslUtils;
//import org.cthul.api4j.groovy.GroovyDsl;
//
//public class Api1 extends DslNative {
//    
//    private final Api4JConfiguration g;
//    private final GroovyDsl dsl;
//    private final Templates templates;
//    private final String uri;
//
//    @SuppressWarnings("LeakingThisInConstructor")
//    public Api1(Api4JConfiguration g, String uri) {
//        this.g = g;
//        this.uri = uri;
//        dsl = new GroovyDsl();
//        dsl.addGlobals(g, dsl, this);
//        dsl.getExtensions().addAll(Arrays.asList(
//                GlobalExt.class,
//                QdoxExt.class
//                ));
//        templates = new Templates(g.getTemplates());
//        String[] defTemplates = {"delegator", "fields", "full_comment", 
//                    "getter", "initializer", "staticDelegator"};
//        for (String t: defTemplates) {
//            templates.set(t, g.fmTemplate("org/cthul/api4j/api1/" + t + ".ftl"));
//        }
//    }
//
//    //    public DslList<JavaClass> classes(List<String> patterns) {
//    //        DslList<JavaClass> result = new DslList<>(dsl());
//    //        for (String s: patterns) {
//    //            JavaClass jc = g.getQdox().getClassByName(s);
//    //            result.addValue(jc);
//    //        }
//    //        return result;
//    //    }
//    //
//    //    public DslList<JavaClass> classes(String... patterns) {
//    ////        PatternSearcher s = PatternSearcher.forPatterns(patterns);
//    ////        List<JavaClass> list = gen.getQdox().search(s);
//    ////        return new DslList<>(dsl(), list);
//    //        return classes(Arrays.asList(patterns));
//    //    }
//    @Override
//    protected Object methodMissing(String name, Object arg) {
//        Object[] args = (Object[]) arg;
//        if (name.contains(".") 
//                && args != null && args.length == 1
//                && (args[0] instanceof Closure)) {
//            return new GenerateTask(name, (Closure<?>) args[0]);
//        }
//        return super.methodMissing(name, arg); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public Api4JConfiguration getGenerator() {
//        return g;
//    }
//
//    public Templates getTemplates() {
//        return templates;
//    }
//    
//    public String getDefaultClassName() {
//        return GeneratorUtils.classNameForPath(uri);
//    }
//    
//    @Override
//    public GroovyDsl dsl() {
//        return dsl;
//    }
//    
//    public Object configure(Closure<?> c) {
//        return DslUtils.configure(dsl(), this, c);
//    }
//}
//>>>>>>> ab04573103cb1c0605321368b157bdb1e583e6ff
