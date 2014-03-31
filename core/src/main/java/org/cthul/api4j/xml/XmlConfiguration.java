package org.cthul.api4j.xml;

/**
 *
 */
public interface XmlConfiguration {

    XmlHandler accept(String namespace, String element);
}
