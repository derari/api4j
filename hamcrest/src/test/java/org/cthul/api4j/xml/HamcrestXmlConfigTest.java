package org.cthul.api4j.xml;

import java.io.File;
import org.cthul.api4j.api.Generator;
import org.cthul.api4j.test.TestGenerator;
import org.junit.Test;

public class HamcrestXmlConfigTest {
    
    @Test
    public void test_generate() throws Exception {
        File f = new File("src/test/api/example/MyMatchers.xml");
        Generator g = TestGenerator.getWithSource();
        XmlLoader xmlLoader = new XmlLoader(g);
        xmlLoader.load("example/MyMatchers", f);
    }
    
}
