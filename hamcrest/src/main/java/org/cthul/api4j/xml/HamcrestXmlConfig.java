package org.cthul.api4j.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import org.cthul.api4j.Api4JConfiguration;
import org.cthul.api4j.api1.Api1;

public class HamcrestXmlConfig implements XmlConfiguration {

    @Override
    public XmlHandler accept(String namespace, String element) {
        if (namespace == null || "".equals(namespace)) {
            if ("matchers".equals(element) || "factories".equals(element)) {
                return Handler.INSTANCE;
            }
        }
        return null;
    }

    public static class Handler implements XmlHandler {
        
        public static final Handler INSTANCE = new Handler();

        private final XMLInputFactory f = XMLInputFactory.newFactory();
        
        @Override
        public void handle(Api4JConfiguration cfg, String path, InputStream in) throws Exception {
            XMLStreamReader xml = f.createXMLStreamReader(in);
            List<String> classNames = new ArrayList<>();
            while (xml.hasNext()) {
                if (xml.next() == XMLStreamReader.START_ELEMENT) {
                    String clazz = xml.getAttributeValue(null, "class");
                    if (clazz != null) classNames.add(clazz);
                }
            }
            xml.close();
            in.close();
            
            HamcrestGenerator gen = new HamcrestGenerator(classNames);
            Api1 api1 = new Api1(cfg.getContext(path));
            api1.run(gen::generate);
        }
    }
}
