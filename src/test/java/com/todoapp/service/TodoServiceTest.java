package com.todoapp.service;

import com.todoapp.model.Todo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
// import org.mockito.Spy; // No longer needed on the field
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    // 1. @Spy annotation removed from here
    private TodoService todoService;

    // 2. setUp() method is updated
    @BeforeEach
    void setUp() {
        // Get the Singleton instance and spy on it
        todoService = spy(TodoService.getInstance());
        
        // Now clear all todos to reset the state for the test
        todoService.clearAllTodos();
    }

    @Test
    @DisplayName("Should add todo successfully")
    void testAddTodo() {
        Todo todo = todoService.addTodo("Buy groceries", "From supermarket");

        assertNotNull(todo);
        assertEquals("Buy groceries", todo.getTitle());
        assertEquals("From supermarket", todo.getDescription());
        assertFalse(todo.isCompleted());
        assertEquals(1, todoService.getTotalCount());

        verify(todoService).addTodo("Buy groceries", "From supermarket");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    @DisplayName("Should reject invalid titles")
    void testInvalidTitles(String invalidTitle) {
        assertThrows(IllegalArgumentException.class, () -> {
            todoService.addTodo(invalidTitle, "Description");
        });
        assertEquals(0, todoService.getTotalCount());
    }

    @ParameterizedTest
    @CsvSource({
            "'Buy milk', 'From grocery store'",
            "'Call doctor', 'Book appointment'",
            "'Exercise', 'Go for run'",
            "'Read book', 'Finish novel'"
    })
    @DisplayName("Should create todos with different data")
    void testCreateTodos(String title, String description) {
        Todo todo = todoService.addTodo(title, description);

        assertEquals(title, todo.getTitle());
        assertEquals(description, todo.getDescription());
        assertFalse(todo.isCompleted());

        verify(todoService).addTodo(title, description);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/todo-filter.csv", numLinesToSkip = 1)
    @DisplayName("Should filter todos based on CSV file data")
    void testTodoFiltering(String title, String description, boolean completed,
                           String filterType, boolean shouldBeIncluded) {
        // Add the todo
        Todo todo = todoService.addTodo(title, description);

        // Complete it if needed
        if (completed) {
            todoService.completeTodo(todo.getId());
        }

        // Test filtering
        List<Todo> result;
        if (filterType.equals("completed")) {
            result = todoService.getCompletedTodos();
        } else if (filterType.equals("pending")) {
            result = todoService.getPendingTodos();
        } else {
            result = todoService.getAllTodos();
        }

        if (shouldBeIncluded) {
            assertFalse(result.isEmpty());
            assertTrue(result.stream().anyMatch(t -> t.getTitle().equals(title)));
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/todo-status.csv", numLinesToSkip = 1)
    @DisplayName("Should handle todo completion based on CSV file")
    void testTodoCompletion(int id, String title, String description, boolean shouldComplete) {
        Todo todo = todoService.addTodo(title, description);

        if (shouldComplete) {
            boolean result = todoService.completeTodo(todo.getId());
            assertTrue(result);
            assertTrue(todo.isCompleted());
        } else {
            assertFalse(todo.isCompleted());
        }

        assertEquals(title, todo.getTitle());
        assertEquals(description, todo.getDescription());
    }

    @ParameterizedTest
    @MethodSource("getSearchData")
    @DisplayName("Should search todos with various terms")
    void testSearchTodos(String searchTerm, int expectedMinCount) {
        // Setup test data
        todoService.addTodo("Buy milk", "From store");
        todoService.addTodo("Buy bread", "Fresh bread");
        todoService.addTodo("Call mom", "Weekly call");

        List<Todo> results = todoService.searchTodos(searchTerm);

        assertTrue(results.size() >= expectedMinCount);
        verify(todoService).searchTodos(searchTerm);
    }

    static Stream<Object[]> getSearchData() {
        return Stream.of(
                new Object[]{"buy", 2},
                new Object[]{"call", 1},
                new Object[]{"xyz", 0}
        );
    }

    @Test
    @DisplayName("Should delete todo")
    void testDeleteTodo() {
        Todo todo = todoService.addTodo("Task to delete", "Description");
        int todoId = todo.getId();

        boolean deleted = todoService.deleteTodo(todoId);

        assertTrue(deleted);
        assertEquals(0, todoService.getTotalCount());
        verify(todoService).deleteTodo(todoId);
    }

    @Test
    @DisplayName("Should get todo counts correctly")
    void testGetCounts() {
        todoService.addTodo("Task 1", "Description 1");
        todoService.addTodo("Task 2", "Description 2");
        todoService.addTodo("Task 3", "Description 3");

        // Complete one task
        todoService.completeTodo(1);

        assertEquals(3, todoService.getTotalCount());
        assertEquals(1, todoService.getCompletedCount());
        assertEquals(2, todoService.getPendingCount());

        verify(todoService, times(3)).addTodo(anyString(), anyString());
        verify(todoService).completeTodo(1);
    }

    @Test
    @DisplayName("Should find todo by ID")
    void testFindTodoById() {
        Todo todo = todoService.addTodo("Find me", "Description");

        Optional<Todo> found = todoService.findTodoById(todo.getId());
        Optional<Todo> notFound = todoService.findTodoById(999);

        assertTrue(found.isPresent());
        assertEquals(todo.getId(), found.get().getId());
        assertFalse(notFound.isPresent());

        verify(todoService).findTodoById(todo.getId());
        verify(todoService).findTodoById(999);
    }

    @Test
    @DisplayName("Should handle null title")
    void testNullTitle() {
        assertThrows(IllegalArgumentException.class, () -> {
            todoService.addTodo(null, "Description");
        });
        assertEquals(0, todoService.getTotalCount());
    }

    @Test
    @DisplayName("Should get completed and pending todos")
    void testGetCompletedAndPendingTodos() {
        todoService.addTodo("Pending task", "Description");
        todoService.addTodo("Completed task", "Description");
        todoService.completeTodo(2);

        List<Todo> completed = todoService.getCompletedTodos();
        List<Todo> pending = todoService.getPendingTodos();

        assertEquals(1, completed.size());
        assertEquals(1, pending.size());
        assertTrue(completed.get(0).isCompleted());
        assertFalse(pending.get(0).isCompleted());

        verify(todoService).getCompletedTodos();
        verify(todoService).getPendingTodos();
    }
}
