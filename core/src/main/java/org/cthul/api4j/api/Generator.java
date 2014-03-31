package org.cthul.api4j.api;

import com.thoughtworks.qdox.JavaDocBuilder;
import freemarker.template.Configuration;
import groovy.lang.Closure;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import org.cthul.api4j.fm.FmTemplate;
import org.cthul.api4j.fm.FmTemplateLoader;
import org.cthul.api4j.gen.ClassGenerator;
import org.cthul.api4j.groovy.DslUtils;
import org.cthul.api4j.groovy.GroovyDsl;
import org.cthul.resolve.ResourceResolver;

public class Generator {
    
    private final File out;
    private final JavaDocBuilder qdox;
    private final ScriptFinder scriptFinder;
    private final Configuration fmConfig;
    private final Templates templates = new Templates();

    @SuppressWarnings({"LeakingThisInConstructor", "OverridableMethodCallInConstructor"})
    public Generator(File out, ResourceResolver resolver) {
        this.out = out;
        this.qdox = new JavaDocBuilder();
        this.scriptFinder = new ScriptFinder(this, resolver);
        fmConfig = new Configuration();
        fmConfig.setTemplateLoader(new FmTemplateLoader(resolver));
        fmConfig.setDefaultEncoding("UTF-8");
        fmConfig.setLocalizedLookup(false);
        fmConfig.addAutoInclude("org/cthul/api4j/api1/lib.ftl");
    }
    
    public void runScript(File f) {
        scriptFinder.resolve(f).run();
    }
    
    public void runScript(String s) {
        scriptFinder.resolve(s).run();
    }
    
    public void asd(File f) {
        Path p = null;
        p.getFileSystem().getPathMatcher("**");
    }
    
    public void addSourceTree(File f) {
        qdox.addSourceTree(f);
    }

    public JavaDocBuilder getQdox() {
        return qdox;
    }
    
    public ClassGenerator generateClass(GroovyDsl dsl, String name) {
        return new ClassGenerator(dsl, out, name);
    }
    
    public Object generateClass(GroovyDsl dsl, String name, Closure<?> c) throws IOException {
        try (ClassGenerator cg = generateClass(dsl, name)) {
            return DslUtils.configure(dsl, cg, c);
        }
    }

    public Templates getTemplates() {
        return templates;
    }
    
    public Template fmTemplate(String name) {
        try {
            return new FmTemplate(fmConfig.getTemplate(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
