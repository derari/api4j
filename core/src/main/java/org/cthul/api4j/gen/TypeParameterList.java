package org.cthul.api4j.gen;

import com.thoughtworks.qdox.model.JavaGenericDeclaration;
import com.thoughtworks.qdox.model.JavaTypeVariable;
import com.thoughtworks.qdox.model.impl.DefaultJavaTypeVariable;
import java.util.List;

public class TypeParameterList<D extends JavaGenericDeclaration, V extends JavaTypeVariable<D>> extends AutoParsingList<V> {

    public static <D extends JavaGenericDeclaration, V extends JavaTypeVariable<D>> TypeParameterList<D, V> wrap(D owner, List<V> list) {
        if (list instanceof TypeParameterList) {
            return (TypeParameterList<D, V>) list;
        }
        return wrap(list, l -> new TypeParameterList<>(owner, l));
    }
    
    private final D owner;

    public TypeParameterList(D owner) {
        this.owner = owner;
    }

    public TypeParameterList(D owner, List<V> list) {
        super(list);
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
