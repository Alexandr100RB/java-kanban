package model;

import static org.junit.jupiter.api.Assertions.*;

import controllers.InMemoryTaskManager;
import org.junit.jupiter.api.Test;

import java.util.List;

class TaskTest {
    @Test
    void addNewTask() {
        Task task = new Task("Test task", "Test description", "NEW");
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        final int taskId = taskManager.addNewTask(task);
        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "task not found");
        assertEquals(task, savedTask, "tasks not equals.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "tasks does not return");
        assertEquals(1, tasks.size(), "wrong tasks number");
        assertEquals(task, tasks.get(0), "tasks not equals");
    }

}