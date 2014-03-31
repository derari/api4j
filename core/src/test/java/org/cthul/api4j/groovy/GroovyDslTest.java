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
    
    public static class InstExt {
        
        private final GroovyDslTest test;

        public InstExt(GroovyDslTest test) {
            this.test = test;
        }
        
        public GroovyDslTest getTest(Object any) {
            return test;
        }
        
        public GroovyDslTest test(Object any) {
            return test;
        }
    }
     
    @Test
    @SuppressWarnings("unchecked")
    public void test_all() {
        GroovyDsl dsl = new GroovyDsl();
        dsl.getExtensions().add(Ext.class);
        
        List<Foo> foos = Arrays.asList(new Foo(), new Foo());
        DslList gFoos = (DslList) dsl.wrap(foos);
        DslList gBars = (DslList) gFoos.invokeMethod("allBars", null);
        DslList gNames = (DslList) gBars.invokeMethod("eachName", null);
        
        assertThat(gBars, hasSize(4));
        assertThat(gNames, hasSize(4));
    }
    
    @Test
    public void test_instance_ext() {
        GroovyDsl dsl = new GroovyDsl();
        dsl.getExtensions().add(InstExt.class);
        dsl.addGlobal(this);
        DslObject<?> o = (DslObject) dsl.wrap(new Object());
        Object thisTest = o.invokeMethod("getTest", null);
        assertThat(thisTest, is((Object) this));
    }
    
    @Test
    public void test_instance_property_ext() {
        GroovyDsl dsl = new GroovyDsl();
        dsl.getExtensions().add(InstExt.class);
        dsl.addGlobal(this);
        DslObject<?> o = (DslObject) dsl.wrap(new Object());
        Object thisTest = o.getProperty("test");
        assertThat(thisTest, is((Object) this));
    }
}