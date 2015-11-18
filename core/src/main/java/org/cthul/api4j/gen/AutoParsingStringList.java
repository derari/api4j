package org.cthul.api4j.gen;

import java.util.Collection;
import java.util.List;

/**
 * A list of strings that parses (and possibly splits) the input before accepts it.
 */
public abstract class AutoParsingStringList extends AutoParsingListBase<String> {

    public AutoParsingStringList() {
    }

    public AutoParsingStringList(List<String> list) {
        super(list);
    }

    /**
     * Adds each item to the list, parsing string and flattening arrays and iterables.
     * @param c
     * @return true
     */
    public boolean addAll(Iterable<?> c) {
        return smartAdd(c);
    }

    /**
     * Adds each item to the list, parsing string and flattening arrays and iterables.
     * @param o
     * @return true
     */
    public boolean addAll(Object... o) {
        return smartAdd(o);
    }
}
