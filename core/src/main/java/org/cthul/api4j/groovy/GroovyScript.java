package org.cthul.api4j.groovy;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.DelegatingScript;
import java.io.IOException;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.cthul.api4j.api1.QdoxExt;

public abstract class GroovyScript {
    
    protected CompilerConfiguration getCompilerConfiguration() {
        return DefaultConfig.CFG;
    }
    
    private DelegatingScript getScript() throws CompilationFailedException, IOException {
        CompilerConfiguration cfg = getCompilerConfiguration();
        GroovyShell shell = new GroovyShell(cfg);
        return (DelegatingScript) parseScript(shell);
    }
    
    protected void run(Object delegatee) {
        try {
            DelegatingScript script = getScript();
            script.setDelegate(delegatee);
            script.run();
        } catch (CompilationFailedException | IOException ex) {
            throw new RuntimeException(ex);
        }       
    }
    
    protected abstract Script parseScript(GroovyShell shell) throws CompilationFailedException, IOException;
    
    static class DefaultConfig {
        static final CompilerConfiguration CFG = new CompilerConfiguration();
        static {
            CFG.setScriptBaseClass(DelegatingScript.class.getName());
            CFG.getScriptExtensions().add(QdoxExt.class.getCanonicalName());
        }
    }
}
