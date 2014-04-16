package org.cthul.api4j.test;

public interface MyService {
    
    /**
     * Does the stuff.
     * @param repeat 
     */
    void doStuff(int repeat);
    
    <A> A fetchThing(Class<A> clazz); 
}
