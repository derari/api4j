package org.cthul.api4j.gen;

import java.util.*;

public class ModifierList extends AutoParsingStringList {
    
    public static ModifierList wrap(List<String> list) {
        return wrap(list, ModifierList.class, ModifierList::new);
    }

    public ModifierList() {
        super(new SortedList());
    }

    public ModifierList(List<String> list) {
        super(new SortedList(list));
    }

    @Override
    protected String parse(String s) {
        return s;
    }

    @Override
    protected boolean plainAdd(int index, String e) {
        if (e.isEmpty()) {
            return false;
        }
        if (e.contains(" ")) {
            Object[] m = e.split(" ");
            return smartAdd(index, m);
        } else {
            return super.plainAdd(index, e);
        }
    }
    
    private static class SortedList extends AbstractList<String> {
        private final SortedSet<String> set = new TreeSet<>(COMP);
        private final Collection<String> sync;
        private String[] array = null;

        public SortedList() {
            sync = null;
        }

        public SortedList(Collection<String> initial) {
            set.addAll(initial);
            sync = initial;
        }

        protected boolean modified() {
            array = null;
            if (sync != null) {
                sync.clear();
                sync.addAll(set);
            }
            return true;
        }

        @Override
        public boolean add(String e) {
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
                    it.remove();
                    modified();
                }
            };
        }
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
    
    public static boolean isModifier(String s) {
        for (String m: ORDER) {
            if (m.equals(s)) return true;
        }
        return false;
    }
    
    private static final String[] ORDER = {
        "private", "protected", "public",
        "static", 
        "abstract", "default", "final",
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
