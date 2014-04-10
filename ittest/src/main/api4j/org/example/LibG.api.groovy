api("1.0") {

    def classNames = [
        "org.example.Lib1",
        "org.example.Lib2"]
    def methods = classes(classNames).allMethods().grep { it.hasAnnotation(".Factory") }

    generateClass {
        write templates.staticDelegator(methods: methods)
    }
}