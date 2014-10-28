package org.cthul.api4j.api;

import com.thoughtworks.qdox.model.JavaConstructor;
import org.cthul.api4j.Api4JConfiguration;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.cthul.resolve.ResourceResolver;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class Api4JConfigurationTest {
    
    public Api4JConfigurationTest() {
    }
        
    @Test
    public void test_runFileTree() throws Exception {
//        try {
//            TestApi4JConfiguration cfg = new TestApi4JConfiguration();
//            cfg.runFileTree(Paths.get("src/test/api"), "api4JConfigurationTest/**.api.groovy");
//            assertThat(cfg.files, containsInAnyOrder(
//                    "api4JConfigurationTest/test1.api.groovy", 
//                    "api4JConfigurationTest/foo/test2.api.groovy"));
//        } catch (Exception e) {
//            e.printStackTrace(System.err);
//            throw e;
//        }
    }
//    
//    public static class TestApi4JConfiguration extends Api4JConfiguration {
//
//        final List<String> files = new ArrayList<>();
//        
//        public TestApi4JConfiguration() {
//            this(TestConfiguration.testOut(), TestConfiguration.testResolver());
//            TestConfiguration.addSource(this);
//        }
//
//        public TestApi4JConfiguration(File out, ResourceResolver resolver) {
//            super(out, resolver);
//        }
//
//        @Override
//        public void runScript(File root, String f) {
//            files.add(f.replace('\\', '/'));
//        }
//    }

}