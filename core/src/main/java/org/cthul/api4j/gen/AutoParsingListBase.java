package org.cthul.api4j.gen;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * A list of elements that automatically parses strings.
 * @param <E> item type
 */
public abstract class AutoParsingListBase<E> extends AbstractList<E> {
    
    public static <E, L extends AutoParsingListBase<E>> L wrap(List<E> list, Function<List<E>, L> wrapper) {
//        if (list instanceof AutoParsingListBase) {
//            return (L) list;
//        }
        return wrapper.apply(list);
    }
    
    private final List<E> list;
    private Object initial = null;

    public AutoParsingListBase() {
        list = new ArrayList<>();
    }
    
    public AutoParsingListBase(List<E> list) {
        this.list = list;
    }
    
    protected abstract E parse(String s);

    @Override
    public void add(int index, E element) {
        smartAdd(index, element);
    }

    protected boolean smartAdd(Object o) {
        return smartAdd(size(), o);
    }
    
    protected boolean smartAdd(int index, Object o) {
        E e;
        if (o instanceof Object[]) {
            o = Arrays.asList((Object[]) o);
        }
        if (o instanceof Iterable) {
            return addIterable(index, (Iterable) o);
        } else if (o instanceof String) {
            e = parse((String) o);
        } else {
            e = (E) o;
        }
        return plainAdd(index, e);
    }
    
    private List<E> list() {
        if (initial != null) {
            Object tmp = initial;
            initial = null;
            smartAdd(tmp);
        }
        return list;
    }
    
    protected boolean plainAdd(int index, E element) {
        list().add(index, element);
        return true;
    }
    
    protected boolean addIterable(int index, Iterable<?> iterable) {
        boolean change = false;
        for (Object o: iterable) {
            change = true;
            smartAdd(index++, o);
        }
        return change;
    }

    @Override
    public E get(int index) {
        return list().get(index);
    }

    @Override
    public int size() {
        return list().size();
    }

    @Override
    public E remove(int index) {
        return list().remove(index);
    }
    
    public int indexOf(String s) {
        return indexOf(parse(s));
    }
    
    public E get(String s) {
        return get(indexOf(s));
    }

    @Override
    public boolean remove(Object o) {
        return smartRemove(o);
    }
    
    protected boolean smartRemove(Object o) {
        if (o instanceof Object[]) {
            o = Arrays.asList((Object[]) o);
        }
        if (o instanceof Iterable) {
            return removeIterable((Iterable) o);
        } else if (o instanceof String) {
            return remove((String) o);
        } else {
            return super.remove((E) o);
        }
    }
    
    public boolean remove(String s) {
        int i = indexOf(s);
        if (i < 0) return false;
        remove(i);
        return true;
    }

    private boolean removeIterable(Iterable<?> iterable) {
        boolean change = false;
        for (Object o: iterable) {
            change |= smartRemove(o);
        }
        return change;
    }
}
