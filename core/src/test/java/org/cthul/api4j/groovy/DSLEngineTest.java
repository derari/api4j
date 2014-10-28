package org.cthul.api4j.groovy;

import groovy.lang.Closure;
import groovy.lang.MissingMethodException;
import org.codehaus.groovy.runtime.GroovyCategorySupport;
import org.cthul.resolve.ClassResourceResolver;
import org.cthul.resolve.ResourceResolver;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class DSLEngineTest {
    
    public class TestEngine extends DSLEngine {
        public TestEngine(ResourceResolver res) {
            super(res);
        }

        public Object runTest(String scriptName) {
            return run(testApi, scriptName);
        }
    }
    
    TestApi testApi = new TestApi();
    
    public TestEngine testEngine() {
        ResourceResolver rs = new ClassResourceResolver(DSLEngineTest.class)
                .addDomain("test:")
                .addDomain("", "/$1");
        return new TestEngine(rs);
    }
    
//    @Test
//    public void test_extension() throws Exception {
//        TestEngine engine = testEngine();
//        engine.runTest("test:engineTest1");
//        assertThat(testApi.check, is(1));
//    }
    
    @Test
    public void test_run1() throws Exception {
        TestEngine engine = testEngine();
        engine.runTest("test:engineTest1.api.groovy");
        assertThat(testApi.check, is(1));
    }
    
    @Test
    public void test_run2() throws Exception {
        TestEngine engine = testEngine();
        engine.runTest("test:engineTest2.api.groovy");
        assertThat(testApi.check, is(2));
    }
    
    @Test
    public void test_run3() throws Exception {
        try {
            TestEngine engine = testEngine();
            engine.runTest("test:engineTest3.api.groovy");
            assertThat("methodmissing", false);
        } catch (MissingMethodException mme) {
            assertThat(mme.getMessage(), containsString("blink2()"));
        }
        assertThat(testApi.check, is(2));
    }
    
    @Test
    public void test_include() throws Exception {
        TestEngine engine = testEngine();
        engine.runTest("test:includeTest.api.groovy");
        assertThat("passes without error", true);
    }
    
    public static class TestApi {
        
        public int check;
        
        public void blink() {
            check++;
        }
        
        public Object api2(Closure<?> c) {
            return GroovyCategorySupport.use(TestApi2.class, c);
        }
    }
    
    public static class TestApi2 {
        
        public static void blink2(TestApi api) {
            api.blink();
            api.blink();
        }
    }
}
