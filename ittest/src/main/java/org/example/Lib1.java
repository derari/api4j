package org.example;

public class Lib1 {
    
    @Factory
    public static <X> X cast(Object o) {
        return (X) o;
    }
    
}
