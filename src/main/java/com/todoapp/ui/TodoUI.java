package com.todoapp.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.todoapp.model.Todo;
import com.todoapp.service.TodoService;
import com.todoapp.service.observer.Observer; // Pattern 1
import com.todoapp.ui.facade.TodoFacade;     // Pattern 2
import com.todoapp.ui.command.TodoInvoker;   // Pattern 3
import com.todoapp.service.command.AddTodoCommand;
import com.todoapp.service.command.DeleteTodoCommand;

// 1. Implement Observer
public class TodoUI extends JFrame implements Observer {

    private TodoService todoService;
    private TodoFacade todoFacade;     // Facade
    private TodoInvoker todoInvoker;   // Invoker

    private JPanel todoContainer;
    private JTextField inputField;

    public TodoUI() {
        this.todoService = TodoService.getInstance();
        
        // Register as Observer
        this.todoService.registerObserver(this);
        
        // Initialize Patterns
        this.todoFacade = new TodoFacade();
        this.todoInvoker = new TodoInvoker();

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Todo App (Observer + Facade + Command)");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel title = new JLabel("My Todo List", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // List Area
        todoContainer = new JPanel();
        todoContainer.setLayout(new BoxLayout(todoContainer, BoxLayout.Y_AXIS));
        add(new JScrollPane(todoContainer), BorderLayout.CENTER);

        // Footer (Input + Add Button)
        JPanel footer = new JPanel();
        inputField = new JTextField(15);
        JButton addButton = new JButton("Add");
        
        // Button Click -> Command -> Invoker
        addButton.addActionListener(e -> {
            String text = inputField.getText();
            if (!text.isEmpty()) {
                // Creates Command
                AddTodoCommand cmd = new AddTodoCommand(todoService, text, "Created via UI");
                // Invoker executes it
                todoInvoker.execute(cmd);
                inputField.setText("");
            }
        });

        footer.add(inputField);
        footer.add(addButton);
        add(footer, BorderLayout.SOUTH);

        refreshList();
    }

    // 2. Observer Update Method
    @Override
    public void update(String message) {
        System.out.println("UI Received Update: " + message);
        // Automatically refresh when Service notifies us
        refreshList();
    }

    private void refreshList() {
        todoContainer.removeAll();
        
        // Use Facade to get data
        List<Todo> todos = todoFacade.getAll();

        for (Todo todo : todos) {
            JPanel item = new JPanel(new BorderLayout());
            item.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.LIGHT_GRAY));
            item.setMaximumSize(new Dimension(400, 30));
            
            JLabel lbl = new JLabel(" " + todo.getTitle());
            JButton delBtn = new JButton("X");
            delBtn.setForeground(Color.RED);
            delBtn.setBorderPainted(false);
            
            delBtn.addActionListener(e -> {
                // Command + Invoker for delete
                DeleteTodoCommand cmd = new DeleteTodoCommand(todoService, todo.getId());
                todoInvoker.execute(cmd);
            });

            item.add(lbl, BorderLayout.CENTER);
            item.add(delBtn, BorderLayout.EAST);
            todoContainer.add(item);
        }
        
        todoContainer.revalidate();
        todoContainer.repaint();
    }
}
