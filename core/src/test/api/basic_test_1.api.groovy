api("1.0") {

    def classNames = [
        "org.cthul.api4j.test.StaticTestClass1",
        "org.cthul.api4j.test.StaticTestClass2"]
    def methods = classes(classNames).allMethods().grep { it.hasAnnotation(".AtRuntime") }

    generateClass "example.Foo" {
        println methods
        write templates.staticDelegator(methods: methods)
    }
}
