package org.cthul.api4j;

import com.thoughtworks.qdox.JavaProjectBuilder;
import freemarker.template.Configuration;
import java.io.File;
import java.io.IOException;
import org.cthul.api4j.api.*;
import org.cthul.api4j.api1.Api1;
import org.cthul.api4j.fm.FmTemplate;
import org.cthul.api4j.fm.FmTemplateLoader;
import org.cthul.api4j.gen.GeneratedClass;
import org.cthul.api4j.xml.XmlLoader;
import org.cthul.resolve.ClassLoaderResourceResolver;
import org.cthul.resolve.CompositeResolver;
import org.cthul.resolve.ResourceResolver;

/**
 * Entry point for using the Api4J framework.
 * Create an {@linkplain #createEngine() engine} to run script files,
 * {@linkplain #createClass(java.lang.String) create classes manually}, or
 * use it to initialize the {@linkplain Api1 extended api}.
 */
public class Api4JConfiguration {
    
    private final File out;
    private final JavaProjectBuilder qdox;
    private final ResourceResolver resolver;
    private final Configuration fmConfig;
    private final XmlLoader xmlLoader;
    private final Api4JScriptContext rootContext = new Api4JScriptContext(this, null);

    /**
     * Creates a new Api4J configuration.
     * @param out output directory
     * @param resolver resolver for freemarker templates and script files
     */
    @SuppressWarnings({"LeakingThisInConstructor", "OverridableMethodCallInConstructor"})
    public Api4JConfiguration(File out, ResourceResolver resolver) {
        resolver = CompositeResolver.all(
                resolver, 
                new ClassLoaderResourceResolver(getClass().getClassLoader())
                        .addDomain(""));
        this.out = out;
        this.qdox = new JavaProjectBuilder();
        this.resolver = resolver;
        fmConfig = new Configuration();
        fmConfig.setTemplateLoader(new FmTemplateLoader(resolver));
        fmConfig.setDefaultEncoding("UTF-8");
        fmConfig.setLocalizedLookup(false);
        fmConfig.addAutoInclude("org/cthul/api4j/api1/lib.ftl");
        xmlLoader = new XmlLoader(this);
    }
    
    public Api4JEngine createEngine() {
        return new Api4JEngine(this);
    }
    
    public ResourceResolver getResourceResolver() {
        return resolver;
    }
    
    /**
     * Adds a source folder.
     * @param f 
     */
    public void addSourceFolder(File f) {
        qdox.addSourceTree(f);
//        qdox.addSourceFolder(f);
    }

    public JavaProjectBuilder getQdox() {
        return qdox;
    }

    /**
     * Returns the root script context
     * @return root script context
     */
    public Api4JScriptContext getRootContext() {
        return rootContext;
    }
    
    /**
     * Returns a script context with a script uri.
     * This can be used to configure a default package or class name, e.g.
     * {@code getContext("my/package/MyClass.some.ext")}.
     * Everything before the last slash is considered package, 
     * everything before the first dot after the last slash is considered class name.
     * @param uri
     * @return script context
     */
    public Api4JScriptContext getContext(String uri) {
        return getRootContext().subcontext(uri);
    }

    public Api4JScriptContext getContext(String uri) {
        return getRootContext().subcontext(uri);
    }

    /**
     * Returns the loader that executes xml configurations.
     * @return xml loader
     */
    public XmlLoader getXmlLoader() {
        return xmlLoader;
    }

    /**
     * Creates a new class generator. 
     * Notice that it must be closed for the file to be created.
     * @param name
     * @return generated class
     */
    public GeneratedClass createClass(String name) {
        File f = new File(out, name.replace('.', '/') + ".java");
        return new GeneratedClass(getQdox(), f, name.replace('/', '.').replace('\\', '.'));
    }
    
    /**
     * Creates a template.
     * @param name
     * @return template
     */
    public Template fmTemplate(String name) {
        try {
            return new FmTemplate(fmConfig.getTemplate(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}