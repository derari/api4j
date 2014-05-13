api("1.0") {

    def delegatees = [
        "delegatee": "org.cthul.api4j.test.MyService"]

    generateClass "example.MyServiceDelegator" {
        interfaces.addAll delegatees.values()
        write templates.fields(private_final: delegatees)        
        write templates.initializer(fields: delegatees)
        write templates.getter(visibility: "protected", prefix: "", fields: delegatees)
        delegatees.each { name, type ->
            def methods = classes(type).allMethods()
            write templates.delegator(delegatee: name + "()", methods: methods, atOverride: true)
        }
    }
}
