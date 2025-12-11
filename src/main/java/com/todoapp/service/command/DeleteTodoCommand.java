package com.todoapp.service.command;

import com.todoapp.service.TodoService;

public class DeleteTodoCommand implements Command {
    private TodoService service;
    private int id;

    public DeleteTodoCommand(TodoService service, int id) {
        this.service = service;
        this.id = id;
    }

    @Override
    public void execute() {
        service.deleteTodo(id);
    }
}
