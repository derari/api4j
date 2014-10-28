package org.cthul.api4j.fm;

import freemarker.ext.util.WrapperTemplateModel;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;
import freemarker.template.TemplateScalarModel;
import freemarker.template.TemplateSequenceModel;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.cthul.api4j.groovy.DslUtils;

public class FmUtils {
    
    public static Object[] toArray(TemplateSequenceModel seq) throws TemplateModelException {
        return toArray(Object.class, seq);
    }
    
    public static <T> T[] toArray(Class<T> clazz, TemplateSequenceModel seq) throws TemplateModelException {
        final T[] result = (T[]) Array.newInstance(clazz, seq.size());
        for (int i = 0; i < result.length; i++) {
            TemplateModel tm = seq.get(i);
            result[i] = getValue(clazz, tm);
        }
        return result;
    }
    
    public static Map<?,?> toMap(TemplateHashModelEx hash) throws TemplateModelException {
        Map<Object, Object> map = new LinkedHashMap<>();
        TemplateModelIterator it = hash.keys().iterator();
        while (it.hasNext()) {
            String key = getValue(String.class, it.next());
            map.put(key, getValue(Object.class, hash.get(key)));
        }
        return map;
    }
    
    public static <T> T getValue(Class<T> clazz, Object o) throws TemplateModelException {
        if (clazz.isArray() && ! clazz.getComponentType().isPrimitive()) {
            return (T) getArray((Class) clazz, o);
        }
        if (clazz == Map.class) {
            return (T) toMap((TemplateHashModelEx) o);
        }
        if (o == null) return null;
        if (o instanceof WrapperTemplateModel) {
            o = ((WrapperTemplateModel) o).getWrappedObject();
        }
        if (o instanceof TemplateScalarModel && clazz.isAssignableFrom(String.class)) {
            o = ((TemplateScalarModel) o).getAsString();
        }
        o = DslUtils.unwrap(o);
        return (T) o;
    }
    
    public static <T> List<T> getList(Class<T> clazz, Object o) throws TemplateModelException {
        o = getValue(Object.class, o);
        if (o == null) return null;
        if (o instanceof TemplateSequenceModel) {
            Object[] ary = FmUtils.toArray((TemplateSequenceModel) o);
            DslUtils.unwrapAll(ary);
            o = Arrays.asList(ary);
        }
        if (o instanceof Object[]) {
            o = Arrays.asList((Object[]) o);
        }
        if (o instanceof Collection && !(o instanceof List)) {
            o = new ArrayList<>((Collection) o);
        }
        return (List) o;
    }

    public static <T> T[] getArray(Class<? extends T[]> clazz, Object o) throws TemplateModelException {
        o = getValue(Object.class, o);
        if (o == null) return null;
        if (o instanceof TemplateSequenceModel) {
            Object[] ary = FmUtils.toArray((TemplateSequenceModel) o);
            DslUtils.unwrapAll(ary);
            o = ary;
        }
        if (o instanceof Collection) {
            o = ((Collection<String>) o).toArray();
        }
        if (clazz.getComponentType().isInstance(o) && !clazz.isInstance(o)) {
            o = new Object[]{o};
        }
        Class<?> comp = clazz.getComponentType();
        final Object[] ary = (Object[]) o;
        for (int i = 0; i < ary.length; i++) {
            ary[i] = getValue(comp, ary[i]);
        }
        if (clazz.isInstance(ary)) return (T[]) ary;
        return Arrays.copyOf(ary, ary.length, clazz);
    }

    public static <T> T getValue(Class<T> clazz, Object o, T def) throws TemplateModelException {
        T t = getValue(clazz, o);
        if (t == null) return def;
        return t;
    }
}
