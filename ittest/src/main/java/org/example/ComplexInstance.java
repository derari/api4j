package org.example;

import java.util.ArrayList;
import java.util.List;

public class ComplexInstance {

    private final String name;
    private final int id;
    private final List<String> data;

    @Factory
    public ComplexInstance(String name, int id, List<String> data) {
        this.name = name;
        this.id = id;
        this.data = new ArrayList<>(data);
    }
    
    public ComplexInstance(String name, int id) {
        this.name = name;
        this.id = id;
        this.data = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<String> getData() {
        return data;
    }
}
