package org.cthul.api4j.api1;

import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MissingMethodException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.cthul.api4j.Api4JConfiguration;
import org.cthul.api4j.api.Api4JScriptContext;
import org.cthul.api4j.api.Template;
import org.cthul.api4j.api.Templates;
import org.cthul.api4j.gen.GeneratedClass;
import org.cthul.api4j.groovy.ClosureConfigurable;
import org.cthul.api4j.groovy.DslUtils;
import org.cthul.api4j.groovy.NamedClosure;

public class Api1 extends GroovyObjectSupport implements ClosureConfigurable, AutoCloseable {
    
    private final Api4JScriptContext script;
    private final List<AutoCloseable> closeables = new ArrayList<>();
    private Exception closeException = null;
    private final Templates templates;

    public Api1(Api4JScriptContext script) {    
        this.script = script;
        templates = new Templates(script.getTemplates(), true);
        initTemplates();
    }
    
    private void initTemplates() {
        Api4JConfiguration cfg = getConfiguration();
        String[] defaultTemplates = {
            "call", "staticCall",
            "delegator", "staticDelegator"};
        for (String t: defaultTemplates) {
            templates.set(t, cfg.fmTemplate("org/cthul/api4j/api1/" + t + ".ftl"));
        }
    }
    
    public Api4JConfiguration getConfiguration() {
        return script.getConfiguration();
    }

    @Override
    public <V> V configure(Closure<V> closure) {
//        final ImportCustomizer ic = new ImportCustomizer();
//        ic.addStaticStars(
//                "org.cthul.api4j.api1.GeneralImports",
//                "org.cthul.strings.JavaNames",
//                "org.cthul.strings.Romans",
//                "org.cthul.strings.AlphaIndex");
//        final List<CompilationCustomizer> customizers = script.getEngine()
//                .getScriptEngine().getConfig().getCompilationCustomizers();
        return Globals.inNewContext(() -> {
//            customizers.add(ic);
            try {
                Globals.put(this);
                Globals.put(getConfiguration());
                return DslUtils.tryClosureOn(this, closure,
                        GeneralTools.class,
                        QdoxTools.class);
            } finally {
//                customizers.remove(ic);
            }
        });
    }
    
    public void run(Consumer<Api1> r) {
        Globals.inNewContext(() -> {
            Globals.put(this);
            Globals.put(getConfiguration());
            try {
                r.accept(this);
            } finally {
                try {
                    close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        });
    }
    
    @Override
    public synchronized void close() throws Exception {
        closeables.stream().forEach(this::tryClose);
        closeables.clear();
        if (closeException != null) {
            Exception e = closeException;
            closeException = null;
            throw e;
        }
    }
    
    private void tryClose(AutoCloseable ac) {
        try {
            ac.close();
        } catch (Exception e) {
            if (closeException != null) {
                closeException.addSuppressed(e);
            } else {
                closeException = e;
            }
        }
    }
    
    public synchronized void closeEventually(AutoCloseable ac) {
        closeables.add(ac);
    }
    
    public GeneratedClass createClass(String name) {
        int iDot = name.indexOf('.');
        if (iDot < 0) iDot = name.indexOf('\\');
        if (iDot < 0) iDot = name.indexOf('/');
        if (iDot == 0) {
            name = name.substring(1);
        } else if (iDot < 0) {
            String uri = script.getUri();
            int iSlash = uri.lastIndexOf('/');
            if (iSlash < 0) iSlash = uri.lastIndexOf('\\');
            if (iSlash < 0) iSlash = uri.length();
            name = uri.substring(0, iSlash) + "." + name;
        }
        
        GeneratedClass gc = getConfiguration().createClass(name);
        closeEventually(gc);
        return gc;
    }
    
    public GeneratedClass createClass() {
        String uri = script.getUri();
        int dot = uri.indexOf('.');
        if (dot > 0) {
            return createClass(uri.substring(0, dot));
        } else {
            return createClass(uri);
        }
    }
    
    protected Object methodMissing(String name, Object arg) {
        Object[] args = (Object[]) arg;
        if (name.contains(".") 
                && args != null && args.length == 1
                && (args[0] instanceof Closure)) {
            return new NamedClosure<>(name, (Closure<?>) args[0]);
        }
        throw new MissingMethodException(name, getClass(), (Object[]) arg);
    }

    public Templates getTemplates() {
        return templates;
    }
    
    public Template getTemplate(String uri) {
        return getTemplates().get(uri);
    }
}
