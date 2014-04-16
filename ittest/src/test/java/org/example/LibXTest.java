package org.example;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class LibXTest {
    
    @Test
    public void test() {
        assertThat(LibX.theAnswer(), is(42));
    }
}