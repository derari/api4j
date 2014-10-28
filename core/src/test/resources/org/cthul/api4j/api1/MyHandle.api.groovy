api1 {
    def className = "org.cthul.api4j.test.StaticOperations"
    def all_methods = className.asClass.allMethods.grep { it.static && it.public }
    def allocators = all_methods.grep { it.name == "allocate" }
    def operations = all_methods.grep { it.name != "allocate" }

    generatedClass {
        field("private final int handle") {
            getter(modifiers: "protected")
        }
        constructor(signature: "int handle", body: "this.handle = handle;")
        constructors(allocators) { m ->
            body = "this(%s.%s(%s));" % [m.declaringClass.fullyQualifiedName, m.name, m.argumentsString];
        }
        methods(operations) { m ->
            modifiers.remove("static")
            body = templates.staticDelegator(method: m, replace_args: ["handle": "getHandle()"])
        }
    }
}