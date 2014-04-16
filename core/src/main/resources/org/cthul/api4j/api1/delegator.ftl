<#if get??>
    <#assign delegatee = "get" + get?cap_first />
</#if>
<#if !(delegatee??)>
    <#assign delegatee = "delegatee()" />
</#if>
<#list def_methods as method >

<@full_comment method />
<#if atOverride?? && atOverride>
@Override
</#if>
    <#t><@modifier_string2 method=method modifiers=visibility />
    <#t><@generic_parameter_string method.typeParameters />
    <#t> ${method.returnType.genericValue}
    <#t> ${method.name}
    <#t>(<@parameter_string method.parameters replace_args />)
 {
    <@optional_return method />${delegatee}.${method.name}(<@argument_string method.parameters replace_args />);
}
</#list>