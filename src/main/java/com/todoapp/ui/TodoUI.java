package com.todoapp.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import com.todoapp.model.Todo;
import com.todoapp.service.TodoService;
import com.todoapp.service.observer.Observer; 
import com.todoapp.ui.facade.TodoFacade;     
import com.todoapp.ui.command.TodoInvoker;   
import com.todoapp.service.command.AddTodoCommand;
import com.todoapp.service.command.DeleteTodoCommand;
import com.todoapp.service.command.ToggleStatusCommand; // Make sure you created this!

public class TodoUI extends JFrame implements Observer {

    private TodoService todoService;
    private TodoFacade todoFacade;     
    private TodoInvoker todoInvoker;   

    private JPanel todoContainer;
    private JTextField inputField;
    private String currentFilter = "ALL"; // ALL, PENDING, COMPLETED

    // UI Colors & Fonts
    private final Color PRIMARY_COLOR = new Color(51, 102, 255);
    private final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private final Color DANGER_COLOR = new Color(220, 53, 69);
    private final Color BG_COLOR = new Color(245, 245, 245);
    private final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);

    public TodoUI() {
        this.todoService = TodoService.getInstance();
        this.todoService.registerObserver(this);
        
        this.todoFacade = new TodoFacade();
        this.todoInvoker = new TodoInvoker();

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Todo List Application");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // --- Header Section ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("My Tasks", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Filter Buttons
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        filterPanel.setOpaque(false); // Transparent to show blue header
        
        filterPanel.add(createFilterButton("All", "ALL"));
        filterPanel.add(createFilterButton("Active", "PENDING"));
        filterPanel.add(createFilterButton("Done", "COMPLETED"));
        
        headerPanel.add(filterPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // --- Main List Section ---
        todoContainer = new JPanel();
        todoContainer.setLayout(new BoxLayout(todoContainer, BoxLayout.Y_AXIS));
        todoContainer.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(todoContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // --- Footer Section ---
        JPanel footerPanel = new JPanel(new BorderLayout(10, 0));
        footerPanel.setBackground(BG_COLOR);
        footerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        inputField = new JTextField();
        inputField.setFont(MAIN_FONT);
        inputField.setPreferredSize(new Dimension(200, 40));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton addButton = new JButton("Add");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setBackground(PRIMARY_COLOR);
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);
        addButton.setOpaque(true);
        
        addButton.addActionListener(e -> {
            String text = inputField.getText();
            if (!text.trim().isEmpty()) {
                AddTodoCommand cmd = new AddTodoCommand(todoService, text, "");
                todoInvoker.execute(cmd);
                inputField.setText("");
            }
        });

        footerPanel.add(inputField, BorderLayout.CENTER);
        footerPanel.add(addButton, BorderLayout.EAST);
        add(footerPanel, BorderLayout.SOUTH);

        refreshList();
    }
    
    private JButton createFilterButton(String text, String filterType) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setForeground(PRIMARY_COLOR);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        btn.addActionListener(e -> {
            this.currentFilter = filterType;
            refreshList();
        });
        return btn;
    }

    @Override
    public void update(String message) {
        refreshList();
    }

    private void refreshList() {
        todoContainer.removeAll();
        
        // 1. Get List based on Facade/Filter
        List<Todo> todos;
        if ("PENDING".equals(currentFilter)) {
            todos = todoFacade.getPending();
        } else if ("COMPLETED".equals(currentFilter)) {
            todos = todoFacade.getCompleted();
        } else {
            todos = todoFacade.getAll();
        }

        // 2. Build UI Cards
        for (Todo todo : todos) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            itemPanel.setBackground(Color.WHITE);
            itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                new EmptyBorder(10, 15, 10, 15)
            ));

            // Checkbox for "Done"
            JCheckBox doneBox = new JCheckBox();
            doneBox.setBackground(Color.WHITE);
            doneBox.setSelected("COMPLETED".equals(todo.getStatus()));
            doneBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Action: Toggle Status
            doneBox.addActionListener(e -> {
                ToggleStatusCommand cmd = new ToggleStatusCommand(todoService, todo.getId());
                todoInvoker.execute(cmd);
            });

            // Title Label
            JLabel titleLbl = new JLabel(todo.getTitle());
            titleLbl.setFont(MAIN_FONT);
            
            if (doneBox.isSelected()) {
                titleLbl.setForeground(Color.GRAY);
                titleLbl.setText("<html><strike>" + todo.getTitle() + "</strike></html>");
            } else {
                titleLbl.setForeground(Color.DARK_GRAY);
            }

            // Right Side Panel (Priority + Delete)
            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            rightPanel.setBackground(Color.WHITE);

            // Delete Button
            JButton deleteBtn = new JButton("×");
            deleteBtn.setFont(new Font("Arial", Font.BOLD, 20));
            deleteBtn.setForeground(DANGER_COLOR);
            deleteBtn.setBorder(null);
            deleteBtn.setContentAreaFilled(false);
            deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            deleteBtn.addActionListener(e -> {
                DeleteTodoCommand cmd = new DeleteTodoCommand(todoService, todo.getId());
                todoInvoker.execute(cmd);
            });

            rightPanel.add(deleteBtn);

            itemPanel.add(doneBox, BorderLayout.WEST);
            itemPanel.add(titleLbl, BorderLayout.CENTER);
            itemPanel.add(rightPanel, BorderLayout.EAST);
            
            todoContainer.add(itemPanel);
        }
        
        todoContainer.revalidate();
        todoContainer.repaint();
    }
}
