package org.cthul.api4j.api1;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import java.util.*;
import java.util.concurrent.Callable;
import org.cthul.api4j.api.Generator;
import org.cthul.api4j.api.TestGenerator;
import org.cthul.api4j.gen.QdoxWriter;
import org.cthul.api4j.gen.SimpleGenerator;
import org.junit.Test;

public class Api1Test {

//    @Test
//    public void test_basic() {
//        JavaMethod jm = null;
////        jm.getTypeParameters()[0].getGenericValue()
//        Generator g = TestGenerator.getWithSource();
//        g.getTemplates().set("testTemplate", g.fmTemplate("testmethod.ftl"));
//        g.runScript("basic_test_1.api.groovy");
//    }
    
    @Test
    public void test_basic() {
//        Generator g = TestGenerator.getWithSource();
//        g.runScript("t.api.gest_static_proxy.api.groovy");
    }
    
    @Test
    public void test_qdox() {
        Generator g = TestGenerator.getWithSource();
        JavaClass jc = g.getQdox().getClassByName(MyList.class.getName());
        SimpleGenerator sg = new SimpleGenerator(null);
        QdoxWriter qw = new QdoxWriter(sg);
        qw.printJavaFile(jc);
        StringBuilder sb = new StringBuilder();
        sg.writeTo(sb);
        System.out.println(sb);
    }
    
    /**
     * static doc
     * @param <Foo>
     * @param <Bar> 
     * @see List
     */
    public static class MyList<Foo extends Integer, Bar> extends AbstractList<Foo> implements Callable<Map<? extends List<?>, ? super Bar>> {

        private final List<? super Foo> list = null;
        
        /**
         * getter
         * @param index
         * @return 
         */
        @Override
        public Foo get(int index) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int size() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Map<? extends List<?>, ? super Bar> call() throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
    
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
