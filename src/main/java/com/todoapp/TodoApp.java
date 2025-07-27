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