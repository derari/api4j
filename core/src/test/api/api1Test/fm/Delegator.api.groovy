api1 {

    def delegatees = [
        "delegatee": "org.cthul.api4j.test.MyService"]

    generateClass {
        interfaces = delegatees.values()
        def constr = generateConstructor()
        delegatees.each { name, type ->
            constr.signature << type + " " + name
            constr.body("this.%s = %<s;%n", name)
            generateField(name, type: type) {
                modifiers = "private final";
                generateGetter(name: name, modifiers: "protected")
            }
            def methods = classes(type).allMethods()
            def delegatee = name + "()"
            generateMethods(methods) { m ->
                modifiers = "public"
                body = templates.delegator(delegatee: delegatee, method: m)
            }
        }
    }
}
