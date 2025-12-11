package com.todoapp.ui.command;

import com.todoapp.service.command.Command;
import java.util.ArrayList;
import java.util.List;

public class TodoInvoker {
    private List<Command> commandHistory = new ArrayList<>();

    public void execute(Command command) {
        commandHistory.add(command);
        command.execute();
    }
}
