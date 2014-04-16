api1 {
    def delegatees = ["service": "org.example.Service<T>"]

    generateClass {
        interfaces.addAll delegatees.values()
        write templates.fields(private_final: delegatees)
        write templates.initializer(fields: delegatees)
        write templates.getter(fields: delegatees, visibility: "protected", prefix: "")
        delegatees.each { delegatee, type ->
            write templates.delegator(methods: asClass(type).methods, delegatee: delegatee + "()")
        }
    }
}