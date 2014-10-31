package org.cthul.api4j.test;

import groovy.lang.Reference;
import java.util.List;
import java.util.Map;

public class StaticTestClass1 {
    
    /**
     * Fetches the global key.
     * Actually, returns the argument.
     * @param foo the foo
     * @return global key
     */
    @AtSource
    @AtRuntime
    public static String getGlobalKey(String foo) {
        return foo;
    }
    
    /**
     * Just to test generics.
     * @param <A>
     * @param <B> the specific type
     * @param map
     * @return what
     */
    @AtSource
    @AtRuntime
    public static <A, B extends A> A generic(Map<B, A> map) {
        return null;
    }
    
    @AtSource
    @AtRuntime
    public static <A> List<A>[] allTheLists(List<Reference<? super A>> src, int count) {
        return null;
    }
    
    @AtSource
    @AtRuntime
    public static <X> void x(X x) { }
    
    @AtSource
    @AtRuntime
    public static <R extends Runnable> void r(R r) { }
    
    @AtSource
    @AtRuntime
    public static <L extends List & Runnable> void l(L l) { }
}
