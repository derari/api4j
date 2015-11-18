package org.cthul.api4j;

import com.thoughtworks.qdox.JavaProjectBuilder;
import freemarker.template.Configuration;
import java.io.File;
import java.io.IOException;
import org.cthul.api4j.api.*;
import org.cthul.api4j.fm.FmTemplate;
import org.cthul.api4j.fm.FmTemplateLoader;
import org.cthul.api4j.gen.GeneratedClass;
import org.cthul.api4j.xml.XmlLoader;
import org.cthul.resolve.ClassLoaderResourceResolver;
import org.cthul.resolve.CompositeResolver;
import org.cthul.resolve.ResourceResolver;

public class Api4JConfiguration {
    
    private final File out;
    private final JavaProjectBuilder qdox;
    private final ResourceResolver resolver;
    private final Configuration fmConfig;
    private final XmlLoader xmlLoader;
    private final Api4JScriptContext rootContext = new Api4JScriptContext(this, null);

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
    
    public void addSourceFolder(File f) {
        qdox.addSourceFolder(f);
    }

    public JavaProjectBuilder getQdox() {
        return qdox;
    }

    public Api4JScriptContext getRootContext() {
        return rootContext;
    }

    public Api4JScriptContext getContext(String uri) {
        return getRootContext().subcontext(uri);
    }

    public XmlLoader getXmlLoader() {
        return xmlLoader;
    }

    public GeneratedClass createClass(String name) {
        File f = new File(out, name.replace('.', '/') + ".java");
        return new GeneratedClass(getQdox(), f, name.replace('/', '.').replace('\\', '.'));
    }
    
    public Template fmTemplate(String name) {
        try {
            return new FmTemplate(fmConfig.getTemplate(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}