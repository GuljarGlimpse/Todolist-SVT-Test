package com.todoapp.service.strategy;

import com.todoapp.model.Todo;

public class TitleSearch implements SearchStrategy {
    @Override
    public boolean matches(Todo todo, String query) {
        if (todo.getTitle() == null) return false;
        return todo.getTitle().toLowerCase().contains(query.toLowerCase());
    }
}
