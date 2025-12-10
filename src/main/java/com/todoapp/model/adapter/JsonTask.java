package com.todoapp.model.adapter;

// Adaptee class - represents external JSON data
public class JsonTask {
    private String jsonData;

    public JsonTask(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    @Override
    public String toString() {
        return "JSON Task: " + jsonData;
    }
}