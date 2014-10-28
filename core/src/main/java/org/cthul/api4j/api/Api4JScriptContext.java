package org.cthul.api4j.api;

import groovy.lang.Closure;
import java.net.URI;
import org.cthul.api4j.Api4JConfiguration;
import org.cthul.api4j.Api4JEngine;
import org.cthul.api4j.api1.Api1;

public class Api4JScriptContext {
    
    private final String uri;
    private final URI root;
    private final Api4JEngine engine;
    private final Api4JConfiguration cfg;
    private final Templates templates;
    private boolean recreate = true;

    public Api4JScriptContext(Api4JEngine engine, URI root) {
        this.uri = "";
        this.root = root;
        this.engine = engine;
        this.cfg = engine.getConfiguration();
        this.templates = new Templates();
    }
    
    public Api4JScriptContext(Api4JConfiguration cfg, URI root) {
        this.uri = "";
        this.root = root;
        this.engine = null;
        this.cfg = cfg;
        this.templates = new Templates();
    }

    public Api4JScriptContext(Api4JScriptContext parent, Api4JEngine engine, String uri, URI root) {
        this.uri = uri;
        this.root = root;
        this.engine = engine;
        this.cfg = engine != null ? engine.getConfiguration() : parent.getConfiguration();
        this.templates = new Templates(parent.templates);
        this.recreate = parent.recreate;
    }
    
//    public ScriptContext(ScriptContext parent, String uri, Templates templates) {
//        this.uri = uri;
//        this.cfg = parent.cfg;
//        this.templates = templates;
//    }
    public void setRecreate(boolean recreate) {
        this.recreate = recreate;
    }

    public boolean isRecreate() {
        return recreate;
    }
    
    public Api4JScriptContext newContext(Api4JEngine engine, URI root, String scriptUri) {
        return new Api4JScriptContext(this, engine, scriptUri, root);
    }
//    
//    public Api4JScriptContext newRoot(URI root) {
//        return new Api4JScriptContext(this, uri, root);
//    }
    
    public Api4JScriptContext subcontext(String uri) {
        return newContext(engine, root, uri);
    }

    public URI getRoot() {
        return root;
    }
    
    public String getUri() {
        return uri;
    }

    public Api4JEngine getEngine() {
        return engine;
    }

    public Api4JConfiguration getConfiguration() {
        return cfg;
    }

    public Templates getTemplates() {
        return templates;
    }
    
    public <T> T api1(Closure<T> c) {
        return new Api1(this).configure(c);
    }
    
    public <T> T api(String version, Closure<T> c) {
        if (version.equals("1.0") || version.equals("1")) {
            return api1(c);
        }
        throw new IllegalArgumentException(
                    "Unsupported version: " + version);
    }
}
