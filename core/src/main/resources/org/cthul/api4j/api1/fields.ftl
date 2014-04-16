<#assign mods = ["private_final", "final", "protected_final", "public_final",
                 "private", "protected", "public"] />
<#if field??>
    <#assign fields = {private_final: {field: type}} />
</#if>
<#if !(fields??)>
    <#assign fields = {} />
</#if>
<#list mods as m>
    <#if (m + "??")?eval>
        <#assign fields = fields + {m: m?eval} />
    </#if>
</#list>

<#list mods as m>
    <@list_entries fields[m]!{}; field, type>
${m?replace("_"," ")} ${type} ${field};
    </@list_entries>
</#list>