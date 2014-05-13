package org.cthul.api4j.gen;

import org.cthul.api4j.groovy.DslUtils;

public abstract class SelfGeneratingBase implements SelfGenerating {

    @Override
    public void writeTo(StringBuilder a) {
        DslUtils.uncheckedWriteTo(this, a);
    }    
}
