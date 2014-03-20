package org.cthul.api4j.api;

import org.cthul.api4j.api.PatternSearcher;
import java.util.regex.Pattern;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class PatternSearcherTest {
    
    @Test
    public void testCompile() {
        Pattern p = PatternSearcher.compile("..*Test");
        assertThat(p.matcher("FooTest").matches(), is(true));
        assertThat(p.matcher("bar.FooTest").matches(), is(true));
        assertThat(p.matcher("bar.baz.FooTest").matches(), is(true));
        assertThat(p.matcher("FooTest.baz").matches(), is(false));
    }
    
    @Test
    public void testCompile2() {
        Pattern p = PatternSearcher.compile("bar..*");
        assertThat(p.matcher("FooTest").matches(), is(false));
        assertThat(p.matcher("bar.FooTest").matches(), is(true));
        assertThat(p.matcher("bar.baz.FooTest").matches(), is(true));
        assertThat(p.matcher("barFooTest").matches(), is(false));
        assertThat(p.matcher("FooTest.bar").matches(), is(false));
    }

    @Test
    public void testCompile3() {
        Pattern p = PatternSearcher.compile("bar.*");
        assertThat(p.matcher("FooTest").matches(), is(false));
        assertThat(p.matcher("bar.FooTest").matches(), is(true));
        assertThat(p.matcher("bar.baz.FooTest").matches(), is(false));
        assertThat(p.matcher("barFooTest").matches(), is(false));
        assertThat(p.matcher("FooTest.bar").matches(), is(false));
    }
    
    @Test
    public void testCompile4() {
        Pattern p = PatternSearcher.compile("bar.baz..*");
        assertThat(p.matcher("FooTest").matches(), is(false));
        assertThat(p.matcher("bar.FooTest").matches(), is(false));
        assertThat(p.matcher("bar.baz.FooTest").matches(), is(true));
        assertThat(p.matcher("bar.baz.xxyy.FooTest").matches(), is(true));
        assertThat(p.matcher("barbaz.FooTest").matches(), is(false));
        assertThat(p.matcher("barxbaz.FooTest").matches(), is(false));
    }
}