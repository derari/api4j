package org.cthul.api4j.api;

import org.cthul.api4j.Api4JConfiguration;
import groovy.lang.*;
import java.io.File;
import java.io.IOException;
import org.codehaus.groovy.control.CompilationFailedException;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ApiScriptTest {
    
    public ApiScriptTest() {
    }
    
    boolean success = false;
    
    @Test
    public void test_run() {
        Api4JConfiguration g = new Api4JConfiguration(new File("target"), null);
        ApiScript script = new TestApiScript(g.getRootContext());
        script.run();
        assertThat(success, is(true));
    }
    
    class TestApiScript extends ApiScript {

        public TestApiScript(ScriptContext ctx) {
            super("test", ctx);
        }

        @Override
        public Object api(String version, Closure<?> closure) {
            success = true;
            //super.api(version, closure);
            return null;
        }

        @Override
        protected ApiScript findScript(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Script parseScript(GroovyShell shell) throws CompilationFailedException, IOException {
            return shell.parse(new File("src/test/api/apiScriptTest/run.api.groovy"));
        }        
    }
}