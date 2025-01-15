package controllers;

import static org.junit.jupiter.api.Assertions.*;

import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager;

    @BeforeEach
    public void initHistoryManager() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void testHistoricVersions() {
        Task task = new Task("Test task 1", "Test descriptions", "NEW");
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "historic task should be added");
        task.setStatus("IN_PROGRESS");
        historyManager.add(task);
        assertEquals(2, historyManager.getHistory().size(), "historic task should be added");
    }

    @Test
    public void testHistoricVersionsByPointer() {

    }
}