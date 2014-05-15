package org.cthul.api4j.groovy;

import groovy.lang.GroovyObjectSupport;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class DslListTest {
    
    static class Bar {}
    
    static class Foo extends GroovyObjectSupport {
        
        public List<Bar> getBars() {
            return Arrays.asList(new Bar(), new Bar());
        }
    }
     
    @Test
    public void test_all() {
        GroovyDsl dsl = new GroovyDsl();
        DslList foos = new DslList(dsl, Arrays.asList(new Foo(), new Foo()));
        Object o = foos.invokeMethod("allBars", null);
        List<Bar> bars = (List) ((DslList) o).__object();
        assertThat(bars, hasSize(4));
    }
}