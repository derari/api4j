package org.cthul.api4j.gen;

import java.util.Collection;
import java.util.List;

public abstract class AutoParsingStringList extends AutoParsingListBase<String> {

    public AutoParsingStringList() {
    }

    public AutoParsingStringList(List<String> list) {
        super(list);
    }

    public boolean add(Collection<?> c) {
        return smartAdd(c);
    }

    public boolean add(Object[] o) {
        return smartAdd(o);
    }
}
