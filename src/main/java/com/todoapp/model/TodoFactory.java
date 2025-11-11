
package com.todoapp.model;

public class TodoFactory {

    /**
     * Creates a new Todo object.
     * This factory method ensures all new todos are created
     * with a default "PENDING" status.
     */
    public static Todo createTodo(int id, String title, String description) {

        // This correctly calls the 4-argument constructor from Todo.java
        return new Todo(id, title, description, "PENDING");
    }
}