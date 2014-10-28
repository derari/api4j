package org.example;

import java.util.HashMap;
import java.util.Map;

public class StaticLib1 {
    
    @Factory
    public static <X> X cast(Object o) {
        return (X) o;
    }
    
    /**
     * Maps Ks to Vs.
     * @param <K>
     * @param <V>
     * @param key
     * @param value
     * @return a map
     */
    @Factory
    public static <K, V> Map<K, V> map(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
    
}
