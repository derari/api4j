package org.cthul.api4j.xml;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import org.cthul.api4j.Api4JConfiguration;

public class XmlLoader {
    
    private final Api4JConfiguration g;
    private final List<XmlConfiguration> services;
    private final XMLInputFactory xmlFactory = XMLInputFactory.newFactory();

    public XmlLoader(Api4JConfiguration g) {
        this(g, Thread.currentThread().getContextClassLoader());
    }
    
    public XmlLoader(Api4JConfiguration g, ClassLoader cl) {
        this.g = g;
        services = new ArrayList<>();
        ServiceLoader<XmlConfiguration> loader = ServiceLoader.load(XmlConfiguration.class, cl);
        for (XmlConfiguration cfg: loader) {
            services.add(cfg);
        }
    }
    
    public void load(String path, File f) throws Exception {
        String ns;
        String name;
        try (FileInputStream fis = new FileInputStream(f)) {
            XMLStreamReader r = xmlFactory.createXMLStreamReader(fis);
            while (r.nextTag() != XMLStreamReader.START_ELEMENT) {}
            ns = r.getNamespaceURI();
            name = r.getLocalName();
            r.close();
        }
        
        for (XmlConfiguration c: services) {
            XmlHandler h = c.accept(ns, name);
            if (h != null) {
                System.out.println(path + " -> " + h);
                h.handle(g, path, new FileInputStream(f));
                return;
            }
        }
        throw new IllegalArgumentException(
                "No handler for" + ns + ":" + name + " (" +
                services.size() + " installed)");
    }
    
    public void load(String path, InputStream is) throws Exception {
        BufferedInputStream bis = new BufferedInputStream(is);
        bis.mark(1024*8);

        XMLStreamReader r = xmlFactory.createXMLStreamReader(bis);
        while (r.nextTag() != XMLStreamReader.START_ELEMENT) {}
        String ns = r.getNamespaceURI();
        String name = r.getLocalName();

        bis.reset();
        for (XmlConfiguration c: services) {
            XmlHandler h = c.accept(ns, name);
            if (h != null) {
                h.handle(g, path, bis);
                return;
            }
        }
    }
}
