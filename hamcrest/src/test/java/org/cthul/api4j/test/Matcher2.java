package org.cthul.api4j.test;

import java.util.List;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public class Matcher2<T> implements Matcher<T> {
    
    /**
     * matches and does stuff.
     * @param <T>
     * @param <X>
     * @param t
     * @param x
     * @return 
     */
    @Factory
    public static <T, X> Matcher2<T> matcher2(T t, List<? super X> x) {
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