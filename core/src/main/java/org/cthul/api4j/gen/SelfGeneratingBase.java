package org.cthul.api4j.gen;

import java.io.IOException;

public abstract class SelfGeneratingBase implements SelfGenerating {

    @Override
    public void writeTo(StringBuilder a) {
        try {
            writeTo((Appendable) a);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        writeTo(sb);
        return sb.toString();
    }
}
