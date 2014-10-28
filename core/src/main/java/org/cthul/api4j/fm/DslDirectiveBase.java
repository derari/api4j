package org.cthul.api4j.fm;

import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import freemarker.ext.util.WrapperTemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateSequenceModel;
import java.util.*;
import static org.cthul.api4j.fm.FmUtils.*;
import org.cthul.api4j.groovy.DslUtils;

public abstract class DslDirectiveBase {
    
    protected IllegalArgumentException reportRequiredArgs(Object... params) {
        return reportRequiredArgs(Arrays.asList(params));
    }
    
    protected IllegalArgumentException reportRequiredArgs(List<?> params) {
        return new IllegalArgumentException(
                    "Required parameters missing: " + params);
    }
    
    protected void assertRequiredArgs(Collection<?> args, String... params) {
        if (args.size() < params.length) {
            throw reportRequiredArgs(
                    Arrays.asList(params).subList(args.size(), params.length));
        }
    }
    
    protected void assertArgumentCountLE(Collection<?> args, int size) {
        if (args.size() > size) {
            throw new IllegalArgumentException(
                    "Expected " + (size == 0 ? "no" : size) + " arguments, got " + args.size());
        }
    }
    
//    protected void assertArgumentCountLe(Object[] args, int size) {
//        if (args.length > size) {
//            throw new IllegalArgumentException(
//                    "Expected " + (size == 0 ? "no" : size) + " arguments, got " + args.length);
//        }
//    }
    
    protected void assertLoopVarCountLE(Object[] args, int size) {
        if (args.length > size) {
            throw new IllegalArgumentException(
                    "Expected " + (size == 0 ? "no" : size) + " loop vars, got " + args.length);
        }
    }
    
    protected void assertRequiredArgs(Map<?, ?> args, String... params) {
        List<String> missing = new ArrayList<>();
        for (String p: params) {
            if (!args.containsKey(p)) {
                missing.add(p);
            }
        }
        if (missing.size() > 0) {
            throw reportRequiredArgs(missing);
        }
    }
    
    protected void assertNoOtherArguments(Map<?, ?> args, Object... expect) {
        if (args.isEmpty()) return;
        List<Object> keys = new LinkedList<>(args.keySet());
        if (expect.length > 0) {
            Iterator<Object> it = keys.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                for (Object e: expect) {
                    if (e.equals(o)) {
                        it.remove();
                        break;
                    }
                }
            }
        }
        if (!keys.isEmpty()) {
            throw new IllegalArgumentException(
                    "Unexpected arguments: " + keys);
        }
    }

    protected List<JavaParameter> getParameters(Object o) throws TemplateModelException {
        o = getValue(Object.class, o);
        if (o instanceof JavaMethod) {
            return ((JavaMethod) o).getParameters();
        }
        return getList(JavaParameter.class, o);
    }
    
    protected String[] getStrings(Object o) throws TemplateModelException {
        if (o == null) return null;
        if (o instanceof WrapperTemplateModel) {
            o = ((WrapperTemplateModel) o).getWrappedObject();
        }
        o = DslUtils.unwrap(o);
        if (o instanceof Collection) {
            return ((Collection<String>) o).toArray(NO_STRINGS);
        }
        if (o instanceof CharSequence) {
            return new String[]{o.toString()};
        }
        if (o instanceof TemplateSequenceModel) {
            Object[] ary = FmUtils.toArray((TemplateSequenceModel) o);
            DslUtils.unwrapAll(ary);
            return Arrays.copyOf(ary, ary.length, String[].class);
        }
        return (String[]) o;
    }
    
    private static final String[] NO_STRINGS = {};
}
