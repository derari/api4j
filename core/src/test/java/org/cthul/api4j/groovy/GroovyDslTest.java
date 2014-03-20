package org.cthul.api4j.groovy;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class GroovyDslTest {
    
    public static class Bar {}
    
    public static class Foo {
        
        public List<Bar> getBars() {
            return Arrays.asList(new Bar(), new Bar());
        }
    }
    
    public static class Ext {
        public static String getName(Bar bar) {
            return "bar";
        }
    }
     
    @Test
    @SuppressWarnings("unchecked")
    public void test_all() {
        GroovyDsl dsl = new GroovyDsl();
        dsl.getExtensions().add(Ext.class);
        
        List<Foo> foos = Arrays.asList(new Foo(), new Foo());
        DslList<Foo> gFoos = (DslList) dsl.wrap(foos);
        DslList<Bar> gBars = (DslList) gFoos.invokeMethod("allBars", null);
        DslList<String> gNames = (DslList) gBars.invokeMethod("eachName", null);
        
        assertThat(gBars, hasSize(4));
        assertThat(gNames, hasSize(4));
    }
}