package com.todoapp.service.strategy;

import com.todoapp.model.Todo;

public interface SearchStrategy {
    boolean matches(Todo todo, String query);
}
