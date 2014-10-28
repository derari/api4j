api1 {
    def delegatees = [
        "service": "org.example.Service<T>"]

    generatedClass {
        typeParameters << "T"
        interfaces << delegatees.values()
        def constr = constructor()
        delegatees.each { name, type ->
            constr.signature << type + " " + name
            constr.body << "this.%s = %<s;%n" % name
            def delegatee
            field(name, type: type) {
                modifiers = "private final";
                getter(modifiers: "protected") { delegatee = it.name + "()" }
            }
            def allMethods = type.asClass().allMethods
            methods(allMethods) { m ->
                modifiers = "public"
                body = templates.delegator(delegatee: delegatee, method: m)
            }
        }
    }
}