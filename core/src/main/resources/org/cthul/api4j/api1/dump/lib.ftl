<#ftl strip_text = true />


===== Macros =====


<#macro full_comment method>
/**
    <#list (method.comment!"")?split("\r\n|\n\r|\n|\r", "r") as l>
        <#if (l_has_next || l?length > 0)>
            <#lt> * ${l}
        </#if>
    </#list>
    <#list method.tags as t>
        <#if (t.name != "param" || !(replace_args[t.value]??))
             && !(skip_doctags?seq_contains(t.name)) >
            <#lt> * @${t.name} ${t.value}
        </#if>
    </#list>
 */
</#macro>


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


<#assign modifier_string2 = "org.cthul.api4j.api1.ModifierStringDirective"?new() />
<#macro modifier_string entity modifiers=[]>
    <#t><@modifier_string2 entity=entity modifiers=modifiers />
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


<#assign current_simple_name = __cg.simpleName />
<#assign current_package = __cg.package />
<#assign current_class = __cg.name />


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
