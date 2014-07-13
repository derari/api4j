package org.cthul.api4j.api;

import java.io.File;
import org.cthul.api4j.Api4JConfiguration;

public class ScriptContext {
    
    private final String uri;
    private final File root;
    private final Api4JConfiguration cfg;
    private final Templates templates;

    public ScriptContext(Api4JConfiguration cfg, File root) {
        this.uri = "";
        this.root = root;
        this.cfg = cfg;
        this.templates = new Templates();
    }

    public ScriptContext(ScriptContext parent, String uri, File root) {
        this.uri = uri;
        this.root = root;
        this.cfg = parent.cfg;
        this.templates = new Templates(parent.templates);
    }
    
//    public ScriptContext(ScriptContext parent, String uri, Templates templates) {
//        this.uri = uri;
//        this.cfg = parent.cfg;
//        this.templates = templates;
//    }
    
    public ScriptContext newRoot(File root) {
        return new ScriptContext(this, uri, root);
    }
    
    public ScriptContext subcontext(String uri) {
        return new ScriptContext(this, uri, root);
    }

    public File getRoot() {
        return root;
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
