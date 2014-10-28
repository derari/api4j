api1 {

    // fetch fields from actual constructor,
    // to ensure order and types are always correct
    def constr = "org.example.ComplexInstance".asClass().constructors
                    .grep({ it.hasAnnotation(".Factory")}).first()
    def fields = [:]
    constr.parameters.each { fields[it.name] = it.type }

    // generate more helpers for list members
    def lists = ["data" : "java.lang.String"]

    generatedClass {

        constructor {
            // initialize lists
            lists.keySet().each {
                body << "this.%s = new java.util.ArrayList<>();" % it
            }
        }

        // create default getters and setters
        def getters = []
        fields.each { name, type ->
            field(name, type: type, modifiers: "private") {
                getter { getters << it.name + "()" }
                setter(fluent: true)
            }
        }

        // create list appenders
        lists.each { name, type ->
            def item = singularOf(name)
            method("add" + CamelCase(item)) {
                modifiers = "public"
                returns = declaringClass
                signature = type + " " + item;
                body << "this.%s.add(%s);\n" % [name, item]
                body << "return this;"
            }
            method("add" + CamelCase(name)) {
                modifiers = "public"
                returns = declaringClass
                signature = "java.util.Collection<? extends " + type + "> " + name;
                body << "this.%s.addAll(%<s);\n" % name
                body << "return this;"
            }
        }

        // build method
        method("build") {
            modifiers = "public"
            returns = "org.example.ComplexInstance";
            body << "return new org.example.ComplexInstance(%s);" % getters.join(", ")
        }
    }
}