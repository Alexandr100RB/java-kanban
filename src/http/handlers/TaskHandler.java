package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestedMethod = exchange.getRequestMethod();
        String requestedPath = exchange.getRequestURI().getPath();
        String[] splitPath = requestedPath.split("/");
        if (requestedMethod.equals("GET") && splitPath.length == 2 && splitPath[1].equals("tasks")) {
            getTasksHandle(exchange);
        } else if (requestedMethod.equals("GET") && splitPath.length == 3 && splitPath[1].equals("tasks")) {
            getTaskByIdHandle(exchange, splitPath[2]);
        } else if (requestedMethod.equals("POST") && splitPath.length == 2 && splitPath[1].equals("tasks")) {
            postTaskHandle(exchange);
        } else if (requestedMethod.equals("DELETE") && splitPath.length == 3 && splitPath[1].equals("tasks")) {
            deleteTaskByIdHandle(exchange, splitPath[2]);
        } else {
            sendNotAllowed(exchange, "Такого метода не существует");
        }
    }

    private void getTasksHandle(HttpExchange exchange) throws IOException {
        try {
            List<Task> tasks = taskManager.getAllTasks();
            String text = gson.toJson(tasks);
            sendText(exchange, text);
        } catch (Exception exception) {
            sendNotFound(exchange, "Не удалось получить список задач");
        }
    }

    private void getTaskByIdHandle(HttpExchange exchange, String stringId) throws IOException {
        try {
            int id = Integer.parseInt(stringId);
            Task task = taskManager.getTaskById(id);
            sendText(exchange, gson.toJson(task));
        } catch (Exception exception) {
            sendNotFound(exchange, "Задача " + stringId + " не найдена");
        }
    }

    private void postTaskHandle(HttpExchange exchange) throws IOException {
        try {
            InputStream bodyInputStream = exchange.getRequestBody();
            String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(body, Task.class);
            if (task == null) {
                sendNotFound(exchange, "Не удалось получить задачу");
                return;
            }
            if (taskManager.isTasksCrossed(task)) {
                sendHasInteractions(exchange, "Задача пересекается с существующей");
                return;
            }
            int id = task.getId();
            if (id == 0) {
                taskManager.addNewTask(task);
                sendSuccess(exchange);
            } else {
                if (taskManager.getTaskById(id) == null) {
                    sendNotFound(exchange, "Не найдена задача " + id);
                } else {
                    taskManager.modifyTask(id, task);
                    sendSuccess(exchange);
                }
            }
        } catch (Exception exception) {
            sendBadRequest(exchange, "Не удалось добавить задачу");
        }
    }

    private void deleteTaskByIdHandle(HttpExchange exchange, String stringId) throws IOException {
        try {
            int id = Integer.parseInt(stringId);
            taskManager.deleteTaskById(id);
            sendSuccess(exchange);
        } catch (Exception exception) {
            sendNotFound(exchange, "Задача " + stringId + " не найдена");
        }
    }
}
