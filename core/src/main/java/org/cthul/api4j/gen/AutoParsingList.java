package org.cthul.api4j.gen;

import java.util.Collection;
import java.util.List;

public abstract class AutoParsingList<E> extends AutoParsingListBase<E> {

    public AutoParsingList() {
    }

    public AutoParsingList(List<E> list) {
        super(list);
    }

    public boolean add(String e) {
        return smartAdd(e);
    }

    public boolean addAll(Iterable<?> c) {
        return smartAdd(c);
    }

    public boolean addAll(Object[] o) {
        return smartAdd(o);
    }
    
    public boolean addAll(String... s) {
        return smartAdd(s);
    }
}
