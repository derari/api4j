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
<#assign all_fields = {} />
<#list mods as m>
    <@list_entries fields[m]!{}; field, type>
    <#assign all_fields = all_fields + {field: type} />
    </@list_entries>
</#list>
<#if all_fields?size == 0>
    <#assign all_fields = fields />
</#if>

/**
 * Creates a new ${current_class}.
<@list_entries all_fields; field, type>
 * @param ${field} 
</@list_entries>
 */
${visibility} ${current_simple_name}(<#rt>
<@list_entries all_fields; field, type>
    <#t><#if cont??>, <#else><#assign cont=true /></#if>
    <#t>${type} ${field}
</@list_entries>
) {
<@list_entries all_fields; field, type>
    this.${field} = ${field};
</@list_entries>
}
