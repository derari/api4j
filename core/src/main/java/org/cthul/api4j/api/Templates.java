package org.cthul.api4j.api;

import groovy.lang.MissingPropertyException;
import groovy.util.Expando;
import java.util.Map;
import org.cthul.api4j.groovy.DslUtils;

public class Templates extends Expando {
    
    private final Templates parent;
    private final boolean readOnly;

    public Templates() {
        this(null, false);
    }

    public Templates(Templates parent) {
        this(parent, false);
    }

    public Templates(Templates parent, boolean readOnly) {
        this.parent = parent;
        this.readOnly = readOnly;
    }
    
    public void set(String name, Template template) {
        setProperty(name, template);
    }

    @Override
    public void setProperty(String property, Object newValue) {
        if (readOnly) {
            parent.setProperty(property, newValue);
        } else {
            super.setProperty(property, DslUtils.unwrap(newValue));
        }
    }
    
    public void force(String name, Template template) {
        super.setProperty(name, template);
    }
    
    private boolean guard = false;
    
    public synchronized Template get(String template) {
        Template t = null;
        if (guard) {
            t = (Template) propertyMissing(template);
        } else {
            guard = true;
            try {
                t = (Template) getProperty(template);
            } finally {
                guard = false;
            }
        }
        if (t == null) {
            throw new MissingPropertyException(template, getClass());
        }
        return t;
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
