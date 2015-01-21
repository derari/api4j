package org.cthul.api4j.api1;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaType;
import java.util.Collections;
import org.cthul.api4j.Api4JConfiguration;
import org.cthul.api4j.Api4JEngine;
import org.cthul.api4j.TestEngine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class QdoxToolsTest {
    
    Api4JConfiguration cfg;
    JavaClass jcTheInterface;
    JavaClass jcQdoxTest;
    
    @Before
    public void setUp() {
        Api4JEngine engine = TestEngine.testEngine();
        cfg = engine.getConfiguration();
        Globals.beginContext();
        Globals.put(cfg);
        jcTheInterface = QdoxTools.asClass(TheInterface.class.getCanonicalName().replace(".The", "$The"));
        jcQdoxTest = QdoxTools.asClass(QdoxToolsTest.class.getCanonicalName());
    }
    
    @After
    public void tearDown() {
        Globals.endContext();
    }
    
    private JavaMethod method(String method) {
        return jcQdoxTest.getMethodBySignature(method, Collections.emptyList());
    }
    
    private JavaClass returnTypeOf(String method) {
        return method(method).getReturns();
    }
    
    @Test
    public void test_resolveTypeArgument_basic() {
        JavaClass jc1 = returnTypeOf("method_simple1");
        JavaType result = QdoxTools.resolveTypeArgument(jc1, jcTheInterface, 0);
        assertThat(result.getGenericFullyQualifiedName(), is("java.lang.String"));
    }
    
    @Test
    public void test_resolveTypeArgument_simple_impl() {
        JavaClass jc1 = returnTypeOf("method_simple_impl");
        JavaType result = QdoxTools.resolveTypeArgument(jc1, jcTheInterface, 0);
        assertThat(result.getGenericFullyQualifiedName(), is("java.lang.Double"));
    }
    
    @Test
    public void test_resolveTypeArgument_typed_impl() {
        JavaClass jc1 = returnTypeOf("method_typed_impl");
        JavaType result = QdoxTools.resolveTypeArgument(jc1, jcTheInterface, 0);
        assertThat(result.getGenericFullyQualifiedName(), is("java.lang.Integer"));
    }
    
    @Test
    public void test_resolveTypeArgument_typed_impl_extends() {
        JavaClass jc1 = returnTypeOf("method_typed_impl2");
        JavaType result = QdoxTools.resolveTypeArgument(jc1, jcTheInterface, 1);
        assertThat(result.getGenericFullyQualifiedName(), is("java.lang.String"));
    }
    
    @Test
    public void test_resolveTypeArgument_typed_impl_super() {
        JavaClass jc1 = returnTypeOf("method_typed_impl2");
        JavaType result = QdoxTools.resolveTypeArgument(jc1, jcTheInterface, 0);
        assertThat(result.getGenericFullyQualifiedName(), is("java.lang.Object"));
    }
    
    @Test
    public void test_resolveTypeArgument_typed_impl_generic() {
        JavaMethod jm = method("method_typed_generic");
        JavaType result = QdoxTools.resolveReturnTypeArgument(jm, jcTheInterface, 0);
        assertThat(result.getGenericFullyQualifiedName(), is("T extends java.lang.Double"));
    }
    
    public static TheInterface<String, Integer> method_simple1() {
        return null;
    }
    
    public static SimpleImpl method_simple_impl() {
        return null;
    }
    
    public static TypedImpl<String, Integer> method_typed_impl() {
        return null;
    }
            
    public static TypedImpl<? extends String, ? super Integer> method_typed_impl2() {
        return null;
    }
    
    public static <T extends Double> TypedImpl<T,T> method_typed_generic() {
        return null;
    }
    
    public static interface TheInterface<X, Y> {
    }
}
