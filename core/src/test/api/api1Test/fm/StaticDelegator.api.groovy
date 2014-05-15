api1 {

    def classNames = [
        "org.cthul.api4j.test.StaticTestClass1",
        "org.cthul.api4j.test.StaticTestClass2"]
    def methods = classes(classNames).allMethods().grep { it.hasAnnotation(".AtRuntime") }

    generateClass {
        generateMethods(methods) { m ->
            body = templates.staticDelegator(method: m)
        }
    }
}