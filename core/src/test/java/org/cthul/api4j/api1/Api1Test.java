package org.cthul.api4j.api1;

import java.io.File;
import org.cthul.api4j.Api4JConfiguration;
import org.cthul.api4j.api.TestConfiguration;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class Api1Test {

    private static final File root = new File("target/test-out/api1Test");
    
    @Test
    public void test_empty() {
        Api4JConfiguration cfg = TestConfiguration.getWithSource();
        cfg.runScript(root, "api1Test/empty/empty.api.groovy");
    }
    
    private void assertFileGenerated(String script) {
        assertFileGenerated(script, script);
    }
    
    private void assertFileGenerated(String script, String file) {
        File expectedFile = new File(root, file + ".java");
        expectedFile.delete();
        Api4JConfiguration cfg = TestConfiguration.getWithSource();
        cfg.runScript(root, "api1Test/" + script + ".api.groovy");
        assertThat(expectedFile.exists(), is(true));        
    }
    
    @Test
    public void test_explicit_name() {
        assertFileGenerated("empty/ExplicitName", "empty/ExplicitNameX");
    }
    
    @Test
    public void test_explicit_name2() {
        assertFileGenerated("empty/ExplicitName2", "empty/ExplicitNameX2");
    }
    
    @Test
    public void test_implicit_name() {
        assertFileGenerated("empty/ImplicitName");
    }
    
    @Test
    public void test_static_delegator() {
        assertFileGenerated("fm/StaticDelegator");
    }
    
    
    @Test
    public void test_delegator() {
        assertFileGenerated("fm/Delegator");
    }
    
    @Test
    public void test_my_handle() {
        assertFileGenerated("fm/MyHandle");
    }
    
    @Test
    public void test_builder() {
        assertFileGenerated("fm/Builder");
    }
}
    
//<<<<<<< HEAD
//    public void test_basic() {
////        Api4JConfiguration g = TestGenerator.getWithSource();
////        g.runScript("t.api.gest_static_proxy.api.groovy");
//    }
//    
//    @Test
//    public void test_qdox() {
//        Api4JConfiguration g = TestGenerator.getWithSource();
//        JavaClass jc = g.getQdox().getClassByName(MyList.class.getName());
//        SimpleGenerator sg = new SimpleGenerator(null);
//        QdoxWriter qw = new QdoxWriter(sg);
//        qw.printJavaFile(jc);
//        StringBuilder sb = new StringBuilder();
//        sg.writeTo(sb);
//        System.out.println(sb);
//    }
//    
//    /**
//     * static doc
//     * @param <Foo>
//     * @param <Bar> 
//     * @see List
//     */
//    public static class MyList<Foo extends Integer, Bar> extends AbstractList<Foo> implements Callable<Map<? extends List<?>, ? super Bar>> {
//
//        private final List<? super Foo> list = null;
//        
//        /**
//         * getter
//         * @param index
//         * @return 
//         */
//        @Override
//        public Foo get(int index) {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public int size() {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public Map<? extends List<?>, ? super Bar> call() throws Exception {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//    }
//}
//=======
//    public void test_library() {
//        JavaClass jc;
//        Generator g = TestGenerator.getWithSource();
////        g.getTemplates().set("testTemplate", g.fmTemplate("testmethod.ftl"));
//        g.runScript("basic_test_1.api.groovy");
//    }
//    
//    @Test
//    public void test_delegator() {
//        Generator g = TestGenerator.getWithSource();
//        g.runScript("delegator_test.api.groovy");        
//    }
//    
//    @Test
//    public void test_handle() {
//        Generator g = TestGenerator.getWithSource();
//        g.runScript("handle_test.api.groovy");        
//    }
//    
//    @Test
//    public void test_builder() {
//        Generator g = TestGenerator.getWithSource();
//        g.runScript("builder_test.api.groovy");        
//    }
//>>>>>>> ab04573103cb1c0605321368b157bdb1e583e6ff
    
//abs, ancestors, 
//byte, 
//c, cap_first, capitalize, ceiling, children, chop_linebreak, chunk, contains, 
//date, datetime, default, double, 
//ends_with, eval, exists, 
//first, float, floor, 
//groups, 
//has_content, html, 
//if_exists, index_of, int, interpret, is_boolean, is_collection, is_date, is_directive, is_enumerable, is_hash, is_hash_ex, is_indexable, is_infinite, is_macro, is_method, is_nan, is_node, is_number, is_sequence, is_string, is_transform, iso, iso_h, iso_h_nz, iso_local, iso_local_h, iso_local_h_nz, iso_local_m, iso_local_m_nz, iso_local_ms, iso_local_ms_nz, iso_local_nz, iso_m, iso_m_nz, iso_ms, iso_ms_nz, iso_nz, iso_utc, iso_utc_h, iso_utc_h_nz, iso_utc_m, iso_utc_m_nz, iso_utc_ms, iso_utc_ms_nz, iso_utc_nz, 
//j_string, join, js_string, json_string, 
//keys, 
//last, last_index_of, left_pad, length, long, lower_case, 
//matches, 
//namespace, new, node_name, node_namespace, node_type, number, number_to_date, number_to_datetime, number_to_time, 
//parent, 
//replace, reverse, right_pad, root, round, rtf, 
//seq_contains, seq_index_of, seq_last_index_of, short, size, sort, sort_by, split, starts_with, string, substring, 
//time, trim, 
//uncap_first, upper_case, url, 
//values, 
//web_safe, word_list, 
//xhtml, xml
    
//    static class TestTemplate implements Template, SelfGenerating {
//
//        private Map<String, Object> map;
//        
//        @Override
//        public SelfGenerating generate(Map<String, Object> map) {
//            this.map = map;
//            return this;
//        }
//
//        @Override
//        public void writeTo(Appendable a) throws IOException {
//            JavaMethod jm = (JavaMethod) map.get("method");
//            
//            a.append("// template ");
//            a.append(jm.getComment());
//            a.append("\n");
//        }
//
//        @Override
//        public void writeTo(StringBuilder a) {
//            DslUtils.uncheckedWriteTo(this, a);
//        }
//    }
