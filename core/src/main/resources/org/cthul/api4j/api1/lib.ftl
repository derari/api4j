<#ftl strip_text = true />

<#macro full_comment method>
/**
    <#list (method.comment!"")?split("\r\n|\n\r|\n|\r", "r") as l>
        <#if (l_has_next || l?length > 0)>
            <#lt> * ${l}
        </#if>
    </#list>
 *
    <#list method.tags as t>
        <#lt> * @${t.name} ${t.value}
    </#list>
 */
</#macro>


<#macro parameter_string params>
    <#list params as p>
        <#t>${p.type.genericValue} ${p.name}<#if p_has_next>, </#if>
    </#list>
</#macro>


<#macro argument_string params>
    <#list params as p>
        <#t>${p.name}<#if p_has_next>, </#if>
    </#list>
</#macro>


<#macro generic_parameter_string typeVars>
    <#if (typeVars?size > 0)>
        <#t> <
        <#list typeVars as v><#t>
            <#assign gv = v.genericValue />
            <#t>${gv?substring(1,gv?length-1)}<#if v_has_next>, </#if>
        </#list>
        ><#t>
    </#if>
</#macro>


<#macro declaration_string method>
    <#t><@modifier_string method />
    <#t><@generic_parameter_string method.typeParameters />
    <#t> ${method.returnType.genericValue}
    <#t> ${method.name}
    <#t>(<@parameter_string method.parameters />)
</#macro>


<#macro modifier_string method>
    <#t>${method.modifiers?join(" ")}
</#macro>


<#if methods??>
    <#assign def_methods = methods />
<#elseif method??>
    <#assign def_methods = [method] />
</#if>
