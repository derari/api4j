package org.cthul.api4j.api1;

import freemarker.core.Environment;
import freemarker.template.*;
import java.io.IOException;
import java.util.*;
import org.cthul.api4j.fm.DslDirectiveBase;

/**
 *
 */
public class ModifierStringDirective {
//extends DslDirectiveBase
//                implements TemplateMethodModelEx, TemplateDirectiveModel {
//
//    @Override
//    public Object exec(List arguments) throws TemplateModelException {
//        assertRequiredArgs(arguments, "entity");
//        
//        AbstractJavaEntity e = getValue(AbstractJavaEntity.class, arguments.get(0));
//        
//        Boolean[] override = newDefaultOverride();
//        String vis = readModifiers(1, arguments, null, override);
//        return build(e, vis, override);
//    }
//
//    @Override
//    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
//        assertLoopVarCountLE(loopVars, 0);
//        
//        AbstractJavaEntity e = getValue(AbstractJavaEntity.class, params.remove("entity"));
//        if (e == null) e = getValue(AbstractJavaEntity.class, params.remove("method"));
//        if (e == null) e = getValue(AbstractJavaEntity.class, params.remove("class"));
//        if (e == null) e = getValue(AbstractJavaEntity.class, params.remove("clazz"));
//        if (e == null) e = getValue(AbstractJavaEntity.class, params.remove("field"));
//        if (e == null) throw reportRequiredArgs("entity");
//        
//        String vis = null;
//        for (String m: MODS_VIS) {
//            vis = getValue(String.class, params.remove(m));
//            if (vis != null) break;
//        }
//        
//        Boolean[] override = newDefaultOverride();
//        Boolean[] ov = getValue(Boolean[].class, params.remove("modOverride"));
//        if (ov != null) {
//            System.arraycopy(ov, 0, override, 0, Math.min(ov.length, override.length));
//        }
//        
//        int i = 0;
//        for (String m: MODS_OTHER) {
//            Boolean o = getValue(Boolean.class, params.remove(m));
//            if (o != null) override[i] = o;
//            i++;
//        }
//
//        Object[] modifiers = getValue(Object[].class, params.remove("modifiers"));
//        if (modifiers != null) {
//            vis = readModifiers(modifiers, vis, override);
//        }
//        
//        assertNoOtherArguments(params);
//        
//        env.getOut().append(build(e, vis, override));
//    }
//    
//    protected String readModifiers(int startIndex, List<?> arguments, String vis, Boolean[] override) throws TemplateModelException {
//        return readModifiers(arguments.subList(startIndex, arguments.size()), vis, override);
//    }
//    
//    protected String readModifiers(Object[] arguments, String vis, Boolean[] override) throws TemplateModelException {
//        return readModifiers(Arrays.asList(arguments), vis, override);
//    }
//    
//    protected String readModifiers(List<?> arguments, String vis, Boolean[] override) throws TemplateModelException {
//        arguments: for (int i = 0; i < arguments.size(); i++) {
//            Object a = getValue(Object.class, arguments.get(i));
//            if (a instanceof String) {
//                for (String m: MODS_VIS) {
//                    if (a.equals(m)) {
//                        vis = m;
//                        continue arguments;
//                    }
//                }
//                boolean set = true;
//                String s = (String) a;
//                if (s.startsWith("+")) {
//                    s = s.substring(1);
//                } else if (s.startsWith("-")) {
//                    set = false;
//                    s = s.substring(1);
//                }
//                int j = 0;
//                for (String m: MODS_OTHER) {
//                    if (s.equals(m)) {
//                        override[j] = set;
//                        continue arguments;
//                    }
//                    j++;
//                }
//                throw new IllegalArgumentException(
//                        "Unexpected modifier: " + s);
//            } else {
//                Boolean[] ov = getValue(Boolean[].class, a);
//                System.arraycopy(ov, 0, override, 0, Math.min(ov.length, override.length));
//            }
//        }
//        return vis;
//    }
//    
//    protected CharSequence build(AbstractJavaEntity e, String vis, Boolean[] overrides) {
//        StringBuilder sb = new StringBuilder();
//        String[] mods = e.getModifiers();
//        Arrays.sort(mods);
//        if (vis == null) {
//            appendExisting(sb, MODS_VIS, mods, null);
//        } else if (vis.length() > 0) {
//            sb.append(vis);
//            sb.append(' ');
//        }
//        appendExisting(sb, MODS_OTHER, mods, overrides);
//        if (sb.length() == 0) return "";
//        sb.setLength(sb.length()-1);
//        return sb;
//    }
//    
//    protected void appendExisting(StringBuilder sb, String[] mods, String[] include, Boolean[] override) {
//        int i = 0;
//        for (String m: mods) {
//            Boolean o = override != null ? override[i++] : null;
//            if (o == Boolean.TRUE ||
//                    (o == null && Arrays.binarySearch(include, m) >= 0)) {
//                sb.append(m);
//                sb.append(' ');
//            }
//        }
//    }
//
//    protected Boolean[] newDefaultOverride() {
//        return new Boolean[]{null, false, false};
//    }
//    
//    private static final String[] MODS_VIS = {
//        "private", "protected", "public"};
//
//    private static final String[] MODS_OTHER = {
//        "static", "final", "synchronized"};
}
