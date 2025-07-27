package com.todoapp.service;

import com.todoapp.model.Todo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    private TodoService todoService;

    @Mock
    private TodoService mockTodoService;

    @BeforeEach
    void setUp() {
        // Initialize real service for testing
        todoService = new TodoService();
    }

    @ParameterizedTest
    @DisplayName("Test adding todos with valid titles using @ValueSource")
    @ValueSource(strings = {"Buy groceries", "Meeting at 3pm", "Call mom", "Finish project", "Read book"})
    void testAddTodoWithValidTitles(String title) {
        Todo todo = todoService.addTodo(title, "Test description");

        assertNotNull(todo);
        assertEquals(title, todo.getTitle());
        assertEquals("Test description", todo.getDescription());
        assertFalse(todo.isCompleted());
        assertEquals(1, todoService.getTotalCount());

        todoService.clearAllTodos(); // Clean up for next test
    }

    @ParameterizedTest
    @DisplayName("Test adding todos with invalid titles using @ValueSource")
    @ValueSource(strings = {"", "   "})
    void testAddTodoWithInvalidTitles(String invalidTitle) {
        assertThrows(IllegalArgumentException.class, () -> {
            todoService.addTodo(invalidTitle, "Description");
        });
        assertEquals(0, todoService.getTotalCount());
    }

    @ParameterizedTest
    @DisplayName("Test todo creation with various data using @CsvSource")
    @CsvSource({
            "Buy milk, From grocery store, 1",
            "Call doctor, Book appointment, 1",
            "Exercise, Go for a run, 1",
            "Read, Finish the book, 1",
            "Cook, Prepare dinner, 1"
    })
    void testAddMultipleTodos(String title, String description, int expectedCount) {
        Todo todo = todoService.addTodo(title, description);

        assertNotNull(todo);
        assertEquals(title, todo.getTitle());
        assertEquals(description, todo.getDescription());
        assertEquals(expectedCount, todoService.getTotalCount());

        // Clean up for next test iteration
        todoService.clearAllTodos();
    }

    @ParameterizedTest
    @DisplayName("Test todo operations using @CsvSource")
    @CsvSource({
            "1, Task 1, Description 1, true, false",
            "1, Task 2, Description 2, false, true",
            "1, Task 3, Description 3, true, true",
            "1, Task 4, Description 4, false, false"
    })
    void testTodoOperations(int expectedId, String title, String description,
                            boolean shouldComplete, boolean shouldDelete) {
        todoService.clearAllTodos();
        Todo todo = todoService.addTodo(title, description);

        assertEquals(expectedId, todo.getId());

        if (shouldComplete) {
            boolean completed = todoService.completeTodo(todo.getId());
            assertTrue(completed);
            assertTrue(todo.isCompleted());
            assertEquals(1, todoService.getCompletedCount());
        }

        if (shouldDelete) {
            boolean deleted = todoService.deleteTodo(todo.getId());
            assertTrue(deleted);
            assertEquals(0, todoService.getTotalCount());
        }
    }

    @ParameterizedTest
    @DisplayName("Test search functionality using @MethodSource")
    @MethodSource("provideSearchTestData")
    void testSearchTodos(String keyword, String todoTitlesStr, int expectedResults) {
        todoService.clearAllTodos();

        // Parse the comma-separated string into a list
        String[] todoTitles = todoTitlesStr.split(",");

        // Add test todos
        for (String title : todoTitles) {
            todoService.addTodo(title.trim(), "Description");
        }

        List<Todo> results = todoService.searchTodos(keyword);
        assertEquals(expectedResults, results.size());
    }

    static Stream<Arguments> provideSearchTestData() {
        return Stream.of(
                Arguments.of("buy", "Buy milk,Buy bread,Call mom", 2),
                Arguments.of("call", "Buy milk,Call mom,Call doctor", 2),
                Arguments.of("app", "Testing app,Read book,Walk outside", 1),
                Arguments.of("xyz", "Buy milk,Call mom,Exercise", 0),
                Arguments.of("", "Task 1,Task 2,Task 3", 3)
        );
    }

    @Test
    @DisplayName("Test Mockito with TodoService")
    void testMockitoIntegration() {
        // Setup mock behavior
        Todo mockTodo = new Todo(1, "Mocked Todo", "Mocked Description");
        when(mockTodoService.addTodo("Test", "Test Desc")).thenReturn(mockTodo);
        when(mockTodoService.getTotalCount()).thenReturn(1);
        when(mockTodoService.findTodoById(1)).thenReturn(Optional.of(mockTodo));

        // Test mock behavior
        Todo result = mockTodoService.addTodo("Test", "Test Desc");
        assertEquals("Mocked Todo", result.getTitle());
        assertEquals(1, mockTodoService.getTotalCount());

        Optional<Todo> found = mockTodoService.findTodoById(1);
        assertTrue(found.isPresent());
        assertEquals(mockTodo, found.get());

        // Verify interactions
        verify(mockTodoService).addTodo("Test", "Test Desc");
        verify(mockTodoService).getTotalCount();
        verify(mockTodoService).findTodoById(1);
    }

    @ParameterizedTest
    @DisplayName("Test mocked todo operations using @MethodSource")
    @MethodSource("provideMockTestData")
    void testMockedOperations(int id, String title, boolean shouldExist) {
        // Setup mock
        if (shouldExist) {
            Todo mockTodo = new Todo(id, title, "Description");
            when(mockTodoService.findTodoById(id)).thenReturn(Optional.of(mockTodo));
            when(mockTodoService.completeTodo(id)).thenReturn(true);
            when(mockTodoService.deleteTodo(id)).thenReturn(true);
        } else {
            when(mockTodoService.findTodoById(id)).thenReturn(Optional.empty());
            when(mockTodoService.completeTodo(id)).thenReturn(false);
            when(mockTodoService.deleteTodo(id)).thenReturn(false);
        }

        // Test operations
        Optional<Todo> found = mockTodoService.findTodoById(id);
        boolean completed = mockTodoService.completeTodo(id);
        boolean deleted = mockTodoService.deleteTodo(id);

        assertEquals(shouldExist, found.isPresent());
        assertEquals(shouldExist, completed);
        assertEquals(shouldExist, deleted);

        // Verify interactions
        verify(mockTodoService).findTodoById(id);
        verify(mockTodoService).completeTodo(id);
        verify(mockTodoService).deleteTodo(id);
    }

    static Stream<Arguments> provideMockTestData() {
        return Stream.of(
                Arguments.of(1, "Existing Todo", true),
                Arguments.of(2, "Another Todo", true),
                Arguments.of(999, "Non-existent", false),
                Arguments.of(-1, "Invalid ID", false)
        );
    }

    @Test
    @DisplayName("Test todo statistics")
    void testTodoStatistics() {
        todoService.clearAllTodos();

        // Add some todos
        todoService.addTodo("Task 1", "Description 1");
        todoService.addTodo("Task 2", "Description 2");
        todoService.addTodo("Task 3", "Description 3");

        assertEquals(3, todoService.getTotalCount());
        assertEquals(3, todoService.getPendingCount());
        assertEquals(0, todoService.getCompletedCount());

        // Complete one todo
        todoService.completeTodo(1);

        assertEquals(3, todoService.getTotalCount());
        assertEquals(2, todoService.getPendingCount());
        assertEquals(1, todoService.getCompletedCount());
    }

    @ParameterizedTest
    @DisplayName("Test description validation with various lengths")
    @ValueSource(ints = {0, 50, 100, 250, 500})
    void testDescriptionLengthValidation(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("a");
        }
        String description = sb.toString();

        assertDoesNotThrow(() -> {
            todoService.addTodo("Test Title", description);
        });

        todoService.clearAllTodos();
    }

    @Test
    @DisplayName("Test invalid description length")
    void testInvalidDescriptionLength() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 501; i++) {
            sb.append("a");
        }
        String longDescription = sb.toString();

        assertThrows(IllegalArgumentException.class, () -> {
            todoService.addTodo("Test Title", longDescription);
        });
    }

    @Test
    @DisplayName("Test null title validation")
    void testNullTitleValidation() {
        assertThrows(IllegalArgumentException.class, () -> {
            todoService.addTodo(null, "Description");
        });
    }

    @Test
    @DisplayName("Test title too long validation")
    void testTitleTooLongValidation() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 101; i++) {
            sb.append("a");
        }
        String longTitle = sb.toString();

        assertThrows(IllegalArgumentException.class, () -> {
            todoService.addTodo(longTitle, "Description");
        });
    }

    @Test
    @DisplayName("Test finding todo by ID")
    void testFindTodoById() {
        todoService.clearAllTodos();
        Todo todo = todoService.addTodo("Test Todo", "Test Description");

        Optional<Todo> found = todoService.findTodoById(todo.getId());
        assertTrue(found.isPresent());
        assertEquals(todo.getId(), found.get().getId());

        Optional<Todo> notFound = todoService.findTodoById(999);
        assertFalse(notFound.isPresent());
    }

    @Test
    @DisplayName("Test get completed and pending todos")
    void testGetCompletedAndPendingTodos() {
        todoService.clearAllTodos();

        Todo todo1 = todoService.addTodo("Todo 1", "Description 1");
        Todo todo2 = todoService.addTodo("Todo 2", "Description 2");
        Todo todo3 = todoService.addTodo("Todo 3", "Description 3");

        // Complete one todo
        todoService.completeTodo(todo2.getId());

        List<Todo> completed = todoService.getCompletedTodos();
        List<Todo> pending = todoService.getPendingTodos();

        assertEquals(1, completed.size());
        assertEquals(2, pending.size());
        assertTrue(completed.get(0).isCompleted());
        assertFalse(pending.get(0).isCompleted());
        assertFalse(pending.get(1).isCompleted());
    }
}
    // Unit tests - Leader implementation
    @Test
    public void testAddTodo() {
        TodoService service = new TodoService();
        service.addTodo("Test Todo", "Test Description");
        assertEquals(1, service.getTotalTodos());
    }
    
    @Test
    public void testValidateTodo() {
        TodoService service = new TodoService();
        assertTrue(service.validateTodo("Valid Title"));
        assertFalse(service.validateTodo(""));
        assertFalse(service.validateTodo(null));
    }

