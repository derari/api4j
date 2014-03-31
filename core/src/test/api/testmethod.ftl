
    /** 
<#list (method.comment!"")?split("\n") as l><#if (l_has_next || l?length > 0)>
     * ${l}
</#if></#list>
     *
<#list method.tags as t>
     * @${t.name} ${t.value}
</#list>
     */
    ${method.getDeclarationSignature(true)} {
        return ${method.parentClass.fullyQualifiedName}.${method.name}(<@argumentlist method.parameters />);
    }
