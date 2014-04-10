package org.cthul.api4j.api;

import groovy.lang.Closure;
import org.cthul.api4j.api1.Api1;
import org.cthul.api4j.groovy.GroovyScript;

public abstract class ApiScript extends GroovyScript {
    
    private final String uri;
    private final Generator g;

    public ApiScript(String uri, Generator g) {
        this.uri = uri;
        this.g = g;
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

    public void api(String version, Closure<?> closure) {
        new Api1(g, uri).configure(closure);
    }
}
