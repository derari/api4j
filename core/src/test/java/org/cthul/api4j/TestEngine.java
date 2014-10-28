package org.cthul.api4j;

import java.io.File;
import org.cthul.resolve.ClassResourceResolver;
import org.cthul.resolve.ResourceResolver;

public class TestEngine {
    
    public static final File target = new File("./target/testout");
    
    public static Api4JEngine testEngine() {
        ResourceResolver res = new ClassResourceResolver(TestEngine.class)
                .addDomain("test:/");
        Api4JConfiguration cfg = new Api4JConfiguration(target, res);
        cfg.getQdox().addSourceFolder(new File("./src/test/java"));
        return new Api4JEngine(cfg);
    }
    
}
