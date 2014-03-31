api("1.0") {

    def classNames = [
        "org.cthul.test.StaticTestClass1",
        "org.cthul.test.StaticTestClass2"]
    def methods = classes(classNames).allMethods().grep { it.hasAnnotation(".AtRuntime") }

    generateClass "example.Foo" {
        methods.each { m ->
            write templates.staticDelegate(method: m)
        }
    }
}
