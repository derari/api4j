package org.cthul.api4j.xml;

/**
 * Plug-in entry point for custom xml formats.
 */
public interface XmlConfiguration {

    XmlHandler accept(String namespace, String element);
}
