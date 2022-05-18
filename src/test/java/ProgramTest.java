import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProgramTest {
    public static Todo todo = new Todo();
    List<Todo> todoList = new ArrayList<>();


    @AfterEach
    void tearDown() {
        todoList.clear();
    }

    @BeforeEach
    void setUp() {
        todo.setId(2);
        todo.setTitle("Write new feature");
        todo.setCompleted(false);
        todoList.add(todo);
    }

    @Test
    void shouldFindTodoById() {
        assertEquals(todo, Program.findTodoById(2, todoList));
    }

    @Test
    void shouldMarkAsCompleted() {
        boolean before = todoList.get(0).isCompleted();

        List<Todo> updatedList = Program.markAsCompleted(2, todoList);
        boolean after = updatedList.get(0).isCompleted();

        assertFalse(before);
        assertTrue(after);
        assertNotEquals(before, after);
    }
}