package com.todoapp.service;

import com.todoapp.model.Todo;
import java.util.*;
import java.util.stream.Collectors;

public class TodoService {
    private List<Todo> todos;
    private int nextId;

    public TodoService() {
        this.todos = new ArrayList<>();
        this.nextId = 1;
    }

    public Todo addTodo(String title, String description) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (title.length() > 100) {
            throw new IllegalArgumentException("Title cannot exceed 100 characters");
        }
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("Description cannot exceed 500 characters");
        }

        Todo todo = new Todo(nextId++, title.trim(), description != null ? description.trim() : "");
        todos.add(todo);
        return todo;
    }

    public boolean deleteTodo(int id) {
        return todos.removeIf(todo -> todo.getId() == id);
    }

    public boolean completeTodo(int id) {
        Optional<Todo> todo = findTodoById(id);
        if (todo.isPresent()) {
            todo.get().setCompleted(true);
            return true;
        }
        return false;
    }

    public boolean uncompleteTodo(int id) {
        Optional<Todo> todo = findTodoById(id);
        if (todo.isPresent()) {
            todo.get().setCompleted(false);
            return true;
        }
        return false;
    }

    public Optional<Todo> findTodoById(int id) {
        return todos.stream()
                .filter(todo -> todo.getId() == id)
                .findFirst();
    }

    public List<Todo> getAllTodos() {
        return new ArrayList<>(todos);
    }

    public List<Todo> getCompletedTodos() {
        return todos.stream()
                .filter(Todo::isCompleted)
                .collect(Collectors.toList());
    }

    public List<Todo> getPendingTodos() {
        return todos.stream()
                .filter(todo -> !todo.isCompleted())
                .collect(Collectors.toList());
    }

    public List<Todo> searchTodos(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllTodos();
        }

        String lowerKeyword = keyword.toLowerCase();
        return todos.stream()
                .filter(todo ->
                        todo.getTitle().toLowerCase().contains(lowerKeyword) ||
                                todo.getDescription().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    public int getTotalCount() {
        return todos.size();
    }

    public int getCompletedCount() {
        return (int) todos.stream().filter(Todo::isCompleted).count();
    }

    public int getPendingCount() {
        return getTotalCount() - getCompletedCount();
    }

    public void clearAllTodos() {
        todos.clear();
        nextId = 1;
    }
}

    public Todo getTodoById(int id) {
        for (Todo todo : todos) {
            if (todo.getId() == id) {
                return todo;
            }
        }
        return null;
    }
    
    public List<Todo> searchTodos(String keyword) {
        List<Todo> results = new ArrayList<>();
        for (Todo todo : todos) {
            if (todo.getTitle().contains(keyword)) {
                results.add(todo);
            }
        }
        return results;
    }


    // Validation and error handling - Leader implementation
    public boolean validateTodo(String title) {
        return title != null && title.length() > 0;
    }
    
    public void displayAllTodos() {
        if (todos.isEmpty()) {
            System.out.println("No todos found");
            return;
        }
        System.out.println("=== All Todos ===");
        for (Todo todo : todos) {
            System.out.println(todo.toString());
        }
    }
    
    public int getTotalTodos() {
        return todos.size();
    }

