package org.cthul.api4j.api;

import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.io.File;
import java.io.IOException;
import org.codehaus.groovy.control.CompilationFailedException;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class ApiScriptTest {
    
    public ApiScriptTest() {
    }
    
    boolean success = false;
    
    @Test
    public void testRun() {
        Generator g = new Generator(null);
        ApiScript script = new TestApiScript(g);
        script.run();
        assertThat(success, is(true));
    }
    
    class TestApiScript extends ApiScript {

        public TestApiScript(Generator g) {
            super("test", g);
        }

        @Override
        public void api(String version, Closure<?> closure) {
            success = true;
            super.api(version, closure);
        }

        @Override
        protected ApiScript findScript(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Script parseScript(GroovyShell shell) throws CompilationFailedException, IOException {
            return shell.parse(new File("src/test/api/test.api.groovy"));
        }        
    }
}