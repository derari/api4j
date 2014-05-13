<#if !(prefix??)>
    <#assign prefix = "get" />
</#if>
<#if field??>
    <#assign fields = {field: type} />
</#if>
<@list_entries fields; field, type>
<#if (prefix?length > 0)>
    <#assign get = prefix + field?cap_first />
</#if>

/**
 * Returns the ${field}.
 * @return ${field}
 */
${visibility} ${type} ${get!field}() {
    return ${field};
}
</@list_entries>