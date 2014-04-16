api("1.0") {

    def classNames = [
        "org.example.StaticLib1",
        "org.example.StaticLib2"]
    def methods = classes(classNames).allMethods().grep { it.hasAnnotation(".Factory") }

    generateClass {
        write templates.staticDelegator(methods: methods)
    }
}