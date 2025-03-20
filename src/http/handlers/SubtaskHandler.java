package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import http.HttpTaskServer;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestedMethod = exchange.getRequestMethod();
        String requestedPath = exchange.getRequestURI().getPath();
        String[] splitPath = requestedPath.split("/");
        if (requestedMethod.equals("GET") && splitPath.length == 2 && splitPath[1].equals("subtasks")) {
            getSubtasksHandle(exchange, gson);
        } else if (requestedMethod.equals("GET") && splitPath.length == 3 && splitPath[1].equals("subtasks")) {
            getSubtaskByIdHandle(exchange, gson, splitPath[2]);
        } else if (requestedMethod.equals("POST") && splitPath.length == 2 && splitPath[1].equals("subtasks")) {
            postSubtaskHandle(exchange, gson);
        } else if (requestedMethod.equals("DELETE") && splitPath.length == 3 && splitPath[1].equals("subtasks")) {
            deleteSubtaskByIdHandle(exchange, splitPath[2]);
        } else {
            sendNotFound(exchange, "Такого метода не существует");
        }
    }

    private void getSubtasksHandle(HttpExchange exchange, Gson gson) throws IOException {
        try {
            List<Subtask> tasks = taskManager.getAllSubtasks();
            String text = gson.toJson(tasks);
            sendText(exchange, text);
        } catch (Exception exception) {
            sendNotFound(exchange, "Не удалось получить список подзадач");
        }
    }

    private void getSubtaskByIdHandle(HttpExchange exchange, Gson gson, String stringId) throws IOException {
        try {
            int id = Integer.parseInt(stringId);
            Task task = taskManager.getSubtaskById(id);
            sendText(exchange, gson.toJson(task));
        } catch (Exception exception) {
            sendNotFound(exchange, "Подзадача " + stringId + " не найдена");
        }
    }

    private void postSubtaskHandle(HttpExchange exchange, Gson gson) throws IOException {
        try {
            InputStream bodyInputStream = exchange.getRequestBody();
            String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
            Subtask subtask = gson.fromJson(body, Subtask.class);
            if (subtask == null) {
                sendNotFound(exchange, "Не удалось получить подзадачу");
                return;
            }
            try {
                int id = subtask.getId();
                if (taskManager.getSubtaskById(id) == null) {
                    sendNotFound(exchange, "Не найдена подзадача " + id);
                } else {
                    taskManager.modifySubtask(id, subtask);
                    sendSuccess(exchange);
                }
            } catch (Exception exception) {
                taskManager.addNewSubtask(subtask);
                sendSuccess(exchange);
            }
        } catch (Exception exception) {
            sendNotFound(exchange, "Не удалось добавить подзадачу");
        }
    }

    private void deleteSubtaskByIdHandle(HttpExchange exchange, String stringId) throws IOException {
        try {
            int id = Integer.parseInt(stringId);
            taskManager.deleteSubtaskById(id);
            sendSuccess(exchange);
        } catch (Exception exception) {
            sendNotFound(exchange, "Подзадача " + stringId + " не найдена");
        }
    }
}
