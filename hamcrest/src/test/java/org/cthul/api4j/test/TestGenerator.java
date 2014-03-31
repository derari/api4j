package org.cthul.api4j.test;

import java.io.File;
import org.cthul.api4j.api.Generator;
import org.cthul.resolve.ClassResourceResolver;
import org.cthul.resolve.CompositeResolver;
import org.cthul.resolve.FileResolver;
import org.cthul.resolve.ResourceResolver;

public class TestGenerator {

    public static Generator get() {
        File base = new File("src/test/api");
        ResourceResolver res = new CompositeResolver(
                new ClassResourceResolver(Generator.class).addDomain("", "/$1"),
                new FileResolver(base, base).addDomain(""));
        Generator g = new Generator(new File("target/test-out"),res);
        return g;
    }
    
    public static Generator getWithSource() {
        Generator g = get();
        g.getQdox().addSourceTree(new File("src/test/java"));
        return g;
    }
    
}
