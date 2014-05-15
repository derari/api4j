package org.example;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class ComplexInstanceBuilderTest {
    
    @Test
    public void test() {
        ComplexInstanceBuilder builder = new ComplexInstanceBuilder();
        ComplexInstance instance = builder
                .name("name").id(42)
                .addDatum("dat1")
                .addDatum("dat2")
                .build();
        assertThat(instance.getName(), is("name"));
        assertThat(instance.getId(), is(42));
        assertThat(instance.getData(), contains("dat1", "dat2"));
    }
}