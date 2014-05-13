<#if !(prefix??)>
    <#assign prefix = "set" />
</#if>
<#if field??>
    <#assign fields = {name: type} />
</#if>
<@list_entries fields; field, type>
<#if (prefix?length > 0)>
    <#assign set = prefix + field?cap_first />
</#if>

${visibility} void ${set!field}(${type} ${field}) {
    this.${field} = ${field};
}
</@list_entries>