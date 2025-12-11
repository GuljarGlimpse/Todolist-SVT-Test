package com.todoapp.service;

import com.todoapp.model.Todo;
import com.todoapp.model.TodoFactory;
import com.todoapp.service.iterator.Container;
import com.todoapp.service.iterator.Iterator;
import com.todoapp.service.observer.Observer;
import com.todoapp.service.observer.Subject;
import com.todoapp.service.strategy.SearchStrategy;
import com.todoapp.service.strategy.TitleSearch;

import java.util.*;
import java.util.stream.Collectors;

// UPDATED: Now implements Subject as well
public class TodoService implements Container, Subject {

    private static TodoService instance;
    private List<Todo> todos;
    private int nextId;
    
    // Observer List
    private List<Observer> observers = new ArrayList<>();

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

    // --- OBSERVER PATTERN IMPLEMENTATION ---
    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
    // ---------------------------------------

    // --- ITERATOR PATTERN ---
    @Override
    public Iterator getIterator() {
        return new TodoIterator();
    }

    private class TodoIterator implements Iterator {
        int index;
        @Override
        public boolean hasNext() { return index < todos.size(); }
        @Override
        public Object next() { return hasNext() ? todos.get(index++) : null; }
    }
    // ------------------------

    // --- STRATEGY PATTERN ---
    public List<Todo> searchTodos(String keyword, SearchStrategy strategy) {
        if (keyword == null || keyword.trim().isEmpty()) return getAllTodos();
        return todos.stream().filter(t -> strategy.matches(t, keyword)).collect(Collectors.toList());
    }
    
    public List<Todo> searchTodos(String keyword) {
        return searchTodos(keyword, new TitleSearch());
    }
    // ------------------------

    public Todo addTodo(String title, String description) {
        if (title == null || title.trim().isEmpty()) throw new IllegalArgumentException("Title cannot be empty");
        
        Todo todo = TodoFactory.createTodo(nextId++, title.trim(), description != null ? description.trim() : "");
        todos.add(todo);
        
        // NOTIFY OBSERVERS
        notifyObservers("Added: " + title);
        
        return todo;
    }

    public boolean deleteTodo(int id) {
        boolean removed = todos.removeIf(todo -> todo.getId() == id);
        if (removed) {
            notifyObservers("Deleted ID: " + id);
        }
        return removed;
    }

    public boolean completeTodo(int id) {
        Optional<Todo> todo = findTodoById(id);
        if (todo.isPresent()) {
            todo.get().setCompleted(true);
            notifyObservers("Completed ID: " + id);
            return true;
        }
        return false;
    }

    // Keep other methods the same...
    public boolean uncompleteTodo(int id) {
        Optional<Todo> todo = findTodoById(id);
        if (todo.isPresent()) {
            todo.get().setCompleted(false);
            return true; // No notify needed for now, or add it if you want
        }
        return false;
    }

    public Optional<Todo> findTodoById(int id) {
        return todos.stream().filter(todo -> todo.getId() == id).findFirst();
    }

    public List<Todo> getAllTodos() { return new ArrayList<>(todos); }
    public List<Todo> getCompletedTodos() { return todos.stream().filter(Todo::isCompleted).collect(Collectors.toList()); }
    public List<Todo> getPendingTodos() { return todos.stream().filter(todo -> !todo.isCompleted).collect(Collectors.toList()); }
    public int getTotalCount() { return todos.size(); }
    public int getCompletedCount() { return (int) todos.stream().filter(Todo::isCompleted).count(); }
    public int getPendingCount() { return getTotalCount() - getCompletedCount(); }
    public void clearAllTodos() { todos.clear(); nextId = 1; notifyObservers("Cleared all"); }
}
