package org.cthul.api4j.gen;

import java.io.IOException;
import org.cthul.api4j.groovy.GroovyDsl;

public class IndentedGenerator extends SimpleGenerator {
    
    private final String first;
    private final String mid;
    private final String last;

    public IndentedGenerator(String indent, GroovyDsl dsl) {
        this(indent, indent, indent, dsl);
    }
    
    public IndentedGenerator(String first, String mid, String last, GroovyDsl dsl) {
        super(dsl);
        this.first = first;
        this.mid = mid;
        this.last = last;
    }

    @Override
    protected void actualWriteTo(Appendable a) throws IOException {
        try (IndentAppendable block = block(a)) {
            super.actualWriteTo(block);
        }
    }
    
    protected IndentAppendable block(Appendable a) {
        return new IndentAppendable(a);
    }

    protected class IndentAppendable implements Appendable, AutoCloseable {
        
        private final StringBuilder line = new StringBuilder();
        private final StringBuilder linebreak = new StringBuilder();
        private boolean empty = true;
        private boolean atBreak = true;
        private final Appendable a;

        public IndentAppendable(Appendable a) {
            this.a = a;
        }
        
        @Override
        public Appendable append(CharSequence csq) throws IOException {
            return append(csq, 0, csq.length());
        }

        @Override
        public Appendable append(CharSequence csq, int start, int end) throws IOException {
            for (int i = start; i < end; i++) {
                appendChar(csq.charAt(i));
            }
            return this;
        }
        
        private void flushLine() throws IOException {
            if (empty) {
                a.append(first);
                empty = false;
            } else {
                a.append(mid);
            }
            a.append(line);
            a.append(linebreak);
            line.setLength(0);
            linebreak.setLength(0);
            atBreak = false;
        }
        
        private void appendChar(char c) throws IOException {
            if (c == '\n' | c == '\r') {
                atBreak = true;
                linebreak.append(c);
            } else {
                if (atBreak) flushLine();
                line.append(c);
            }
        }

        @Override
        public Appendable append(char c) throws IOException {
            appendChar(c);
            return this;
        }

        @Override
        public void close() throws IOException {
            if (atBreak || line.length() > 0) {
                a.append(empty ? first : last);
                a.append(line);
                a.append(linebreak);
            }
        }
    }
}
