package com.todoapp.ui;

import com.todoapp.model.Todo;
import com.todoapp.service.TodoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

public class TodoUI extends JFrame {
    private final TodoService todoService;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField searchField;
    private JLabel statusLabel;
    private JLabel titleLabel;
    private JSpinner dateSpinner;
    private JPanel todoContainer; // Changed from JList to JPanel
    private JScrollPane todoScrollPane;

    // Color Scheme
    private static final Color HEADER_COLOR = new Color(49, 78, 237);         // #314EED
    private static final Color SIDEBAR_COLOR = new Color(28, 28, 28);         // #1C1C1C
    private static final Color BACKGROUND_COLOR = new Color(13, 13, 13);      // #0D0D0D
    private static final Color CARD_COLOR = new Color(35, 35, 35);            // Slightly lighter than background
    private static final Color TEXT_COLOR = new Color(220, 220, 220);         // Light Gray #dcdcdc
    private static final Color TEXT_SECONDARY = new Color(160, 160, 160);     // Medium Gray #a0a0a0
    private static final Color BORDER_COLOR = new Color(80, 80, 80);          // Dark Border #505050

    public TodoUI() {
        this.todoService = new TodoService();
        initializeModernUI();
        updateStatusLabel();
        loadInitialTodos();
    }

    private void loadInitialTodos() {
        refreshTodoList(todoService.getAllTodos());
    }

    private void initializeModernUI() {
        setTitle("Todo List - Modern Design");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        setSize(1200, 800);
        setMinimumSize(new Dimension(1000, 700));

        getContentPane().setBackground(BACKGROUND_COLOR);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createStatusPanel(), BorderLayout.SOUTH);

        setLocationRelativeTo(null);

        try {
            setIconImage(createAppIcon());
        } catch (Exception e) {
            // Icon creation failed, continue without it
        }
    }

    private Image createAppIcon() {
        BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = icon.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(HEADER_COLOR);
        g2.fillRoundRect(4, 4, 24, 24, 8, 8);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("T", 11, 22);
        g2.dispose();
        return icon;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color[] colors = {
                        new Color(0, 146, 69),    // #009245
                        new Color(252, 238, 33),  // #FCEE21
                        new Color(0, 168, 197),   // #00A8C5
                        new Color(217, 224, 33)   // #D9E021
                };

                LinearGradientPaint gradient = new LinearGradientPaint(
                        0, 0, getWidth(), 0,
                        new float[]{0.0f, 0.33f, 0.66f, 1.0f},
                        colors
                );

                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        headerPanel.setBorder(new EmptyBorder(25, 35, 25, 35));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);

        titleLabel = new JLabel("📝 My Todo List");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.BLACK);

        JLabel statsLabel = new JLabel("Stay organized, stay productive");
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statsLabel.setForeground(new Color(0, 0, 0, 180));

        leftPanel.add(titleLabel, BorderLayout.NORTH);
        leftPanel.add(statsLabel, BorderLayout.SOUTH);

        JPanel userProfilePanel = createUserProfilePanel();

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(userProfilePanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createUserProfilePanel() {
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        profilePanel.setOpaque(false);

        JLabel avatarLabel = new JLabel("GH") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(37, 170, 225));
                g2d.fillOval(0, 0, 45, 45);

                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (45 - fm.stringWidth("GH")) / 2;
                int y = (45 + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString("GH", x, y);

                g2d.dispose();
            }
        };
        avatarLabel.setPreferredSize(new Dimension(45, 45));

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setOpaque(false);
        userInfoPanel.setBorder(new EmptyBorder(0, 15, 0, 0));

        JLabel nameLabel = new JLabel("Guljar Hosen");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel roleLabel = new JLabel("🎓 Student");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        roleLabel.setForeground(new Color(0, 0, 0, 160));
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        userInfoPanel.add(nameLabel);
        userInfoPanel.add(roleLabel);

        profilePanel.add(avatarLabel);
        profilePanel.add(userInfoPanel);

        return profilePanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);

        JPanel leftPanel = createImprovedLeftPanel();
        leftPanel.setPreferredSize(new Dimension(380, 0));
        leftPanel.setBackground(SIDEBAR_COLOR);

        JPanel rightPanel = createRightPanel();

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createImprovedLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(SIDEBAR_COLOR);
        leftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(SIDEBAR_COLOR);
        contentPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        contentPanel.add(createImprovedAddTodoSection());
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(createFilterSection());
        contentPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        return leftPanel;
    }

    private JPanel createImprovedAddTodoSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(SIDEBAR_COLOR);

        JLabel sectionTitle = new JLabel("Add New Todo");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 18));
        sectionTitle.setForeground(TEXT_COLOR);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLbl = new JLabel("Title");
        titleLbl.setFont(new Font("Arial", Font.BOLD, 13));
        titleLbl.setForeground(TEXT_COLOR);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleField = new JTextField();
        titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        titleField.setAlignmentX(Component.LEFT_ALIGNMENT);
        styleImprovedTextField(titleField);

        JLabel descLbl = new JLabel("Description (Optional)");
        descLbl.setFont(new Font("Arial", Font.BOLD, 13));
        descLbl.setForeground(TEXT_COLOR);
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        descriptionArea = new JTextArea(4, 0);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        styleImprovedTextArea(descriptionArea);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        descScroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));

        JLabel dateLbl = new JLabel("Due Date (Optional)");
        dateLbl.setFont(new Font("Arial", Font.BOLD, 13));
        dateLbl.setForeground(TEXT_COLOR);
        dateLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        dateSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateSpinner.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        dateSpinner.setBackground(CARD_COLOR);
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) dateSpinner.getEditor();
        editor.getTextField().setBackground(CARD_COLOR);
        editor.getTextField().setForeground(TEXT_COLOR);

        JButton addBtn = createNewGradientButton("Add Todo");
        addBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        addBtn.setPreferredSize(new Dimension(300, 50));
        addBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        addBtn.setFont(new Font("Arial", Font.BOLD, 14));
        addBtn.addActionListener(e -> addTodo());

        section.add(sectionTitle);
        section.add(Box.createVerticalStrut(20));
        section.add(titleLbl);
        section.add(Box.createVerticalStrut(8));
        section.add(titleField);
        section.add(Box.createVerticalStrut(15));
        section.add(descLbl);
        section.add(Box.createVerticalStrut(8));
        section.add(descScroll);
        section.add(Box.createVerticalStrut(15));
        section.add(dateLbl);
        section.add(Box.createVerticalStrut(8));
        section.add(dateSpinner);
        section.add(Box.createVerticalStrut(20));
        section.add(addBtn);

        return section;
    }

    private JPanel createFilterSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(SIDEBAR_COLOR);

        JLabel sectionTitle = new JLabel("Filter & Search");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 18));
        sectionTitle.setForeground(TEXT_COLOR);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        searchField = new JTextField();
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        searchField.setAlignmentX(Component.LEFT_ALIGNMENT);
        styleImprovedTextField(searchField);
        searchField.addActionListener(e -> searchTodos());

        JButton searchBtn = createNewGradientButton("Search");
        searchBtn.addActionListener(e -> searchTodos());

        JButton allBtn = createNewGradientButton("All Todos");
        JButton pendingBtn = createNewGradientButton("Pending");
        JButton completedBtn = createNewGradientButton("Completed");

        allBtn.addActionListener(e -> showAllTodos());
        pendingBtn.addActionListener(e -> showPendingTodos());
        completedBtn.addActionListener(e -> showCompletedTodos());

        section.add(sectionTitle);
        section.add(Box.createVerticalStrut(15));
        section.add(searchField);
        section.add(Box.createVerticalStrut(10));
        section.add(searchBtn);
        section.add(Box.createVerticalStrut(15));
        section.add(allBtn);
        section.add(Box.createVerticalStrut(8));
        section.add(pendingBtn);
        section.add(Box.createVerticalStrut(8));
        section.add(completedBtn);

        return section;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.setBorder(new EmptyBorder(0, 0, 0, 25));

        rightPanel.add(createTodoListPanel(), BorderLayout.CENTER);

        return rightPanel;
    }

    // NEW: JPanel-based todo list instead of JList
    private JPanel createTodoListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(0, 20, 0, 20)
        ));

        JLabel headerLabel = new JLabel("Your Todos");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(TEXT_COLOR);
        headerLabel.setBorder(new EmptyBorder(15, 0, 15, 0));

        // Create container for todo items
        todoContainer = new JPanel();
        todoContainer.setLayout(new BoxLayout(todoContainer, BoxLayout.Y_AXIS));
        todoContainer.setBackground(CARD_COLOR);

        todoScrollPane = new JScrollPane(todoContainer);
        todoScrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        todoScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        todoScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(headerLabel, BorderLayout.NORTH);
        panel.add(todoScrollPane, BorderLayout.CENTER);

        return panel;
    }

    // NEW: Create individual todo item panel with interactive buttons
    private JPanel createTodoItemPanel(Todo todo) {
        JPanel todoPanel = new JPanel(new BorderLayout());
        todoPanel.setBackground(CARD_COLOR);
        todoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 1, 0, getTodoColor(todo)),
                new EmptyBorder(15, 20, 15, 20)
        ));
        todoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Left side - Todo info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(todo.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(getTodoColor(todo));

        String descText = todo.getDescription().isEmpty() ? "No description" : todo.getDescription();
        JLabel descLabel = new JLabel(descText);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(TEXT_SECONDARY);

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(descLabel);

        // Right side - Action buttons or completion status
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setOpaque(false);

        if (todo.isCompleted()) {
            // Show completion status instead of buttons
            JLabel completedLabel = new JLabel("✅ Completed");
            completedLabel.setFont(new Font("Arial", Font.BOLD, 16));
            completedLabel.setForeground(new Color(34, 197, 94)); // Green color
            completedLabel.setBorder(new EmptyBorder(8, 15, 8, 15));
            buttonPanel.add(completedLabel);
        } else {
            // Show action buttons for pending todos
            // Complete button (Green with checkmark)
            JButton completeBtn = createStatusButton("✓", "Mark as Complete", new Color(34, 197, 94));
            completeBtn.addActionListener(e -> {
                System.out.println("Marking todo as COMPLETE: " + todo.getTitle());
                completeTodo(todo);
            });

            // Pending button (Yellow with clock)
            JButton pendingBtn = createStatusButton("◷", "Mark as Pending", new Color(251, 191, 36));
            pendingBtn.addActionListener(e -> {
                System.out.println("Marking todo as PENDING: " + todo.getTitle());
                incompleteTodo(todo);
            });

            // Delete button (Red with X)
            JButton deleteBtn = createStatusButton("✕", "Delete Todo", new Color(239, 68, 68));
            deleteBtn.addActionListener(e -> {
                System.out.println("DELETING todo: " + todo.getTitle());
                deleteTodo(todo);
            });

            // Add all 3 buttons for pending todos
            buttonPanel.add(completeBtn);
            buttonPanel.add(pendingBtn);
            buttonPanel.add(deleteBtn);
        }

        todoPanel.add(infoPanel, BorderLayout.CENTER);
        todoPanel.add(buttonPanel, BorderLayout.EAST);

        return todoPanel;
    }

    // NEW: Create interactive status buttons
    private JButton createStatusButton(String emoji, String tooltip, Color baseColor) {
        JButton button = new JButton(emoji) {
            private boolean isHovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Create gradient based on base color
                Color lightColor = isHovered ? baseColor.darker() : baseColor;
                Color darkColor = isHovered ? baseColor.darker().darker() : baseColor.darker();

                LinearGradientPaint gradient = new LinearGradientPaint(
                        0, 0, getWidth(), 0,
                        new float[]{0.0f, 1.0f},
                        new Color[]{lightColor, darkColor}
                );

                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Draw emoji
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);

                g2d.dispose();
            }

            public void setHovered(boolean hovered) {
                this.isHovered = hovered;
                repaint();
            }
        };

        button.setPreferredSize(new Dimension(50, 35));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setToolTipText(tooltip);

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                try {
                    button.getClass().getMethod("setHovered", boolean.class).invoke(button, true);
                } catch (Exception ex) {
                    // Fallback
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                try {
                    button.getClass().getMethod("setHovered", boolean.class).invoke(button, false);
                } catch (Exception ex) {
                    // Fallback
                }
            }
        });

        return button;
    }

    // NEW: Refresh the todo list display
    private void refreshTodoList(List<Todo> todos) {
        todoContainer.removeAll();

        if (todos.isEmpty()) {
            JLabel emptyLabel = new JLabel("No todos found. Add one to get started!");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            emptyLabel.setForeground(TEXT_SECONDARY);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            emptyLabel.setBorder(new EmptyBorder(50, 20, 50, 20));
            todoContainer.add(emptyLabel);
        } else {
            for (Todo todo : todos) {
                todoContainer.add(createTodoItemPanel(todo));
                todoContainer.add(Box.createVerticalStrut(5)); // Small gap between items
            }
        }

        todoContainer.revalidate();
        todoContainer.repaint();
        updateStatusLabel();
    }

    private Color getTodoColor(Todo todo) {
        Color[] colors = {
                new Color(255, 99, 132),   // Pink/Red
                new Color(54, 162, 235),   // Blue
                new Color(255, 205, 86),   // Yellow
                new Color(75, 192, 192),   // Teal
                new Color(153, 102, 255),  // Purple
                new Color(255, 159, 64),   // Orange
                new Color(199, 199, 199),  // Grey
                new Color(83, 102, 255),   // Indigo
                new Color(255, 99, 255),   // Magenta
                new Color(99, 255, 132)    // Green
        };
        return colors[Math.abs(todo.getId()) % colors.length];
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SIDEBAR_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
                new EmptyBorder(15, 35, 15, 35)
        ));

        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Arial", Font.BOLD, 13));
        statusLabel.setForeground(TEXT_SECONDARY);

        panel.add(statusLabel, BorderLayout.WEST);

        return panel;
    }

    private JButton createNewGradientButton(String text) {
        JButton button = new JButton(text) {
            private boolean isHovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color startColor, endColor;
                if (isHovered) {
                    startColor = new Color(0, 116, 55);
                    endColor = new Color(0, 134, 157);
                } else {
                    startColor = new Color(0, 146, 69);
                    endColor = new Color(0, 168, 197);
                }

                LinearGradientPaint gradient = new LinearGradientPaint(
                        0, 0, getWidth(), 0,
                        new float[]{0.0f, 1.0f},
                        new Color[]{startColor, endColor}
                );

                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2d.setColor(Color.BLACK);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);

                g2d.dispose();
            }

            public void setHovered(boolean hovered) {
                this.isHovered = hovered;
                repaint();
            }
        };

        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setForeground(Color.BLACK);
        button.setBorder(new EmptyBorder(15, 25, 15, 25));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setPreferredSize(new Dimension(280, 45));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                try {
                    button.getClass().getMethod("setHovered", boolean.class).invoke(button, true);
                } catch (Exception ex) {
                    // Fallback
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                try {
                    button.getClass().getMethod("setHovered", boolean.class).invoke(button, false);
                } catch (Exception ex) {
                    // Fallback
                }
            }
        });

        return button;
    }

    private void styleImprovedTextField(JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(12, 15, 12, 15)
        ));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBackground(CARD_COLOR);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
    }

    private void styleImprovedTextArea(JTextArea area) {
        area.setBorder(new EmptyBorder(12, 15, 12, 15));
        area.setFont(new Font("Arial", Font.PLAIN, 14));
        area.setBackground(CARD_COLOR);
        area.setForeground(TEXT_COLOR);
        area.setCaretColor(TEXT_COLOR);
    }

    // Action methods - Updated to use refreshTodoList
    private void addTodo() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a title for the todo.", "Title Required", JOptionPane.WARNING_MESSAGE);
            titleField.requestFocus();
            return;
        }

        try {
            Todo todo = todoService.addTodo(title, description);
            System.out.println("ADDED new todo: " + todo.getTitle());

            titleField.setText("");
            descriptionArea.setText("");
            titleField.requestFocus();

            refreshTodoList(todoService.getAllTodos());
            showSuccessMessage("Todo added successfully!");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTodo(Todo todo) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete:\n\"" + todo.getTitle() + "\"?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            todoService.deleteTodo(todo.getId());
            refreshTodoList(todoService.getAllTodos());
            showSuccessMessage("Todo deleted successfully!");
        }
    }

    private void completeTodo(Todo todo) {
        if (!todo.isCompleted()) {
            todoService.completeTodo(todo.getId());
            refreshTodoList(todoService.getAllTodos());
            showSuccessMessage("Todo marked as completed! 🎉");
        } else {
            JOptionPane.showMessageDialog(this, "This todo is already completed.", "Already Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void incompleteTodo(Todo todo) {
        if (todo.isCompleted()) {
            todoService.uncompleteTodo(todo.getId());
            refreshTodoList(todoService.getAllTodos());
            showSuccessMessage("Todo marked as pending.");
        } else {
            JOptionPane.showMessageDialog(this, "This todo is already pending.", "Already Pending", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void searchTodos() {
        String keyword = searchField.getText().trim();
        refreshTodoList(todoService.searchTodos(keyword));
    }

    private void showAllTodos() {
        refreshTodoList(todoService.getAllTodos());
        searchField.setText("");
    }

    private void showPendingTodos() {
        refreshTodoList(todoService.getPendingTodos());
        searchField.setText("");
    }

    private void showCompletedTodos() {
        refreshTodoList(todoService.getCompletedTodos());
        searchField.setText("");
    }

    private void updateStatusLabel() {
        statusLabel.setText(String.format("Total: %d | Pending: %d | Completed: %d",
                todoService.getTotalCount(),
                todoService.getPendingCount(),
                todoService.getCompletedCount()));
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}