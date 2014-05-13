package org.cthul.api4j.gen;

import java.io.IOException;

/**
 * 
 */
public interface SelfGenerating {
    
    void writeTo(Appendable a) throws IOException;

    void writeTo(StringBuilder a);
}
