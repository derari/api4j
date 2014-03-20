package org.cthul.api4j.api1;

import com.thoughtworks.qdox.model.JavaClass;
import java.util.Arrays;
import java.util.List;
import org.cthul.api4j.Api4J;
import org.cthul.api4j.api.Generator;
import org.cthul.api4j.api.PatternSearcher;
import org.cthul.api4j.groovy.DslList;
import org.cthul.api4j.groovy.DslNative;
import org.cthul.api4j.groovy.GroovyDsl;

public class Api1 extends DslNative {
    
    private final Generator g;

    public Api1(Generator g) {
        this.g = g;
    }
    
    public DslList<JavaClass> classes(List<String> patterns) {
        DslList<JavaClass> result = new DslList<>(dsl());
        for (String s: patterns) {
            JavaClass jc = g.getQdox().getClassByName(s);
            result.addValue(jc);
        }
        return result;
    }
    
    public DslList<JavaClass> classes(String... patterns) {
//        PatternSearcher s = PatternSearcher.forPatterns(patterns);
//        List<JavaClass> list = gen.getQdox().search(s);
//        return new DslList<>(dsl(), list);
        return classes(Arrays.asList(patterns));
    }

    @Override
    protected GroovyDsl dsl() {
        return DSL;
    }
    
    private static final GroovyDsl DSL = new GroovyDsl();
    
    static {
        
    }
    
}
