package org.cthul.api4j.api1;

import org.cthul.api4j.api.Generator;
import org.cthul.api4j.api.TestGenerator;
import org.junit.Test;

public class Api1Test {
    
     
    @Test
    public void test_basic() {
        Generator g = TestGenerator.get();
        g.runScript("basic_test_1.api.groovy");
    }
}