package org.cthul.api4j.xml;

import java.io.InputStream;
import org.cthul.api4j.api.Generator;

public interface XmlHandler {
    
    void handle(Generator g, String path, InputStream in) throws Exception;
}
