package org.cthul.api4j.gen;

import groovy.lang.Closure;
import groovy.lang.MissingMethodException;
import java.io.IOException;
import org.cthul.api4j.groovy.DslNative;
import org.cthul.api4j.groovy.DslUtils;
import org.cthul.api4j.groovy.GroovyDsl;
import org.cthul.strings.Strings;

public abstract class GeneratorBase extends DslNative {

    protected final StringBuilder pre = new StringBuilder();
    protected final StringBuilder body = new StringBuilder();
    protected final StringBuilder post = new StringBuilder();

    private final GroovyDsl dsl;

    public GeneratorBase(GroovyDsl dsl) {
        this.dsl = dsl;
    }

    @Override
    protected GroovyDsl dsl() {
        return dsl;
    }
    
    public StringBuilder getPre() {
        return pre;
    }

    public StringBuilder getBody() {
        return body;
    }

    public StringBuilder getPost() {
        return post;
    }
    
    public GeneratorBase pre(CharSequence s) {
        getPre().append(s);
        return this;
    }
    
    public GeneratorBase body(CharSequence s) {
        getBody().append(s);
        return this;
    }
    
    public GeneratorBase post(CharSequence s) {
        getPost().append(s);
        return this;
    }
    
    public GeneratorBase pre(CharSequence s, Object... args) {
        return pre(Strings.format(s, args));
    }
    
    public GeneratorBase body(CharSequence s, Object... args) {
        return body(Strings.format(s, args));
    }
    
    public GeneratorBase post(CharSequence s, Object... args) {
        return post(Strings.format(s, args));
    }
    
    public GeneratorBase pre(SelfGenerating sg) {
        sg.writeTo(getPre());
        return this;
    }
    
    public GeneratorBase body(SelfGenerating sg) {
        sg.writeTo(getBody());
        return this;
    }
    
    public GeneratorBase post(SelfGenerating sg) {
        sg.writeTo(getPost());
        return this;
    }
    
    public GeneratorBase pre(Object o) {
        getPre().append(o);
        return this;
    }
    
    public GeneratorBase body(Object o) {
        getBody().append(o);
        return this;
    }
    
    public GeneratorBase post(Object o) {
        getPost().append(o);
        return this;
    }
    
    protected GeneratorBase sub(StringBuilder sb, Closure<?> c) {
        try (SubGenerator sub = new SubGenerator(dsl(), sb)) {
            Object o = DslUtils.configure(dsl(), sub, c);
            if (o != null && o != this && o != sub) {
                sub.body(o);
            }
        }
        return this;
    }
    
    public GeneratorBase pre(Closure<?> c) {
        return sub(getPre(), c);
    }
    
    public GeneratorBase body(Closure<?> c) {
        return sub(getBody(), c);
    }
    
    public GeneratorBase post(Closure<?> c) {
        return sub(getPost(), c);
    }

    @Override
    protected Object methodMissing(String name, Object arg) {
        if (name.equals("add") || name.equals("write")) {
            return invokeMethod("body", arg);
        }
        if (name.endsWith("ln")) {
            String name2 = name.substring(0, name.length()-2);
            try {
                Object o = invokeMethod(name2, arg);
                invokeMethod(name2, "\n");
                return o;
            } catch (MissingMethodException e) {}
        }
        return super.methodMissing(name, arg);
    }
    
    protected void _writeTo(Appendable a) {
        try {
            actualWriteTo(a);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
    
    protected void writeTo(StringBuilder a) {
        _writeTo(a);
    }
    
    protected void writeTo(Appendable a) throws IOException {
        actualWriteTo(a);
    }
    
    protected void actualWriteTo(Appendable a) throws IOException {
        writePreTo(a);
        writeBodyTo(a);
        writePostTo(a);
    }

    protected void writePreTo(Appendable a) throws IOException {
        a.append(getPre());
    }
    
    protected void writeBodyTo(Appendable a) throws IOException {
        a.append(getBody());
    }
    
    protected void writePostTo(Appendable a) throws IOException {
        a.append(getPost());
    }
}
