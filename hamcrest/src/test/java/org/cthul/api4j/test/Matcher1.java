package org.cthul.api4j.test;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public class Matcher1<T> implements Matcher<T> {
    
    /**
     * Creates new #1 matcher.
     * @param <T>
     * @param t
     * @return 
     */
    @Factory
    public static <T> Matcher1<T> matcher1(T t) {
        return null;
    }

    @Override
    public boolean matches(Object item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void describeMismatch(Object item, Description mismatchDescription) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void describeTo(Description description) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}