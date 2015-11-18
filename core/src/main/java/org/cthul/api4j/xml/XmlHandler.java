package org.cthul.api4j.xml;

import java.io.InputStream;
import org.cthul.api4j.Api4JConfiguration;

/**
 * An Api4J script implementation that is configured with an xml file.
 */
public interface XmlHandler {
    
    void handle(Api4JConfiguration cfg, String path, InputStream in) throws Exception;
}
