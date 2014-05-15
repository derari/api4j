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
import org.cthul.api4j.Api4JConfiguration;
import org.cthul.api4j.api.Template;
import org.cthul.api4j.api1.Api1;
import org.cthul.api4j.api1.GlobalExt;
import org.cthul.api4j.api1.QdoxExt;
import org.cthul.api4j.gen.GeneratedClass;
import org.cthul.api4j.gen.GeneratedMethod;

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
            
            try (Api1 api = new Api1(cfg.getRootContext().subcontext(path))) {
                GlobalExt ge = api.dsl().getExtension(GlobalExt.class);

                List<JavaClass> qdoxClasses = ge.classes(classNames);
                Map<String, Object> argMap = new HashMap<>();
                Template staticDelegator = api.getTemplates().get("staticDelegator");

                GeneratedClass cg = api.generateClass();
                for (JavaClass sourceClass: qdoxClasses) {
                    for (JavaMethod sourceMethod: sourceClass.getMethods()) {
                        if (QdoxExt.hasAnnotation(sourceMethod, ".Factory")) {
                            GeneratedMethod newMethod = QdoxExt.generateMethod(cg, sourceMethod);
                            argMap.put("method", sourceMethod);
                            QdoxExt.body(newMethod, staticDelegator.generate(argMap));
                        }
                    }
                }
            }
        }
    }
}
