api("1.0") {

    def classNames = [
        "org.cthul.api4j.test.StaticOperations"]
    def all_methods = classes(classNames).allMethods().grep { it.static && it.public }
    def constructors = all_methods.grep { it.name == "allocate" }
    def methods = all_methods.grep { it.name != "allocate" }

    generateClass "example.MyHandle" {
        write templates.fields(private_final: ["handle": "int"])
        write templates.initializer(fields: ["handle": "int"])
        write templates.getter(fields: ["handle": "int"])
        constructors.each { m ->
            write templates.full_comment(method: m, skip_doctags: ["return"])
            writeln "public %s(%s) {", simpleName, paramsString(m)
            writeln "    this(%s.%s(%s));", m.parentClass.fullyQualifiedName, m.name, argsString(m)
            writeln "}"
        }
        write templates.staticDelegator(methods: methods, modifiers: ["-static"], replace_args: ["handle": "getHandle()"])
    }
}
