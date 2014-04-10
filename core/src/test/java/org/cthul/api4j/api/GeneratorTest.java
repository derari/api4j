package org.cthul.api4j.api;

import java.nio.file.Paths;
import org.junit.Test;

public class GeneratorTest {
    
    public GeneratorTest() {
    }
        
    @Test
    public void test_runFileTree() throws Exception {
        try {
            Generator g = TestGenerator.getWithSource();
            g.runFileTree(Paths.get("src/test/api"), "root2/**.api.groovy");
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw e;
        }
    }
}