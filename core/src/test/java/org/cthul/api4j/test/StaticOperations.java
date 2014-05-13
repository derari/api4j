package org.cthul.api4j.test;

public class StaticOperations {
    
    /**
     * Allocates data
     * @param data the data
     * @return handle
     */
    public static int allocate(String data) {
        return 0;
    }
    
    public static String read(int handle, int index) {
        return "";
    }
    
    public static void store(int handle, int index, String data) {
    }
    
    /**
     * Frees the resource
     * @param handle 
     */
    public static void free(int handle) {
    }
}
