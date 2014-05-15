api1 {
    def classNames = [
        "org.cthul.api4j.test.StaticOperations"]
    def all_methods = classes(classNames).allMethods().grep { it.static && it.public }
    def constructors = all_methods.grep { it.name == "allocate" }
    def methods = all_methods.grep { it.name != "allocate" }

    generateClass {
        generateField("handle") {
            modifiers = "private final"
            type = "int"
            generateGetter(modifiers: "protected")
        }
        generateConstructor {
            signature = "int handle"
            body = "this.handle = handle;"
        }
        generateConstructors(constructors) { m ->
            body("this(%s.%s(%s));", m.declaringClass.fullyQualifiedName, m.name, m.argumentsString);
        }
        generateMethods(methods) { m ->
            modifiers.remove("static")
            body = templates.staticDelegator(method: m, replace_args: ["handle": "getHandle()"])
        }
    }
}