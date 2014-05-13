api("1.0") {

    def classNames = [
        "org.cthul.api4j.test.StaticTestClass1",
        "org.cthul.api4j.test.StaticTestClass2"]
    def methods = classes(classNames).allMethods().grep { it.hasAnnotation(".AtRuntime") }

    generateClass "example.Foo" {
        
        write templates.staticDelegator(methods: methods)
    }
}

/*

generateClass {

    generateFields(

    generateMethods(methods) {
        removeParameter "handle"
        source = templates.staticDelegator(it, arguments: ["handle": "getHandle()"])
    }

}

*/