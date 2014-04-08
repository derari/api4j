package org.cthul.api4j.api;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.io.File;
import java.io.IOException;
import org.codehaus.groovy.control.CompilationFailedException;
import org.cthul.resolve.ObjectResolver;
import org.cthul.resolve.RResult;
import org.cthul.resolve.ResourceResolver;

public class ScriptFinder extends ObjectResolver<ApiScript, RuntimeException> {

    private final Generator g;

    public ScriptFinder(Generator g, ResourceResolver resolver) {
        super(resolver);
        this.g = g;
    }

    public ScriptFinder(Generator g, ResourceResolver... resolver) {
        super(resolver);
        this.g = g;
    }
    
    public ApiScript resolve(File f) {
        return resolve(f.toURI().toString());
    }

    @Override
    public ApiScript resolve(String uri) throws RuntimeException {
        return super.resolve(uri);
    }
    
    @Override
    protected ApiScript result(RResult result) throws RuntimeException {
        return new ResolvedScript(result, g);
    }
    
    protected class ResolvedScript extends ApiScript {
        
        private final RResult result;

        public ResolvedScript(RResult result, Generator g) {
            super(result.getUri(), g);
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
