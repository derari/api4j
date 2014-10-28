package org.cthul.api4j.gen;

import com.thoughtworks.qdox.model.JavaGenericDeclaration;
import com.thoughtworks.qdox.model.JavaTypeVariable;
import com.thoughtworks.qdox.model.impl.DefaultJavaTypeVariable;

public class TypeParameterList<D extends JavaGenericDeclaration, V extends JavaTypeVariable<D>> extends AutoParsingList<V> {

    private final D owner;

    public TypeParameterList(D owner) {
        this.owner = owner;
    }
    
    public boolean add(String name) {
        return add(parse(name));
    }

    @Override
    protected V parse(String name) {
        return (V) new DefaultJavaTypeVariable<>(name, owner);
    }
}
