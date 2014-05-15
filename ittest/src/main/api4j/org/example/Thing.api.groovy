api1 {

    def classNames = [
        "org.example.StaticThingOperations"]
    def all_methods = classes(classNames).allMethods().grep { it.static && it.public }
    def constructors = all_methods.grep { it.name == "newThing" }
    def methods = all_methods.grep { it.name != "newThing" }

    generateClass {
        generateField("thingId") {
            modifiers = "private final"
            type = "long"
            generateGetter
        }
        generateConstructor {
            signature = "long thingId"
            body = "this.thingId = thingId;"
        }
        generateConstructors(constructors) { m ->
            body("this(%s);", templates.staticCall(method: m));
        }
        generateMethods(methods) { m ->
            modifiers.remove("static")
            parameters.removeAll { it.name == "thing" }
            body = templates.staticDelegator(method: m, replace_args: ["thing": "getThingId()"])
        }
    }
}