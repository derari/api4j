package org.cthul.api4j.api;

import groovy.lang.Closure;
import org.cthul.api4j.api1.Api1;
import org.cthul.api4j.groovy.GroovyScript;

public abstract class ApiScript extends GroovyScript {
    
    private final String uri;
    private final ScriptContext ctx;

    public ApiScript(String uri, ScriptContext ctx) {
        this.uri = uri;
        this.ctx = ctx;
    }

    public String getUri() {
        return uri;
    }
    
    public void run() {
        run(this);
    }

    @Override
    public void run(Object delegatee) {
        super.run(delegatee);
    }

    public void include(String s) {
        findScript(s).run();
    }
    
    public void include(Object o, String s) {
        findScript(s).run(o);
    }
    
    protected abstract ApiScript findScript(String s);

    public Object api(String version, Closure<?> closure) {
        return api1(closure);
    }
    
    public Api1 api1() {
        return new Api1(ctx);
    }
    
    public Object api1(Closure<?> closure) {
        return api1().configure(closure);
    }
}
