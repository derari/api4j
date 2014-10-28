api1 {

    def classNames = [
        "org.example.StaticLib1",
        "org.example.StaticLib2"]
    def factories = classNames.asClasses().allMethods.grep { it.hasAnnotation(".Factory") }

    generatedClass {
        methods(factories) { m ->
            body = templates.staticDelegator(method: m)
        }
    }
}