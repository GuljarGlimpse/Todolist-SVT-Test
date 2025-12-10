package com.todoapp.model;

import com.todoapp.model.composite.TaskComponent;
import com.todoapp.model.memento.TodoMemento;
import java.util.ArrayList;
import java.util.List;

// Member 2 - Data Model Enhancement with Design Patterns
public class Todo implements TaskComponent {
    private int id;
    private String title;
    private String description;
    private String status;
    private String priority;

    // Composite Pattern: List of children (sub-todos)
    private List<TaskComponent> subTasks = new ArrayList<>();

    // Member 2 implementation - Constructor
    public Todo(int id, String title, String description, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = "MEDIUM";
    }

    // Additional constructor with priority
    public Todo(int id, String title, String description, String status, String priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
    }

    // --- Composite Pattern Methods ---
    public void addSubTask(TaskComponent subTask) {
        subTasks.add(subTask);
    }

    public void removeSubTask(TaskComponent subTask) {
        subTasks.remove(subTask);
    }

    public List<TaskComponent> getSubTasks() {
        return subTasks;
    }

    @Override
    public void displayDetails() {
        System.out.println("Todo [" + id + "]: " + title + " [" + status + "] - Priority: " + priority);
        if (!description.isEmpty()) {
            System.out.println("  Description: " + description);
        }
        if (!subTasks.isEmpty()) {
            System.out.println("  Subtasks (" + subTasks.size() + "):");
            for (TaskComponent component : subTasks) {
                System.out.print("    ");
                component.displayDetails();
            }
        }
    }

    @Override
    public String getDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Todo [").append(id).append("]: ").append(title)
                .append(" [").append(status).append("] - Priority: ").append(priority);
        if (!subTasks.isEmpty()) {
            details.append(" (Has ").append(subTasks.size()).append(" subtasks)");
        }
        return details.toString();
    }
    // ---------------------------------

    // --- Memento Pattern Methods ---
    public TodoMemento saveStateToMemento() {
        return new TodoMemento(this.id, this.title, this.description, this.status, this.priority);
    }

    public void restoreStateFromMemento(TodoMemento memento) {
        this.id = memento.getId();
        this.title = memento.getTitle();
        this.description = memento.getDescription();
        this.status = memento.getStatus();
        this.priority = memento.getPriority();
    }
    // -------------------------------

    // Member 2 - Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}