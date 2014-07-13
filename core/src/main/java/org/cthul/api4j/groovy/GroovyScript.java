package org.cthul.api4j.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.DelegatingScript;
import java.io.File;
import java.io.IOException;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;

public abstract class GroovyScript {
    
    private DelegatingScript cachedScript = null;
    private DslShell shell;
    
    protected CompilerConfiguration getCompilerConfiguration() {
        return DefaultConfig.CFG;
    }
    
    private synchronized DelegatingScript getScript() throws CompilationFailedException, IOException {
        if (cachedScript == null) {
            if (shell == null) {
                CompilerConfiguration cfg = getCompilerConfiguration();
                shell = new DslShell(cfg);
            }
            cachedScript = (DelegatingScript) parseScript(shell);
        }
        return cachedScript;
    }
    
    protected void run(Object delegatee) {
        try {
            DelegatingScript script = getScript();
            shell.setDelegatee(delegatee);
            script.setDelegate(delegatee);
            script.run();
        } catch (CompilationFailedException | IOException ex) {
            throw new RuntimeException(ex);
        }       
    }
    
    protected abstract Script parseScript(GroovyShell shell) throws CompilationFailedException, IOException;
    
    static class DslShell extends GroovyShell {
        private Object delegatee;
        public DslShell(CompilerConfiguration config) {
            super(config);
        }

        public DslShell(Object delegatee, ClassLoader parent, Binding binding, CompilerConfiguration config) {
            super(parent, binding, config);
            this.delegatee = delegatee;
        }

        public void setDelegatee(Object delegatee) {
            this.delegatee = delegatee;
        }

        @Override
        public Script parse(GroovyCodeSource codeSource) throws CompilationFailedException {
            DelegatingScript s = (DelegatingScript) super.parse(codeSource);
            if (delegatee != null) s.setDelegate(delegatee);
            return s;
        }
    }
    
    public static abstract class MyDelegatingScript extends DelegatingScript {

        public MyDelegatingScript() {
        }

        public MyDelegatingScript(Binding binding) {
            super(binding);
        }

        @Override
        public Object evaluate(File file) throws CompilationFailedException, IOException {
            GroovyShell shell = new DslShell(getDelegate(), getClass().getClassLoader(), getBinding(), DefaultConfig.CFG);
            return shell.evaluate(file);
        }
    }
    
    static class DefaultConfig {
        static final CompilerConfiguration CFG = new CompilerConfiguration();
        static {
            CFG.setScriptBaseClass(MyDelegatingScript.class.getName());
            //CFG.getScriptExtensions().add(QdoxExt.class.getCanonicalName());
        }
    }
}
