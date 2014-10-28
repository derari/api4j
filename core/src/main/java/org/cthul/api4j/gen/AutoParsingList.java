package org.cthul.api4j.gen;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class AutoParsingList<E> extends AbstractList<E> {
    
    private final List<E> list = new ArrayList<>();
    
    protected abstract E parse(String s);

    @Override
    public void add(int index, E e) {
        if (e instanceof Object[]) {
            e = (E) Arrays.asList((Object[]) e);
        }
        if (e instanceof Collection) {
            addCollection(index, (Collection) e);
            return;
        }
        if (e instanceof String) {
            e = parse((String) e);
        }
        plainAdd(index, e);
    }
    
    protected void plainAdd(int index, E element) {
        list.add(index, element);
    }
    
    protected boolean addCollection(int index, Collection<?> c) {
        return addAll(index, (Collection) c);
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }    
}
