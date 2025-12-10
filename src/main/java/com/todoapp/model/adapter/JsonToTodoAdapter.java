package com.todoapp.model.adapter;

import com.todoapp.model.Todo;
import org.json.JSONObject;

// Adapter Implementation - matches AdapterClient.java
public class JsonToTodoAdapter implements ITodoAdapter {

    @Override
    public Todo convertToTodo(JsonTask jsonTask) {
        try {
            JSONObject jsonObject = new JSONObject(jsonTask.getJsonData());

            int id = jsonObject.optInt("id", 0);
            String title = jsonObject.getString("title");
            String description = jsonObject.optString("description", "No description");
            String status = jsonObject.optString("status", "PENDING");
            String priority = jsonObject.optString("priority", "MEDIUM");

            Todo todo = new Todo(id, title, description, status, priority);

            System.out.println("✓ Successfully converted JSON to Todo: " + title);
            return todo;

        } catch (Exception e) {
            System.err.println("✗ Error parsing JSON: " + e.getMessage());
            // Return a default todo if parsing fails
            return new Todo(0, "Error Task", "Failed to parse JSON data", "ERROR", "HIGH");
        }
    }

    @Override
    public JsonTask convertToJson(Todo todo) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", todo.getId());
            jsonObject.put("title", todo.getTitle());
            jsonObject.put("description", todo.getDescription());
            jsonObject.put("status", todo.getStatus());
            jsonObject.put("priority", todo.getPriority());
            jsonObject.put("type", "todo");

            JsonTask jsonTask = new JsonTask(jsonObject.toString());
            System.out.println("✓ Converted Todo to JSON: " + todo.getTitle());
            return jsonTask;
        } catch (Exception e) {
            System.err.println("✗ Error converting to JSON: " + e.getMessage());
            return new JsonTask("{\"error\": \"Failed to convert todo to JSON\"}");
        }
    }
}