package com.todoapp.ui.facade;

import com.todoapp.service.TodoService;
import com.todoapp.model.Todo;
import java.util.List;

public class TodoFacade {
    private TodoService todoService;

    public TodoFacade() {
        this.todoService = TodoService.getInstance();
    }

    public List<Todo> getAll() {
        return todoService.getAllTodos();
    }

    public List<Todo> getPending() {
        return todoService.getPendingTodos();
    }

    public List<Todo> getCompleted() {
        return todoService.getCompletedTodos();
    }
}
