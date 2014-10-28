package org.cthul.api4j.api1;

import java.util.Stack;
import java.util.function.Supplier;
import org.cthul.objects.instance.InstanceMap;

public class Globals {
    
    private static final ThreadLocal<Stack<GlobalInstanceMap>> globals = new ThreadLocal<Stack<GlobalInstanceMap>>(){
        @Override
        protected Stack<GlobalInstanceMap> initialValue() {
            Stack<GlobalInstanceMap> stack = new Stack<>();
            stack.add(new GlobalInstanceMap());
            return stack;
        }  
    };
    
    public static void beginContext() {
        Stack<GlobalInstanceMap> stack = globals.get();
        stack.add(stack.peek().copy());
    }
    
    public static void endContext() {
        Stack<GlobalInstanceMap> stack = globals.get();
        stack.pop();
    }
    
    public static <T> T inNewContext(Supplier<T> supp) {
        beginContext();
        try {
            return supp.get();
        } finally {
            endContext();
        }
    }
    
    public static InstanceMap globals() {
        return globals.get().peek();
    }
    
    public static void put(Object object) {
        globals().replace(object.getClass(), object);
    }
    
    public static <T> T get(Class<T> clazz) {
        return globals().getOrCreate(clazz);
    }
    
    public static <T> T getExisting(Class<T> clazz) {
        T t = globals().get(clazz);
        if (t == null) {
            throw new IllegalStateException(
                    "No global " + clazz + " configured");
        }
        return t;
    }
    
    private static class GlobalInstanceMap extends InstanceMap {

        public GlobalInstanceMap() {
        }

        public GlobalInstanceMap(InstanceMap source) {
            super(source);
        }
        
        public GlobalInstanceMap copy() {
            return new GlobalInstanceMap(this);
        }
    }
}
