package org.cthul.api4j.groovy;

import groovy.lang.GroovyObjectSupport;
import groovy.lang.GroovyShell;
import groovy.lang.MetaClass;
import groovy.lang.Script;
import groovy.util.DelegatingScript;
import java.io.IOException;
import java.util.Iterator;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.cthul.api4j.api1.QdoxExt;

public abstract class GroovyScript {
    
    private DelegatingScript cachedScript = null;
    
    protected CompilerConfiguration getCompilerConfiguration() {
        return DefaultConfig.CFG;
    }
    
    private synchronized DelegatingScript getScript() throws CompilationFailedException, IOException {
        if (cachedScript == null) {
            CompilerConfiguration cfg = getCompilerConfiguration();
            GroovyShell shell = new GroovyShell(cfg);
            cachedScript = (DelegatingScript) parseScript(shell);
        }
        return cachedScript;
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
