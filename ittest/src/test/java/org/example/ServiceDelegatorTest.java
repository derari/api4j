package org.example;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class ServiceDelegatorTest {
    
    @Test
    public void test() {
        Service<String> myService = new Service<String>() {
            @Override
            public int process(String data) {
                return data.length();
            }
        };
        Service<String> myDelegator = new ServiceDelegator<>(myService);

        assertThat(myDelegator.process("hello"), is(5));
    }
}