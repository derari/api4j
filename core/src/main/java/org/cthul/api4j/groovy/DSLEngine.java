package org.cthul.api4j.groovy;

import groovy.lang.Binding;
import groovy.util.DelegatingScript;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceConnector;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.cthul.resolve.ResourceResolver;

public class DSLEngine {
    
    private final GroovyScriptEngine engine;

    public DSLEngine(ResourceResolver res) {
        ResourceConnector rc = new ScriptConnector(res);
        engine = new GroovyScriptEngine(rc);
        engine.getConfig().setScriptBaseClass(DelegatingScript.class.getName());
    }
    
    protected Object run(Object delegate, String scriptName) {
        try {
            DelegatingScript ds = (DelegatingScript)engine.createScript(scriptName, new Binding());
            ds.setDelegate(delegate);
            return ds.run();
        } catch (ResourceException | ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    protected GroovyScriptEngine getEngine() {
        return engine;
    }
}
