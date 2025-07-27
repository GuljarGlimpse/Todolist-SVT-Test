package com.todoapp;

import com.todoapp.ui.TodoUI;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TodoApp {
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());  // ✅ Note: SystemS
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }

        // Create and show the UI
        SwingUtilities.invokeLater(() -> {
            new TodoUI().setVisible(true);
        });
    }
}
// Main menu system implementation
public void showMainMenu() {
    System.out.println("=== TodoList Application ===");
    System.out.println("1. Add Todo");
    System.out.println("2. View All Todos");
    System.out.println("3. Update Todo");
    System.out.println("4. Delete Todo");
    System.out.println("5. Exit");
}


    // User input handling - Leader implementation
    public void handleUserInput(int choice) {
        switch(choice) {
            case 1:
                System.out.println("Adding new todo...");
                break;
            case 2:
                System.out.println("Displaying all todos...");
                break;
            case 3:
                System.out.println("Updating todo...");
                break;
            case 4:
                System.out.println("Deleting todo...");
                break;
            case 5:
                System.out.println("Goodbye!");
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

