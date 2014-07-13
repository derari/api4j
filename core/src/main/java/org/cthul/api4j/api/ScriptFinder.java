package org.cthul.api4j.api;

import org.cthul.api4j.Api4JConfiguration;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.io.File;
import java.io.IOException;
import org.codehaus.groovy.control.CompilationFailedException;
import org.cthul.resolve.*;

public class ScriptFinder extends ObjectResolver<ApiScript, RuntimeException> {

    private final Api4JConfiguration g;

    public ScriptFinder(Api4JConfiguration g, ResourceResolver resolver) {
        super(resolver);
        this.g = g;
    }

    public ScriptFinder(Api4JConfiguration g, ResourceResolver... resolver) {
        super(resolver);
        this.g = g;
    }
    
    public ApiScript resolve(File f) {
        return resolve(f.toURI().toString());
    }

    @Override
    public ApiScript resolve(String uri) throws RuntimeException {
        return super.resolve(uri.replace('\\', '/'));
    }
    
    @Override
    protected ApiScript result(RResult result) throws RuntimeException {
        return new ResolvedScript(result, g);
    }
    
    protected class ResolvedScript extends ApiScript {
        
        private final RResult result;

        public ResolvedScript(RResult result, Api4JConfiguration g) {
            super(result.getUri().replace('\\', '/'), 
                    g.getRootContext().subcontext(result.getUri().replace('\\', '/')));
            this.result = result;
        }

        @Override
        protected Script parseScript(GroovyShell shell) throws CompilationFailedException, IOException {
            result.setDefaultEncoding(getCompilerConfiguration().getSourceEncoding());
            return shell.parse(result.asReader());
        }

        @Override
        protected ApiScript findScript(String s) {
            return resolve(s, getUri());
        }
    }
}
