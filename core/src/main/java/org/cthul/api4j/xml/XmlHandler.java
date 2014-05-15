package org.cthul.api4j.xml;

import java.io.InputStream;
import org.cthul.api4j.Api4JConfiguration;

public interface XmlHandler {
    
    void handle(Api4JConfiguration cfgg, String path, InputStream in) throws Exception;
}
