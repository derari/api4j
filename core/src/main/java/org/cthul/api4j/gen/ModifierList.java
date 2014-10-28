package org.cthul.api4j.gen;

import java.util.*;

public class ModifierList extends AbstractList<String> {
    
    private final SortedSet<String> set = new TreeSet<>(COMP);
    private String[] array = null;

    public ModifierList() {
    }

    public ModifierList(Collection<String> initial) {
        this();
        addAll(initial);
    }

    public ModifierList(String... initial) {
        this();
        addAll(Arrays.asList(initial));
    }
    
    protected boolean modified() {
        array = null;
        return true;
    }
    
    @Override
    public boolean add(String e) {
        if (e.isEmpty()) {
            return false;
        }
        if (e.contains(" ")) {
            boolean changed = false;
            for (String s: e.split(" ")) {
                changed |= add(s);
            }
            return changed;
        }
        if (set.add(e)) {
            return modified();
        }
        return false;
    }

    @Override
    public void add(int index, String element) {
        add(element);
    }

    @Override
    public String get(int index) {
        if (array == null) {
            array = set.toArray(new String[set.size()]);
        }
        return array[index];
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public void clear() {
        set.clear();
        modified();
    }

    @Override
    public boolean remove(Object o) {
        if (set.remove(o)) {
            return modified();
        }
        return false;
    }

    @Override
    public String remove(int index) {
        String s = get(index);
        remove(s);
        return s;
    }

    @Override
    public int size() {
        return set.size();
    }
    
    @Override
    public Iterator<String> iterator() {
        final Iterator<String> it = set.iterator();
        return new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }
            @Override
            public String next() {
                return it.next();
            }
            @Override
            public void remove() {
                modified();
                it.remove();
            }
        };
    }
    
    private static int mId(String m) {
        for (int i = 0; i < ORDER.length; i++) {
            if (ORDER[i].equals(m)) return i;
        }
        return ORDER.length;
    }

    public static String[] modifiers() {
        return ORDER.clone();
    }
    
    private static final String[] ORDER = {
        "private", "protected", "public",
        "abstract", "default", "static", 
        "final",
        "synchronized", "volatile"
    };
    
    private static final Comparator<String> COMP = (o1, o2) -> {
        int m1 = mId(o1);
        int c = m1 - mId(o2);
        // if order not equal or equal for known modifier
        if (c != 0 || m1 < ORDER.length) return c;
        // compare unknown modifiers
        return o1.compareTo(o2);
    };
}
