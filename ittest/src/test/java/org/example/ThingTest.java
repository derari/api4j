package org.example;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class ThingTest {
    
    @Test
    public void test() {
        Thing thing = new Thing("key", "value");
        thing.write("key2", "value2");
        assertThat(thing.read("key"), is("value"));
        assertThat(thing.read("key2"), is("value2"));
    }
}