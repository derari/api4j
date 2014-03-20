package org.cthul.api4j.groovy;

import groovy.lang.MetaClass;
import groovy.lang.MissingMethodException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.codehaus.groovy.runtime.InvokerHelper;

public class DslList<E> extends AbstractList<DslObject<E>> implements DslObject<List<E>> {
    
    private final GroovyDsl dsl;
    private final List<E> actual;
    private MetaClass metaClass = InvokerHelper.getMetaClass(getClass());

    public DslList(GroovyDsl dsl) {
        this(dsl, new ArrayList<E>());
    }

    public DslList(GroovyDsl dsl, List<E> actual) {
        this.dsl = dsl;
        this.actual = actual;
    }

    @Override
    public boolean add(DslObject<E> e) {
        return actual.add(e.__object());
    }
    
    public boolean addValue(E e) {
        return actual.add(e);
    }

    @Override
    public DslObject<E> get(int index) {
        return dsl.wrap(actual.get(index));
    }

    @Override
    public int size() {
        return actual.size();
    }

    @Override
    public List<E> __object() {
        return actual;
    }
    
    public DslList<?> all(String name, Object... args) {
        DslUtils.unwrapArgs(args);
        DslList<Object> result = new DslList<>(dsl);
        if (isEmpty()) return result;
        name = "get" + name;
        for (E o: actual) {
            MetaClass mc = InvokerHelper.getMetaClass(o);
            Object o2 = dsl.invokeWithExtensionsNoWrap(o, mc, name, args);
            if (o2 instanceof Collection) {
                for (Object o3: (Collection<?>) o2) {
                    result.add(dsl.wrap(o3));
                }
            } else if (o2 instanceof Object[]) {
                for (Object o3: (Object[]) o2) {
                    result.add(dsl.wrap(o3));
                }
            } else {
                result.add(dsl.wrap(o2));
            }
        }
        return result;
    }

    protected Object methodMissing(String name, Object args) {
        if (name.startsWith("all")) {
            return all(name.substring(3), (Object[]) args);
        }
        if (name.startsWith("each")) {
            return all(name.substring(4), (Object[]) args);
        }
        throw new MissingMethodException(name, getClass(), (Object[]) args);
    }
    
    @Override
    public Object invokeMethod(String name, Object args) {
        return getMetaClass().invokeMethod(this, name, args);
    }

    @Override
    public Object getProperty(String propertyName) {
        return getMetaClass().getProperty(this, propertyName);
    }

    @Override
    public void setProperty(String propertyName, Object newValue) {
        getMetaClass().setProperty(this, propertyName, newValue);
    }

    @Override
    public MetaClass getMetaClass() {
        return metaClass;
    }

    @Override
    public void setMetaClass(MetaClass metaClass) {
        this.metaClass = metaClass;
    }
}
