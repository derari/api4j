api1 {

    def classNames = [
        "org.example.StaticThingOperations"]
    def allMethods = classNames.asClasses().allMethods.grep { it.static && it.public }
    def newThings = allMethods.grep { it.name == "newThing" }
    allMethods = allMethods.grep { it.name != "newThing" }

    generatedClass {
        field("thingId") {
            modifiers = "private final"
            type = "long"
            getter()
        }
        constructor {
            signature = "long thingId"
            body = "this.thingId = thingId;"
        }
        constructors(newThings) { m ->
            body = "this(%s);" % templates.staticCall(method: m);
        }
        methods(allMethods) { m ->
            modifiers.remove("static")
            parameters.removeAll { it.name == "thing" }
            body = templates.staticDelegator(method: m, replace_args: ["thing": "getThingId()"])
        }
    }
}