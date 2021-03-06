<#ftl strip_text = true />


===== Macros =====



<#assign parameter_string_ = "org.cthul.api4j.api1.ParameterStringDirective"?new(false) />
<#macro parameter_string params replace={}>
<#t><@parameter_string_ params=params replace=replace />
</#macro>


<#assign argument_string_ = "org.cthul.api4j.api1.ParameterStringDirective"?new(true) />
<#macro argument_string params replace={} >
<#t><@argument_string_ params=params replace=replace />
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


<#macro declaration_string method replace={}>
    <#t><@modifier_string method modifiers![] />
    <#t><@generic_parameter_string method.typeParameters />
    <#t> ${method.returnType.genericValue}
    <#t> ${method.name}
    <#t>(<@parameter_string method.parameters replace />)
</#macro>



<#macro optional_return method >
    <#t><#if method.returnType.genericValue != "void">return </#if>
</#macro>


<#macro list_entries map>
    <#list map?keys as k>
        <#nested k map[k] />
    </#list>
</#macro>


===== Default Globals ====



===== Default Arguments =====


<#if methods??>
    <#assign def_methods = methods />
<#elseif method??>
    <#assign def_methods = [method] />
</#if>


<#if !(visibility??)>
    <#assign visibility = "public" />
</#if>


<#if !(replace_args??)>
    <#assign replace_args = {} />
</#if>


<#if !(skip_doctags??)>
    <#assign skip_doctags = [] />
</#if>
