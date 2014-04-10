${method}
${methods}
${def_methods}
<#list def_methods as method >

<@full_comment method />
<@declaration_string method /> {
    return ${method.parentClass.fullyQualifiedName}.${method.name}(<@argument_string method.parameters />);
}
</#list>