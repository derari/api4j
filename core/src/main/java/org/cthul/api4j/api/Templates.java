package org.cthul.api4j.api;

import groovy.lang.MissingPropertyException;
import groovy.util.Expando;
import java.util.Map;
import org.cthul.api4j.groovy.DslUtils;

public class Templates extends Expando {
    
    private final Templates parent;

    public Templates() {
        this(null);
    }

    public Templates(Templates parent) {
        this.parent = parent;
    }
    
    public void set(String name, Template template) {
        setProperty(name, template);
    }
    
    private boolean guard = false;
    
    public synchronized Template get(String template) {
        if (guard) {
            return (Template) propertyMissing(template);
        }
        guard = true;
        try {
            return (Template) getProperty(template);
        } finally {
            guard = false;
        }
    }
    
    protected Object propertyMissing(String name) {
        if (parent != null) {
            return parent.getProperty(name);
        }
        throw new MissingPropertyException(name, getClass());
    }
    
    protected Object methodMissing(String name, Object args) {
        Template tpl = (Template) getProperty(name);
        if (tpl == null) {
            throw new IllegalArgumentException(
                    "Unknown template: " + name);
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map) DslUtils.unwrap(((Object[]) args)[0]);
        return tpl.generate(map);
    }
}
