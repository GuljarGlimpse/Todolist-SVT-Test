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

