package model;

import static org.junit.jupiter.api.Assertions.*;

import controllers.InMemoryTaskManager;
import org.junit.jupiter.api.Test;

import java.util.List;

class EpicTest {
    @Test
    void addNewEpic() {
        Epic epic = new Epic("Test epic", "Test description");
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        final int epicId = taskManager.addNewEpic(epic);
        final Task savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "epic not found");
        assertEquals(epic, savedEpic, "epics not equals.");

        final List<Task> epics = taskManager.getAllEpics();

        assertNotNull(epics, "epics does not return");
        assertEquals(1, epics.size(), "wrong epics number");
        assertEquals(epic, epics.get(0), "epics not equals");
    }
}