api1 {

    generatedClass {
        typeParameters << "T"
        typeParameters << "R extends java.lang.Runnable"
        interfaces << "java.lang.Runnable"
        interfaces << ["java.util.Concurrent<T>"]

        def aClass = "java.lang.String".asClass();
        method("foo", returns: aClass, body: 'return "";')

        method("run") { 
            modifiers = "public"; 
            body = ";" 
            tags << "see java.lang.Runnable#run()"
        }
    }

}