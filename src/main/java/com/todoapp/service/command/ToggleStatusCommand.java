package com.todoapp.service.command;

import com.todoapp.service.TodoService;

public class ToggleStatusCommand implements Command {
    private TodoService service;
    private int todoId;

    public ToggleStatusCommand(TodoService service, int todoId) {
        this.service = service;
        this.todoId = todoId;
    }

    @Override
    public void execute() {
        // We check the current status and flip it
        // Note: This logic assumes your Service has findTodoById
        var todo = service.findTodoById(todoId);
        if (todo.isPresent()) {
            if ("COMPLETED".equals(todo.get().getStatus())) {
                service.uncompleteTodo(todoId);
            } else {
                service.completeTodo(todoId);
            }
        }
    }
}
