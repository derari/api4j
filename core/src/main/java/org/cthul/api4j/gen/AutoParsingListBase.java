package org.cthul.api4j.gen;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * A list of elements that automatically parses strings.
 * @param <E> item type
 */
public abstract class AutoParsingListBase<E> extends AbstractList<E> {
    
    public static <E, L extends AutoParsingListBase<E>> L wrap(List<E> list, Class<L> clazz, Function<List<E>, L> wrapper) {
        if (clazz.isInstance(list)) {
            return clazz.cast(list);
        }
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
    public boolean add(E e) {
        return smartAdd(e);
    }

    @Override
    public void add(int index, E element) {
        smartAdd(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return smartAdd(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return smartAdd(index, c);
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
        return smartRemove(false, o);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return smartRemove(true, c);
    }
    
    protected boolean smartRemove(boolean all, Object o) {
        if (o instanceof Object[]) {
            o = Arrays.asList((Object[]) o);
        }
        if (o instanceof Iterable) {
            return removeIterable((Iterable) o);
        } else if (o instanceof String) {
            return all ? removeAll((String) o) : remove((String) o);
        } else {
            return super.remove((E) o);
        }
    }
    
    public boolean removeAll(String s) {
        int i = indexOf(s);
        if (i < 0) return false;
        while (i >= 0) {
            remove(i);
            i = indexOf(s);
        }
        return true;
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
            change |= smartRemove(true, o);
        }
        return change;
    }
    
    public boolean setTo(String value) {
        clear();
        return smartAdd(value);
    }
}
