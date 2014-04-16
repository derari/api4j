api("1.0") {

    templates.fluent_setter = fm '''
<@list_entries fields; field, type>

public ${current_class} ${field}(${type} ${field}) {
    this.${field} = ${field};
    return this;
}
</@list_entries>
'''

    def fields = ["length": "int", "name": "String"]

    generateClass "example.MyBuilder" {
        write templates.fields(protected: fields)
        write templates.initializer(fields: [:])
        write templates.getter(fields: fields)
        write templates.fluent_setter(fields: fields)
    }
}
