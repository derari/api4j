<#macro full_comment method>
    /** 
<#list (method.comment!"")?split("\n") as l><#if (l_has_next || l?length > 0)>
     * ${l}
</#if></#list>
     *
<#list method.tags as t>
     * @${t.name} ${t.value}
</#list>
     */
</#macro>
<#macro parameter_string params>
<#list params as p>${p.type.genericValue} ${p.name}<#if p_has_next>, </#if></#list></#macro>
<#macro argument_string params>
<#list params as p>${p.name}<#if p_has_next>, </#if></#list></#macro>
<#macro generic_parameter_string typeVars>
<#if (typeVars?size > 0)> <<#list typeVars as v><#assign gv = v.genericValue/>${gv?substring(1,gv?length-1)}<#if v_has_next>, </#if></#list>></#if></#macro>
<#macro declaration_string method>
<@modifier_string method /><@generic_parameter_string method.typeParameters /> ${method.returnType.genericValue} ${method.name}(<@parameter_string method.parameters />)</#macro>
<#macro modifier_string method>
${method.modifiers?join(" ")}</#macro>