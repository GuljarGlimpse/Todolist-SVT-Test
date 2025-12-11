package com.todoapp.service.strategy;

import com.todoapp.model.Todo;

public class DescriptionSearch implements SearchStrategy {
    @Override
    public boolean matches(Todo todo, String query) {
        if (todo.getDescription() == null) return false;
        return todo.getDescription().toLowerCase().contains(query.toLowerCase());
    }
}
