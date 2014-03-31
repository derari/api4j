
<@full_comment method />
    <@declaration_string method /> {
        return ${method.parentClass.fullyQualifiedName}.${method.name}(<@argument_string method.parameters />);
    }
