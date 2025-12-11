package com.todoapp.service.command;

import com.todoapp.service.TodoService;

public class AddTodoCommand implements Command {
    private TodoService service;
    private String title;
    private String description;

    public AddTodoCommand(TodoService service, String title, String description) {
        this.service = service;
        this.title = title;
        this.description = description;
    }

    @Override
    public void execute() {
        service.addTodo(title, description);
    }
}
