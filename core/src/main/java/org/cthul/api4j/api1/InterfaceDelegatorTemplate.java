package org.cthul.api4j.api1;

import java.io.IOException;
import java.util.*;
import org.cthul.api4j.api.Template;
import org.cthul.api4j.gen.*;
import org.cthul.strings.JavaNames;

public class InterfaceDelegatorTemplate implements Template {

    @Override
    public SelfGenerating generate(final Map<String, Object> map) {
        return null;
//        return new SelfGeneratingBase() {
//            @Override
//            public void writeTo(Appendable a) throws IOException {
//                Object superclass = map.get("superclass");
//                Collection<Object> methods = (Collection) map.get("methods");
//                Collection<Object> interfaces = (Collection) map.get("interfaces");
//                if (interfaces == null) interfaces = Arrays.asList(map.get("interface"));
//                Collection<Object> targets = (Collection) map.get("targets");
//                if (targets == null) interfaces = Arrays.asList(map.get("target"));
//                if (targets == null) {
//                    targets = new ArrayList<>();
//                    if (superclass != null) targets.add(superclass);
//                    if (interfaces != null) targets.addAll(interfaces);
//                }
//                if (targets.isEmpty()) {
//                    throw new IllegalArgumentException("No delegatees specified");
//                }
//                
//                List<String> names = new ArrayList<>(targets.size());
//                if (targets.size() == 1) {
//                    names.add("delegatee");
//                } else {
//                    LinkedHashSet<String> uniqueNames = new LinkedHashSet<>();
//                    for (Object t: targets) {
//                        String name = getName(t);
//                        name = JavaNames.camelCase(name);
//                        if (!uniqueNames.add(name)) {
//                            int i = 1;
//                            while (!uniqueNames.add(name + i)) i++;
//                        }
//                    }
//                    names.addAll(uniqueNames);
//                }
//                
//                ClassGenerator cg = ClassGenerator.current();
//                
//                // fields
//                int i = 0;
//                for (Object t: targets) {
//                    a.append("\nprivate final ");
//                    a.append(ClassGenerator.classObjectToString(t));
//                    a.append(' ');
//                    a.append(names.get(i++));
//                    a.append(";");
//                }
//                
//                // constructor
//                a.append("\n\npublic ");
//                a.append(cg.getSimpleName());
//                a.append('(');
//                i = 0;
//                for (Object t: targets) {
//                    if (i > 0) a.append(", ");
//                    a.append(ClassGenerator.classObjectToString(t));
//                    a.append(' ');
//                    a.append(names.get(i++));
//                }
//                a.append(") {");
//                for (String n: names) {
//                    a.append("\nthis.");
//                    a.append(n);
//                    a.append(" = ");
//                    a.append(n);
//                    a.append(";");
//                }
//                a.append("\n}");
//                
//                // accessors
//                i = 0;
//                for (Object t: targets) {
//                    String n = names.get(i++);
//                    a.append("\n\nprotected ");
//                    a.append(ClassGenerator.classObjectToString(t));
//                    a.append(' ');
//                    a.append(n);
//                    a.append("() {");
//                    a.append("\n    return ");
//                    a.append(n);
//                    a.append(";\n}");
//                }
//                
//                // delegating methods
//                GlobalExt g = cg.getDsl().getExtension(GlobalExt.class);
//                Template tDelegator = g.templates(null).get("delegator");
//                
//                Map<String, Object> map2 = new HashMap<>();
//                map2.put("methods", methods);
//                map2.put("delegatee", names.iterator().next());
//                tDelegator.generate(map2);
//            }
//
//            private String getName(Object t) {
//                String s = t.toString();
//                int i = s.lastIndexOf('.');
//                return i < 0 ? s : s.substring(i+1);
//            }
//        };
    }
}
