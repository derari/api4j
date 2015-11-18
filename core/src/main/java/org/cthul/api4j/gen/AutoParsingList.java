package org.cthul.api4j.gen;

import java.util.List;

/**
 * A list of elements that automatically parses strings.
 * @param <E> 
 */
public abstract class AutoParsingList<E> extends AutoParsingListBase<E> {

    public AutoParsingList() {
    }

    public AutoParsingList(List<E> list) {
        super(list);
    }

    /**
     * Parses a string and adds the result to the list.
     * @param e
     * @return true
     */
    public boolean add(String e) {
        return smartAdd(e);
    }

    /**
     * Adds each item to the list, parsing strings and flattening arrays and iterables.
     * @param c
     * @return true
     */
    public boolean addAll(Iterable<?> c) {
        return smartAdd(c);
    }

    /**
     * Adds each item to the list, parsing strings and flattening arrays and iterables.
     * @param o
     * @return true
     */
    public boolean addAll(Object... o) {
        return smartAdd(o);
    }
    
    /**
     * Adds each item to the list, parsing each string.
     * @param s
     * @return 
     */
    public boolean addAll(String... s) {
        return smartAdd(s);
    }
}
