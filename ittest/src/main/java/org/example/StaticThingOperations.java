package org.example;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class StaticThingOperations {
    
    private static final AtomicLong id = new AtomicLong();
    private static final Map<String, String> map = new ConcurrentHashMap<>();

    public static long newThing(String key) {
        return newThing(key, "");
    }
    
    public static long newThing(String key, String value) {
        long i = nextId();
        write(i, key, value);
        return i;
    }
    
    private static long nextId() {
        return id.getAndIncrement();
    }
    
    public static String read(long thing, String key) {
        return map.get(thing + "_" + key);
    }
    
    public static void write(long thing, String key, String value) {
        map.put(thing + "_" + key, value);
    }
    
    public static boolean clear(long thing) {
        boolean found = false;
        String prefix = thing + "_";
        Iterator<String> keys = map.keySet().iterator();
        while (keys.hasNext()) {
            if (keys.next().startsWith(prefix)) {
                keys.remove();
                found = true;
            }
        }
        return found;
    }
}
