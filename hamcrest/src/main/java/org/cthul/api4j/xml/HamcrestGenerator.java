package org.cthul.api4j.xml;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cthul.api4j.api.Template;
import org.cthul.api4j.api1.Api1;
import static org.cthul.api4j.api1.QdoxTools.asClasses;
import static org.cthul.api4j.api1.QdoxTools.hasAnnotation;
import static org.cthul.api4j.api1.QdoxTools.method;
import static org.cthul.api4j.api1.QdoxTools.setBody;
import org.cthul.api4j.gen.GeneratedClass;
import org.cthul.api4j.gen.GeneratedMethod;

public class HamcrestGenerator {
    
    private final List<String> classNames;

    public HamcrestGenerator(List<String> classNames) {
        this.classNames = classNames;
    }
    
    public void generate(Api1 api) {
        List<JavaClass> qdoxClasses = asClasses(classNames);
        Map<String, Object> argMap = new HashMap<>();
        Template staticDelegator = api.getTemplate("staticDelegator");

        GeneratedClass cg = api.createClass();
        for (JavaClass sourceClass: qdoxClasses) {
            boolean factoryFound = false;
            for (JavaMethod sourceMethod: sourceClass.getMethods()) {
                if (hasAnnotation(sourceMethod, ".Factory")) {
                    factoryFound = true;
                    GeneratedMethod newMethod = method(cg, sourceMethod);
                    argMap.put("method", sourceMethod);
                    newMethod.setBody(staticDelegator.generate(argMap));
                }
            }
            if (!factoryFound) {
                throw noFactoriesExceoption(sourceClass);
            }
        }
    }
    
    private RuntimeException noFactoriesExceoption(JavaClass sourceClass) {
        StringBuilder sb = new StringBuilder();
        sb.append("No factory methods in ")
                .append(sourceClass.getCanonicalName())
                .append(".");
        for (JavaMethod jm: sourceClass.getMethods()) {
            if (jm.isStatic()) {
                sb.append("\n- ");
                try {
                    sb.append(jm);
                } catch (Exception e) {
                    try {
                        sb.append(jm.getDeclarationSignature(true));
                    } catch (Exception e2) {
                        try {
                            sb.append(jm.getName());
                        } catch (Exception e3) {
                            sb.append(e);
                        }
                    }
                }
            }
        }
        return new RuntimeException(sb.toString());
    }
}
