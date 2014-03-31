package org.cthul.api4j.fm;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import org.cthul.api4j.api.Template;
import org.cthul.api4j.gen.SelfGenerating;
import org.cthul.api4j.groovy.DslUtils;

public class FmTemplate implements Template {
        
    private final Configuration fmConfig;
    private final String name;
    private freemarker.template.Template fmTemplate = null;

    public FmTemplate(freemarker.template.Template fmTemplate) {
        this.fmTemplate = fmTemplate;
        this.fmConfig = null;
        this.name = null;
    }

    public FmTemplate(Configuration fmConfig, String name) {
        this.fmConfig = fmConfig;
        this.name = name;
    }
    
    private freemarker.template.Template template() throws IOException {
        if (fmTemplate == null) {
            fmTemplate = fmConfig.getTemplate(name);
        }
        return fmTemplate;
    }
    
    @Override
    public SelfGenerating generate(Map<String, Object> map) {
        return new Generated(map);
    }
    
    protected class Generated implements SelfGenerating {
        private final Map<String, Object> map;

        public Generated(Map<String, Object> map) {
            this.map = map;
        }
        
        @Override
        public void writeTo(Appendable a) throws IOException {
            try (AppendingWriter w = new AppendingWriter(a)) {
                template().process(map, w);
            } catch (TemplateException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void writeTo(StringBuilder a) {
            DslUtils.uncheckedWriteTo(this, a);
        }
    }
    
    protected static class AppendingWriter extends Writer {
        private final Appendable a;

        public AppendingWriter(Appendable a) {
            this.a = a;
        }
        
        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            a.append(new CharArray(cbuf, off, len));
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
        }
    }
    
    protected static class CharArray implements CharSequence {
        private final char[] cbuf; 
        private final int off;
        private final int len;

        public CharArray(char[] cbuf, int off, int len) {
            this.cbuf = cbuf;
            this.off = off;
            this.len = len;
        }

        @Override
        public int length() {
            return len;
        }

        @Override
        public char charAt(int index) {
            return cbuf[off + index];
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return new CharArray(cbuf, off + start, end - start);
        }
    }
}
