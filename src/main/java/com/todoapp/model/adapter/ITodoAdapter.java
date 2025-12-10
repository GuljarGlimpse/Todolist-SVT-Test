package com.todoapp.model.adapter;

import com.todoapp.model.Todo;

// Adapter Interface - matches AdapterClient.java
public interface ITodoAdapter {
    Todo convertToTodo(JsonTask jsonTask);
    JsonTask convertToJson(Todo todo);
}