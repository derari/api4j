api1 {
    def delegatees = [
        "service": "org.example.Service<T>"]

    generateClass {
        typeParameters = "T"
        interfaces = delegatees.values()
        def constr = generateConstructor()
        delegatees.each { name, type ->
            constr.signature << type + " " + name
            constr.body("this.%s = %<s;%n", name)
            def delegatee
            generateField(name, type: type) {
                modifiers = "private final";
                generateGetter(modifiers: "protected") { delegatee = it.name + "()" }
            }
            def methods = classes(type).allMethods()
            generateMethods(methods) { m ->
                modifiers = "public"
                body = templates.delegator(delegatee: delegatee, method: m)
            }
        }
    }
}