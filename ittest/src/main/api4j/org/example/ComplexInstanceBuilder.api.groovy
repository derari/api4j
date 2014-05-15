api1 {

    // fetch fields from actual constructor,
    // to ensure order and types are always correct
    def constr = classes("org.example.ComplexInstance").allConstructors()
                    .grep({ it.hasAnnotation(".Factory")}).first()
    def fields = [:]
    constr.parameters.each { fields[it.name] = it.type }

    // generate more helpers for list members
    def lists = ["data" : "java.lang.String"]

    generateClass {

        generateConstructor {
            // initialize lists
            lists.keySet().each {
                body("this.%s = new java.util.ArrayList<>();", it)
            }
        }

        // create default getters and setters
        def getters = []
        fields.each { name, type ->
            generateField(name, type: type) {
                modifiers = "private"
                generateGetter { getters << it.name + "()" }
                generateSetter(fluent: true)
            }
        }

        // create list appenders
        lists.each { name, type ->
            def item = singularOf(name)
            generateMethod("add" + CamelCase(item)) {
                modifiers = "public"
                returns = declaringClass
                signature = type + " " + item;
                body("this.%s.add(%s);", name, item)
                body << "\nreturn this;"
            }
            generateMethod("add" + CamelCase(name)) {
                modifiers = "public"
                returns = declaringClass
                signature = "java.util.Collection<? extends " + type + "> " + name;
                body("this.%s.addAll(%<s);", name)
                body << "\nreturn this;"
            }
        }

        // build method
        generateMethod("build") {
            modifiers = "public"
            returns = "org.example.ComplexInstance";
            body("return new org.example.ComplexInstance(%s);", getters.join(", "))
        }
    }
}