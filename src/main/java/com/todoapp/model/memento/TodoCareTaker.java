package com.todoapp.model.memento;

import java.util.Stack;

// Memento Pattern CareTaker - matches MementoPatternDemo.java
public class TodoCareTaker {
    private Stack<TodoMemento> undoStack = new Stack<>();
    private Stack<TodoMemento> redoStack = new Stack<>();

    public void saveState(TodoMemento memento) {
        undoStack.push(memento);
        redoStack.clear(); // Clear redo stack when new action is performed
        System.out.println("✓ State saved to history");
    }

    public TodoMemento undo() {
        if (undoStack.size() > 1) {
            TodoMemento currentState = undoStack.pop();
            redoStack.push(currentState);
            System.out.println("↶ Undo performed");
            return undoStack.peek();
        }
        System.out.println("✗ Cannot undo further");
        return null;
    }

    public TodoMemento redo() {
        if (!redoStack.isEmpty()) {
            TodoMemento state = redoStack.pop();
            undoStack.push(state);
            System.out.println("↷ Redo performed");
            return state;
        }
        System.out.println("✗ Cannot redo further");
        return null;
    }

    public boolean canUndo() {
        return undoStack.size() > 1;
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void clearHistory() {
        undoStack.clear();
        redoStack.clear();
        System.out.println("History cleared");
    }
}