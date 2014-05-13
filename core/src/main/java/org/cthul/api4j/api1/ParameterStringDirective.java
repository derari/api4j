package org.cthul.api4j.api1;

import org.cthul.api4j.fm.DslDirectiveBase;
import com.thoughtworks.qdox.model.JavaParameter;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ParameterStringDirective extends DslDirectiveBase 
                implements TemplateMethodModelEx, TemplateDirectiveModel {
    
    private final boolean argumentsOnly;

    public ParameterStringDirective(boolean argumentsOnly) {
        this.argumentsOnly = argumentsOnly;
    }

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        assertRequiredArgs(arguments, "params");
        assertArgumentCountLE(arguments, 2);
        
        JavaParameter[] params = getParameters(arguments.get(0));
        
        Map<String, String> replace = null;
        if (arguments.size() > 1) {
            replace = getValue(Map.class, arguments.get(1));
        }
        return build(params, replace);
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        JavaParameter[] jparams;
        Map<String, String> replace;
//        int i = 0;
        
        jparams = getParameters(params.remove("params"));
        if (jparams == null) jparams = getParameters(params.remove("method"));
        replace = getValue(Map.class, params.remove("replace"));
        
        assertNoOtherArguments(params);
        assertLoopVarCountLE(loopVars, 0);
        
//        if (jparams == null && loopVars.length > i) {
//            jparams = getParameters(loopVars[i++]);
//        }
//        if (exclude == null && loopVars.length > i) {
//            exclude = getStrings(loopVars[i++]);
//        }
        env.getOut().append(build(jparams, replace));
    }
    
    protected String build(JavaParameter[] params, Map<String, String> replace) {
        if (params == null) {
            throw new IllegalArgumentException("Expected parameters argument");
        }
        if (replace == null) replace = Collections.emptyMap();
        if (params.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        pm: for (JavaParameter pm: params) {
            String rep = replace.get(pm.getName());
            if (rep == null) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                if (!argumentsOnly) {
                    sb.append(pm.getType().getGenericValue());
                    sb.append(' ');
                }
                sb.append(pm.getName());
            } else if (argumentsOnly) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(rep);
            }
        }
        return sb.toString();
    }
}
