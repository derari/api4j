package org.cthul.api4j.api;

import java.util.Map;
import org.cthul.api4j.gen.SelfGenerating;

public interface Template {
    
    SelfGenerating generate(Map<String, Object> map);
}
