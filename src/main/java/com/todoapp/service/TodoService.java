package com.todoapp.service;

import com.todoapp.model.Todo;
import com.todoapp.model.TodoFactory;
import com.todoapp.service.iterator.Container;
import com.todoapp.service.iterator.Iterator;
import com.todoapp.service.strategy.SearchStrategy;
import com.todoapp.service.strategy.TitleSearch;

import java.util.*;
import java.util.stream.Collectors;

// PATTERN: Iterator (Implements Container)
public class TodoService implements Container {

    // PATTERN: Singleton
    private static TodoService instance;
    private List<Todo> todos;
    private int nextId;

    private TodoService() {
        this.todos = new ArrayList<>();
        this.nextId = 1;
    }

    public static TodoService getInstance() {
        if (instance == null) {
            instance = new TodoService();
        }
        return instance;
    }

    // --- PATTERN: Iterator Implementation ---
    @Override
    public Iterator getIterator() {
        return new TodoIterator();
    }

    private class TodoIterator implements Iterator {
        int index;

        @Override
        public boolean hasNext() {
            return index < todos.size();
        }

        @Override
        public Object next() {
            if (this.hasNext()) {
                return todos.get(index++);
            }
            return null;
        }
    }
    // ----------------------------------------

    // --- PATTERN: Strategy Pattern Usage ---
    public List<Todo> searchTodos(String keyword, SearchStrategy strategy) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllTodos();
        }
        // Use the strategy to check for matches
        return todos.stream()
                .filter(todo -> strategy.matches(todo, keyword))
                .collect(Collectors.toList());
    }

    // Backward compatibility (defaults to Title search)
    public List<Todo> searchTodos(String keyword) {
        return searchTodos(keyword, new TitleSearch());
    }
    // ---------------------------------------

    // --- Standard Methods (Used by Command Pattern) ---
    public Todo addTodo(String title, String description) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        // PATTERN: Factory (Member 2's code)
        Todo todo = TodoFactory.createTodo(nextId++, title.trim(), description != null ? description.trim() : "");
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
        return todos.stream().filter(todo -> todo.getId() == id).findFirst();
    }

    public List<Todo> getAllTodos() {
        return new ArrayList<>(todos);
    }

    public List<Todo> getCompletedTodos() {
        return todos.stream().filter(Todo::isCompleted).collect(Collectors.toList());
    }

    public List<Todo> getPendingTodos() {
        return todos.stream().filter(todo -> !todo.isCompleted).collect(Collectors.toList());
    }

    public int getTotalCount() { return todos.size(); }
    public int getCompletedCount() { return (int) todos.stream().filter(Todo::isCompleted).count(); }
    public int getPendingCount() { return getTotalCount() - getCompletedCount(); }
    public void clearAllTodos() { todos.clear(); nextId = 1; }
}
