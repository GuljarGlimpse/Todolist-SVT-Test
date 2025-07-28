
// Member 2 - Data Model Enhancement
package com.todoapp.model;

public class Todo {
    private int id;
    private String title;
    private String description;
    private String status;
    private String priority;
    
    // Member 2 implementation - Constructor
    public Todo(int id, String title, String description, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = "MEDIUM";
    }
    
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

