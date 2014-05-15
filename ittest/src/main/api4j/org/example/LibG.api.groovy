api1 {

    def classNames = [
        "org.example.StaticLib1",
        "org.example.StaticLib2"]
    def methods = classes(classNames).allMethods().grep { it.hasAnnotation(".Factory") }

    generateClass {
        generateMethods(methods) { m ->
            body = templates.staticDelegator(method: m)
        }
    }
}