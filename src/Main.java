import controllers.Managers;
import controllers.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        System.out.println("Программа запущена!");

        File file = new File("task.csv");

        TaskManager manager = Managers.getDefault();
        Task task1 = new Task("Task #1", "Task #1 description", "NEW",
                LocalDateTime.of(2025, 2, 12, 13, 0), Duration.ofHours(2));
        manager.addNewTask(task1);
        Task task2 = new Task("Task #2", "Task #2 description", "NEW",
                LocalDateTime.of(2025, 2, 10, 9, 20), Duration.ofHours(3));
        manager.addNewTask(task2);
        Epic epic1 = new Epic("Epic #1", "Epic #1 description");
        manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #1", "Subtask #1 description", "NEW", 3,
                LocalDateTime.of(2025, 3, 1, 14, 30), Duration.ofHours(10));
        manager.addNewSubtask(subtask1);
        Epic epic2 = new Epic("Epic #2", "Epic #2 description");
        manager.addNewEpic(epic2);
        Subtask subtask2 = new Subtask("Subtask #2", "Subtask #2 description", "NEW", 5,
                LocalDateTime.of(2025, 2, 22, 12, 0), Duration.ofHours(5));
        manager.addNewSubtask(subtask2);
        Subtask subtask3 = new Subtask("Subtask #3", "Subtask #3 description", "NEW", 5,
                LocalDateTime.of(2025, 2, 26, 12, 45), Duration.ofHours(6));
        manager.addNewSubtask(subtask3);
        printAllTasks(manager);
        printAllEpics(manager);
        printAllSubtasks(manager);
        System.out.println("---");
        manager.modifyTask(1, new Task("Task #1", "Task #1 new description", "IN_PROGRESS"));
        manager.modifyTask(2, new Task("Task #2", "Task #2 new description", "DONE"));
        manager.modifySubtask(4, new Subtask("Subtask #1", "Subtask #1 new description",
                subtask1.getId(), "DONE", subtask1.getParentId(),
                LocalDateTime.of(2025, 3, 1, 14, 30), Duration.ofHours(10)));
        manager.modifySubtask(6, new Subtask("Subtask #2", "Subtask #2 new description",
                subtask2.getId(), "IN_PROGRESS", subtask2.getParentId(),
                LocalDateTime.of(2025, 2, 22, 12, 0), Duration.ofHours(5)));
        manager.modifySubtask(7, new Subtask("Subtask #3", "Subtask #3 new description",
                subtask3.getId(), "DONE", subtask3.getParentId(),
                LocalDateTime.of(2025, 2, 26, 12, 45), Duration.ofHours(6)));
        printAllTasks(manager);
        printAllEpics(manager);
        printAllSubtasks(manager);
        System.out.println("---");
        manager.deleteTaskById(2);
        manager.deleteEpicById(3);
        printAllTasks(manager);
        printAllEpics(manager);
        printAllSubtasks(manager);
        System.out.println("---");
        manager.getTaskById(1);
        manager.getEpicById(5);
        manager.getSubtaskById(6);
        manager.getTaskById(1);
        manager.getEpicById(5);
        manager.getSubtaskById(7);
        manager.getSubtaskById(6);
        for (Task task: manager.getHistory()) {
            System.out.println(task.getId());
        }
        System.out.println("---");
        manager.deleteSubtaskById(6);
        manager.deleteTaskById(1);
        for (Task task: manager.getHistory()) {
            System.out.println(task.getId());
        }
        System.out.println("---");

    }

    public static void printAllTasks(TaskManager tasks) {
        for (Task task: tasks.getAllTasks()) {
            System.out.println(task);
        }
    }

    public static void printAllEpics(TaskManager epics) {
        for (Task epic: epics.getAllEpics()) {
            System.out.println(epic);
        }
    }

    public static void printAllSubtasks(TaskManager subtasks) {
        for (Task subtask: subtasks.getAllSubtasks()) {
            System.out.println(subtask);
        }
    }
}
