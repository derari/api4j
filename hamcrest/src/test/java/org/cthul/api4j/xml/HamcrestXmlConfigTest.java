package org.cthul.api4j.xml;

import java.io.File;
import org.cthul.api4j.Api4JConfiguration;
import org.cthul.api4j.test.TestConfiguration;
import org.junit.Test;

public class HamcrestXmlConfigTest {
    
    @Test
    public void test_generate() throws Exception {
        File f = new File("src/test/api/example/MyMatchers.xml");
        Api4JConfiguration cfg = TestConfiguration.getWithSource();
        XmlLoader xmlLoader = new XmlLoader(cfg);
        xmlLoader.load("example/MyMatchers", f);
    }
    
}
