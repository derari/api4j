package org.cthul.api4j.gen;

public class GeneratorUtils {
    
    public static String classNameForPath(String path) {
        int i = path.indexOf('.');
        if (i < 0) {
            return path;
        } else {
            return path.substring(0, i);
        }
    }
    
}
