package org.cthul.test;

import org.hamcrest.Factory;

public class StaticTestClass2 {
    
    @Factory
    public static int getGlobalNumber() {
        return 4;
    }
}
