package org.example;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class LibGTest {
    
    @Test
    public void test() {
        assertThat(LibG.theAnswer(), is(42));
//        assertThat(LibG.map())
    }
}