package org.cthul.api4j.api;

import org.cthul.api4j.Api4JConfiguration;

public class ScriptContext {
    
    private final String uri;
    private final Api4JConfiguration cfg;
    private final Templates templates;

    public ScriptContext(Api4JConfiguration cfg) {
        this.uri = "";
        this.cfg = cfg;
        this.templates = new Templates();
    }

    public ScriptContext(ScriptContext parent, String uri) {
        this.uri = uri;
        this.cfg = parent.cfg;
        this.templates = new Templates(parent.templates);
    }
    
//    public ScriptContext(ScriptContext parent, String uri, Templates templates) {
//        this.uri = uri;
//        this.cfg = parent.cfg;
//        this.templates = templates;
//    }
    
    public ScriptContext subcontext(String uri) {
        return new ScriptContext(this, uri);
    }
    
    public String getUri() {
        return uri;
    }

    public Api4JConfiguration getConfiguration() {
        return cfg;
    }

    public Templates getTemplates() {
        return templates;
    }
}
