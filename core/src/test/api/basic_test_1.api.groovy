api("1.0") {

    def classNames = [
        "org.cthul.api4j.test.StaticTestClass1",
        "org.cthul.api4j.test.StaticTestClass2"]
    println classes(classNames)
    println classes(classNames).allMethods()
    def methods = classes(classNames).allMethods().grep { it.hasAnnotation(".AtRuntime") }

    generateClass "example.Foo" {
        println methods
        methods.each { m ->
            println m
            write templates.staticDelegate(method: m)
        }
    }
}
