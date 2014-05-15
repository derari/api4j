package org.cthul.api4j.fm;

import freemarker.ext.util.WrapperTemplateModel;
import freemarker.template.*;
import groovy.lang.GroovyObject;
import groovy.lang.MissingPropertyException;
import java.util.List;
import org.cthul.api4j.groovy.DslList;
import org.cthul.api4j.groovy.DslUtils;

public class DslObjectWrapper extends DefaultObjectWrapper {

    @Override
    public TemplateModel wrap(Object obj) throws TemplateModelException {
        Object unwrapped = DslUtils.unwrap(obj);
        if (obj instanceof DslList || (unwrapped != null && unwrapped.getClass().isArray())) {
            obj = unwrapped;
        }
        if (obj instanceof GroovyObject) {
            return new GroovyTemplateHashModel((GroovyObject) obj);
        }
        return super.wrap(obj);
    }
    
    private class GroovyTemplateHashModel implements TemplateScalarModel, TemplateHashModel, WrapperTemplateModel, AdapterTemplateModel {

        private final GroovyObject go;

        public GroovyTemplateHashModel(GroovyObject go) {
            this.go = go;
        }

        @Override
        public TemplateModel get(String key) throws TemplateModelException {
            try {
                return wrap(go.getProperty(key));
            } catch (MissingPropertyException e) {
                return new GroovyMethodModel(go, key);
            }
//            for (MetaMethod m: go.getMetaClass().getMetaMethods()) {
//                if (m.getName().equals(key)) {
//                    return new GroovyMethodModel(go, key);
//                }
//            }
//            return wrap(go.getProperty(key));
        }

        @Override
        public boolean isEmpty() throws TemplateModelException {
            return go == null;
        }

        @Override
        public Object getWrappedObject() {
            return DslUtils.unwrap(go);
        }

        @Override
        public String getAsString() throws TemplateModelException {
            return go.toString();
        }

        @Override
        public Object getAdaptedObject(Class hint) {
            if (hint.isInstance(go)) return go;
            if (hint.isInstance(DslUtils.unwrap(go))) return DslUtils.unwrap(go);
            return null;
        }
    }
    
    private class GroovyMethodModel implements TemplateMethodModelEx {
        
        private final GroovyObject go;
        private final String name;

        public GroovyMethodModel(GroovyObject go, String name) {
            this.go = go;
            this.name = name;
        }

        @Override
        public Object exec(List arguments) throws TemplateModelException {
            return go.invokeMethod(name, arguments.toArray());
//            List<MetaMethod> methods = go.getMetaClass().getMetaMethods();
//            List<Class<?>[]> params = new ArrayList<>(methods.size());
//            List<Boolean> varArgs = new ArrayList<>(methods.size());
//            for (MetaMethod m: methods) {
//                if (m.getName().equals(name)) {
//                    params.add(m.getNativeParameterTypes());
//                    varArgs.add(m.isVargsMethod());
//                }
//            }
//            int best = Signatures.bestMatch(
//                    params.toArray(new Class<?>[params.size()][]),
//                    Boxing.unboxBooleans(varArgs), 
//                    Signatures.collectArgTypes(arguments.toArray()));
//            if (best < 0) {
//                throw new TemplateModelException("No method " + name + " for " + arguments);
//            }
//            return methods.get(best).doMethodInvoke(go, arguments.toArray());
        }
    }
}
