package org.cthul.api4j.gen;

import java.io.IOException;
import org.cthul.api4j.groovy.GroovyDsl;

public class BlockGenerator extends SimpleGenerator {
    
    private final String first;
    private final String last;

    public BlockGenerator(GroovyDsl dsl, String first, String last) {
        super(dsl);
        this.first = first;
        this.last = last;
    }

    @Override
    protected void actualWriteTo(Appendable a) throws IOException {
        try (BlockAppendable block = block(a)) {
            super.actualWriteTo(block);
        }
    }
    
    protected BlockAppendable block(Appendable a) {
        return new BlockAppendable(a);
    }

    protected class BlockAppendable implements Appendable, AutoCloseable {
        
        private boolean empty = true;
        private final Appendable a;

        public BlockAppendable(Appendable a) {
            this.a = a;
        }
        
        private void preAppend() throws IOException {
            if (empty) {
                if (first != null) a.append(first);
                empty = false;
            }
        }
        
        @Override
        public Appendable append(CharSequence csq) throws IOException {
            preAppend();
            a.append(csq);
            return this;
        }

        @Override
        public Appendable append(CharSequence csq, int start, int end) throws IOException {
            preAppend();
            a.append(csq, start, end);
            return this;
        }

        @Override
        public Appendable append(char c) throws IOException {
            preAppend();
            a.append(c);
            return this;
        }

        @Override
        public void close() throws IOException {
            if (!empty) {
                if (last != null) a.append(last);
            }
        }
    }
}
