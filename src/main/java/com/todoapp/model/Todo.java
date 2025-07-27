package com.todoapp.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Todo {
    private int id;
    private String title;
    private String description;
    private boolean completed;
    private final LocalDateTime createdAt;  // ✅ Made final - never changes after creation
    private LocalDateTime completedAt;

    public Todo(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = false;
        this.createdAt = LocalDateTime.now();  // Set once in constructor
    }

    // ✅ Keep essential getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    // ✅ Keep setters that are actually used
    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        if (completed) {
            this.completedAt = LocalDateTime.now();
        } else {
            this.completedAt = null;
        }
    }

    // ✅ Keep validation methods
    public boolean isValidTitle() {
        return title != null && !title.trim().isEmpty() && title.length() <= 100;
    }

    public boolean isValidDescription() {
        return description != null && description.length() <= 500;
    }

    // ✅ Keep Object methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return id == todo.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s",
                completed ? "✓" : " ",
                title,
                description);
    }
}