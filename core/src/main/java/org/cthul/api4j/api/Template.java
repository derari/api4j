package org.cthul.api4j.api;

import java.util.HashMap;
import java.util.Map;
import org.cthul.api4j.gen.SelfGenerating;

public interface Template {
    
    SelfGenerating generate(Map<String, Object> map);
    
    default WithArgs with(String key, Object value) {
        return withArgs().put(key, value);
    }
    
    default WithArgs with(Map<String, Object> map) {
        return withArgs().putAll(map);
    }
    
    default WithArgs withArgs() {
        return new WithArgs() {
            final Map<String, Object> map = new HashMap<>();
            @Override
            public WithArgs put(String key, Object value) {
                map.put(key, value);
                return this;
            }
            @Override
            public WithArgs putAll(Map<String, Object> map) {
                map.putAll(map);
                return this;
            }
            @Override
            public SelfGenerating generate() {
                return Template.this.generate(map);
            }
            @Override
            public SelfGenerating generate(Map<String, Object> map) {
                return with(map).generate();
            }
            @Override
            public WithArgs withArgs() {
                return WithArgs.super.withArgs().putAll(map);
            }
            @Override
            public String toString() {
                return Template.this.toString() + map;
            }
        };
    }
    
    interface WithArgs extends Template {
        
        WithArgs put(String key, Object value);
        
        WithArgs putAll(Map<String, Object> map);
        
        SelfGenerating generate();
    }
}
