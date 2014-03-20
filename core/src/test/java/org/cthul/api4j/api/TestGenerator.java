package org.cthul.api4j.api;

import java.io.File;
import org.cthul.resolve.CompositeResolver;
import org.cthul.resolve.FileResolver;
import org.cthul.resolve.ResourceResolver;

public class TestGenerator {

    public static Generator get() {
        File base = new File("src/test/api");
        ResourceResolver res = new CompositeResolver(
                new FileResolver(base, base).addDomain(""));
        Generator g = new Generator(res);
        return g;
    }
    
}
