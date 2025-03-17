package controllers;

import static org.junit.jupiter.api.Assertions.*;

import exceptions.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;

public class TaskManagerTest {
    TaskManager manager;
    HistoryManager historyManager;
    @BeforeEach
    public void initManager() {
        manager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void testEpicStatusCheck() {
        Epic epic = new Epic("Test epic 1", "Test description");
        manager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("Test subtask 1", "Test description", "NEW", 1,
                LocalDateTime.now(), Duration.ofHours(2));
        manager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test subtask 2", "Test description", "NEW", 1,
                LocalDateTime.now(), Duration.ofHours(2));
        manager.addNewSubtask(subtask2);
        assertEquals("NEW", manager.getEpicById(1).getStatus(), "task should be NEW");
        subtask1.setStatus("DONE");
        subtask2.setStatus("DONE");
        manager.modifySubtask(2, subtask1);
        manager.modifySubtask(3, subtask2);
        assertEquals("DONE", manager.getEpicById(1).getStatus(), "task should be DONE");
        subtask1.setStatus("NEW");
        manager.modifySubtask(2, subtask1);
        assertEquals("IN_PROGRESS", manager.getEpicById(1).getStatus(), "task should be IN_PROGRESS");
        subtask1.setStatus("IN_PROGRESS");
        subtask2.setStatus("IN_PROGRESS");
        manager.modifySubtask(2, subtask1);
        manager.modifySubtask(3, subtask2);
        assertEquals("IN_PROGRESS", manager.getEpicById(1).getStatus(), "task should be IN_PROGRESS");
    }

    @Test
    public void testIsTasksCrossed() {
        Task task1 = new Task("Test task 1", "Test description", "NEW",
                LocalDateTime.of(2025, 3, 10, 12, 0), Duration.ofDays(10));
        manager.addNewTask(task1);
        Task task2 = new Task("Test task 2", "Test description", "NEW",
                LocalDateTime.of(2025, 3, 5, 12, 0), Duration.ofDays(10));
        assertTrue(manager.isTasksCrossed(task2), "tasks crossed, answer should be true");
        Task task3 = new Task("Test task 3", "Test description", "NEW",
                LocalDateTime.of(2025, 3, 15, 12, 0), Duration.ofDays(10));
        assertTrue(manager.isTasksCrossed(task3), "tasks crossed, answer should be true");
    }

    @Test
    public void testHistoryManager() {
        assertEquals(0, historyManager.getHistory().size(), "history size should be 0");
        Task task1 = new Task("Test task 1", "Test description", "NEW",
                LocalDateTime.of(2025, 3, 10, 12, 0), Duration.ofDays(10));
        historyManager.add(task1);
        assertEquals(1, historyManager.getHistory().size(), "history size should be 1");
        historyManager.add(task1);
        assertEquals(1, historyManager.getHistory().size(), "history size should be 1");
    }
}
