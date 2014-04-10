package org.cthul.api4j.gen;

public class GeneratorUtils {
    
    public static String classNameForPath(String path) {
        int i = path.indexOf('.');
        return (i < 0 ? path : path.substring(0, i)).replace('/', '.').replace('\\', '.');
    }
    
}
