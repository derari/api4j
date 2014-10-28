api1 {

    def classNames = [
        "org.cthul.api4j.test.StaticTestClass1",
        "org.cthul.api4j.test.StaticTestClass2"]
    def apiMethods = classNames.asClasses.allMethods.grep { it.hasAnnotation(".AtRuntime") }

    generatedClass {
        methods(apiMethods) { m ->
            body = templates.staticDelegator(method: m)
        }
    }
}