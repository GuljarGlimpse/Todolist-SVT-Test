package com.todoapp.model.memento;

// Memento Pattern - matches MementoPatternDemo.java
public class TodoMemento {
    private final int id;
    private final String title;
    private final String description;
    private final String status;
    private final String priority;

    public TodoMemento(int id, String title, String description, String status, String priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "Memento [id=" + id + ", title=" + title + ", status=" + status + "]";
    }
}