package org.cthul.api4j.test;

public class StaticTestClass2 {
    
    @AtSource
    @AtRuntime
    public static int getGlobalNumber() {
        return 4;
    }
    
    @AtSource
    @AtRuntime
    public static void doTheThing() {
        
    }
}
