package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.HttpTaskServer;
import controllers.TaskManager;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestedMethod = exchange.getRequestMethod();
        String requestedPath = exchange.getRequestURI().getPath();
        String[] splitPath = requestedPath.split("/");
        if (requestedMethod.equals("GET") && splitPath.length == 2 && splitPath[1].equals("tasks")) {
            getTasksHandle(exchange, gson);
        } else if (requestedMethod.equals("GET") && splitPath.length == 3 && splitPath[1].equals("tasks")) {
            getTaskByIdHandle(exchange, gson, splitPath[2]);
        } else if (requestedMethod.equals("POST") && splitPath.length == 2 && splitPath[1].equals("tasks")) {
            postTaskHandle(exchange, gson);
        } else if (requestedMethod.equals("DELETE") && splitPath.length == 3 && splitPath[1].equals("tasks")) {
            deleteTaskByIdHandle(exchange, splitPath[2]);
        } else {
            sendNotFound(exchange, "Такого метода не существует");
        }
    }

    private void getTasksHandle(HttpExchange exchange, Gson gson) throws IOException {
        try {
            List<Task> tasks = taskManager.getAllTasks();
            String text = gson.toJson(tasks);
            sendText(exchange, text);
        } catch (Exception exception) {
            sendNotFound(exchange, "Не удалось получить список задач");
        }
    }

    private void getTaskByIdHandle(HttpExchange exchange, Gson gson, String stringId) throws IOException {
        try {
            int id = Integer.parseInt(stringId);
            Task task = taskManager.getTaskById(id);
            sendText(exchange, gson.toJson(task));
        } catch (Exception exception) {
            sendNotFound(exchange, "Задача " + stringId + " не найдена");
        }
    }

    private void postTaskHandle(HttpExchange exchange, Gson gson) throws IOException {
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
            try {
                int id = task.getId();
                if (taskManager.getTaskById(id) == null) {
                    sendNotFound(exchange, "Не найдена задача " + id);
                } else {
                    taskManager.modifyTask(id, task);
                    sendSuccess(exchange);
                }
            } catch (Exception exception) {
                taskManager.addNewTask(task);
                sendSuccess(exchange);
            }
        } catch (Exception exception) {
            sendNotFound(exchange, "Не удалось добавить задачу");
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
