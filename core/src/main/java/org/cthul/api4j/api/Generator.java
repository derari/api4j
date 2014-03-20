package org.cthul.api4j.api;

import com.thoughtworks.qdox.JavaDocBuilder;
import java.io.File;
import org.cthul.api4j.api.ScriptFinder;
import org.cthul.resolve.ResourceResolver;

public class Generator {
    
    private final JavaDocBuilder qdox;
    private final ScriptFinder scriptFinder;

    public Generator(ResourceResolver resolver) {
        qdox = new JavaDocBuilder();
        scriptFinder = new ScriptFinder(this, resolver);
    }
    
    public void runScript(File f) {
        scriptFinder.resolve(f).run();
    }
    
    public void runScript(String s) {
        scriptFinder.resolve(s).run();
    }
    
    public void addSourceTree(File f) {
        qdox.addSourceTree(f);
    }

    public JavaDocBuilder getQdox() {
        return qdox;
    }
    
}
