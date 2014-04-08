package org.cthul.api4j.xml;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import org.cthul.api4j.api.Generator;
import org.cthul.api4j.api.Template;
import org.cthul.api4j.api1.Api1;
import org.cthul.api4j.api1.GlobalExt;
import org.cthul.api4j.api1.QdoxExt;
import org.cthul.api4j.gen.ClassGenerator;
import org.cthul.api4j.gen.GeneratorUtils;

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
        public void handle(Generator g, String path, InputStream in) throws Exception {
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
            
            Api1 api = new Api1(g, path);
            GlobalExt ge = api.dsl().getExtension(GlobalExt.class);
            List<JavaClass> qdoxClasses = ge.classes(null, classNames);
            Map<String, Object> argMap = new HashMap<>();
            Template staticDelegate = api.getTemplates().get("staticDelegate");
            try (ClassGenerator cg = g.generateClass(api.dsl(), api.getDefaultClassName())) {
                for (JavaClass clazz: qdoxClasses) {
                    for (JavaMethod method: clazz.getMethods()) {
                        if (QdoxExt.hasAnnotation(method, ".Factory")) {
                            argMap.put("method", method);
                            cg.body(staticDelegate.generate(argMap));
                        }
                    }
                }
            }
        }
    }
}
