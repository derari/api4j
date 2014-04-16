<#list def_methods as method >

<@full_comment method />
<@declaration_string method replace_args /> {
    <@optional_return method />${method.parentClass.fullyQualifiedName}.${method.name}(<@argument_string method.parameters replace_args />);
}
</#list>