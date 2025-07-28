
// Member 3 - Enhanced User Interface Implementation
package com.todoapp.ui;

import com.todoapp.model.Todo;
import java.util.List;

public class TodoUI {
    
    // Member 3 - Display methods
    public void displayWelcomeMessage() {
        System.out.println("==========================================");
        System.out.println("    Welcome to TodoList Application     ");
        System.out.println("==========================================");
    }
    
    public void displayTodoList(List<Todo> todos) {
        System.out.println("
=== Your Todo List ===");
        if (todos.isEmpty()) {
            System.out.println("No todos found!");
            return;
        }
        
        for (Todo todo : todos) {
            System.out.printf("ID: %d | %s | Status: %s | Priority: %s%n", 
                todo.getId(), todo.getTitle(), todo.getStatus(), todo.getPriority());
        }
    }
    
    // Member 3 - Sorting and filtering UI
    public void showSortingOptions() {
        System.out.println("
Sort Options:");
        System.out.println("1. Sort by Priority");
        System.out.println("2. Sort by Status");
        System.out.println("3. Sort by ID");
    }
    
    public void showFilterOptions() {
        System.out.println("
Filter Options:");
        System.out.println("1. Show Pending Tasks");
        System.out.println("2. Show Completed Tasks");
        System.out.println("3. Show High Priority");
    }
    
    // Member 3 - Statistics display
    public void displayStatistics(List<Todo> todos) {
        long completed = todos.stream().filter(t -> "COMPLETED".equals(t.getStatus())).count();
        long pending = todos.stream().filter(t -> "PENDING".equals(t.getStatus())).count();
        
        System.out.println("
=== Statistics ===");
        System.out.println("Total Todos: " + todos.size());
        System.out.println("Completed: " + completed);
        System.out.println("Pending: " + pending);
    }
}

