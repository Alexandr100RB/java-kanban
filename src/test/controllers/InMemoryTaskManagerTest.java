package controllers;

import static org.junit.jupiter.api.Assertions.*;

import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest {
    TaskManager manager;
    @BeforeEach
    public void initManager() {
        manager = Managers.getDefault();
    }

    @Test
    public void testAddTask() {
        Task task = new Task("Test task 1", "Test descriptions", "NEW");
        manager.addNewTask(task);
        assertEquals(1, manager.getAllTasks().size(), "task should be added");
        Task addedTask = manager.getAllTasks().get(0);
        assertEquals(task, addedTask, "added task id should be set");
        Task taskById = manager.getTaskById(task.getId());
        assertEquals(task, taskById, "added task id should be found");
    }
}