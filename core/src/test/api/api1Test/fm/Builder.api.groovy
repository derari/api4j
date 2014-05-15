api1 {

    def fields = ["name": "String", "length": "int"]
    def getters = []

    generateClass {
        fields.each { name, type ->
            generateField(name, type: type) {
                modifiers = "private";
                generateGetter() { getters << it.name + "()" }
                generateSetter(fluent: true)
            }
        }
        generateMethod("build") {
            modifiers = "public"
            returns = "java.lang.String"
            body('return ""; // new my.Stuff(%s);', getters.join(", "))
        }
    }
}
