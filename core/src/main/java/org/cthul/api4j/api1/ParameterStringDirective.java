package org.cthul.api4j.api1;

import com.thoughtworks.qdox.model.JavaParameter;
import freemarker.core.Environment;
import freemarker.template.*;
import java.io.IOException;
import java.util.*;
import org.cthul.api4j.fm.DslDirectiveBase;
import static org.cthul.api4j.fm.FmUtils.getValue;

/**
 * A freemarker command to generate method signatures and argument lists.
 * Requires a "params" or "method" argument to be a JavaMethod or a list of JavaParameters,
 * and optionally a "replace" argument as a String-String map.
 */
public class ParameterStringDirective extends DslDirectiveBase 
                implements TemplateMethodModelEx, TemplateDirectiveModel {
    
    /** Generates a list of argument names */
    public static final ParameterStringDirective ARGUMENTS = new ParameterStringDirective(true, false);
    
    /** Generates a list of parameter types and names */
    public static final ParameterStringDirective PARAMETERS = new ParameterStringDirective(false, false);
    
    /** Generates a list of parameter types */
    public static final ParameterStringDirective SIGNATURE = new ParameterStringDirective(false, true);
    
    private final boolean argumentsOnly;
    private final boolean signatureOnly;

    public ParameterStringDirective(boolean argumentsOnly) {
        this(argumentsOnly, false);
    }
    
    public ParameterStringDirective(boolean argumentsOnly, boolean signatureOnly) {
        this.argumentsOnly = argumentsOnly;
        this.signatureOnly = signatureOnly;
    }

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        assertRequiredArgs(arguments, "params");
        assertArgumentCountLE(arguments, 2);
        
        List<JavaParameter> params = getParameters(arguments.get(0));
        
        Map<String, String> replace = null;
        if (arguments.size() > 1) {
            replace = getValue(Map.class, arguments.get(1));
        }
        return build(params, replace);
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        List<JavaParameter> jparams;
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
    
    public String build(List<JavaParameter> params) {
        return build(params, Collections.emptyMap());
    }
    
    public String build(List<JavaParameter> params, Map<String, String> replace) {
        if (params == null) {
            throw new IllegalArgumentException("Expected parameters argument");
        }
        if (replace == null) replace = Collections.emptyMap();
        if (params.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        pm: for (JavaParameter pm: params) {
            String rep = replace.get(pm.getName());
            if (rep == null) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                if (!argumentsOnly) {
                    sb.append(pm.getType().getGenericValue());
                    if (!signatureOnly) sb.append(' ');
                }
                if (!signatureOnly) {
                    sb.append(pm.getName());
                }
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
