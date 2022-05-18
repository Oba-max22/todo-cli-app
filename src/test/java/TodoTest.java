
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {
    Todo todo = new Todo();

    @Test
    void createTodo() {
        todo.setId(1L);
        todo.setTitle("Working on Todo Application!");
        todo.setCompleted(true);

        assertEquals(1L, todo.getId());
        assertEquals("Working on Todo Application!", todo.getTitle());
        assertTrue(todo.isCompleted());
    }
}