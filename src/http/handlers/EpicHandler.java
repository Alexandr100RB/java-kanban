package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import model.Epic;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestedMethod = exchange.getRequestMethod();
        String requestedPath = exchange.getRequestURI().getPath();
        String[] splitPath = requestedPath.split("/");
        if (requestedMethod.equals("GET") && splitPath.length == 2 && splitPath[1].equals("epics")) {
            getEpicsHandle(exchange);
        } else if (requestedMethod.equals("GET") && splitPath.length == 3 && splitPath[1].equals("epics")) {
            getEpicByIdHandle(exchange, splitPath[2]);
        } else if (requestedMethod.equals("GET") && splitPath.length == 4 && splitPath[1].equals("epics")
                && splitPath[3].equals("subtasks")) {
            getSubtasksHandle(exchange, splitPath[3]);
        } else if (requestedMethod.equals("POST") && splitPath.length == 2 && splitPath[1].equals("epics")) {
            postEpicHandle(exchange);
        } else if (requestedMethod.equals("DELETE") && splitPath.length == 3 && splitPath[1].equals("epics")) {
            deleteEpicByIdHandle(exchange, splitPath[2]);
        } else {
            sendNotAllowed(exchange, "Такого метода не существует");
        }
    }

    private void getEpicsHandle(HttpExchange exchange) throws IOException {
        try {
            List<Task> tasks = taskManager.getAllEpics();
            String text = gson.toJson(tasks);
            sendText(exchange, text);
        } catch (Exception exception) {
            sendNotFound(exchange, "Не удалось получить список эпиков");
        }
    }

    private void getEpicByIdHandle(HttpExchange exchange, String stringId) throws IOException {
        try {
            int id = Integer.parseInt(stringId);
            Task task = taskManager.getEpicById(id);
            sendText(exchange, gson.toJson(task));
        } catch (Exception exception) {
            sendNotFound(exchange, "Эпик " + stringId + " не найдена");
        }
    }

    private void postEpicHandle(HttpExchange exchange) throws IOException {
        try {
            InputStream bodyInputStream = exchange.getRequestBody();
            String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(body, Epic.class);
            if (epic == null) {
                sendNotFound(exchange, "Не удалось получить эпик");
                return;
            }
            int id = epic.getId();
            if (epic.getId() == 0) {
                taskManager.addNewEpic(epic);
                sendSuccess(exchange);
            } else {
                if (taskManager.getEpicById(id) == null) {
                    sendNotFound(exchange, "Не найден эпик " + id);
                }
            }
        } catch (Exception exception) {
            sendBadRequest(exchange, "Не удалось добавить эпик");
        }
    }

    private void deleteEpicByIdHandle(HttpExchange exchange, String stringId) throws IOException {
        try {
            int id = Integer.parseInt(stringId);
            taskManager.deleteEpicById(id);
            sendSuccess(exchange);
        } catch (Exception exception) {
            sendNotFound(exchange, "Задача " + stringId + " не найдена");
        }
    }

    private void getSubtasksHandle(HttpExchange exchange, String stringId) throws IOException {
        try {
            int id = Integer.parseInt(stringId);
            List<Integer> subtasksIds = ((Epic) taskManager.getEpicById(id)).getSubtasksIds();
            List<Task> subtasks = null;
            for (Integer subtaskId: subtasksIds) {
                subtasks.add(taskManager.getSubtaskById(subtaskId));
            }
            String text = gson.toJson(subtasks);
            sendText(exchange, text);
        } catch (Exception exception) {
            sendNotFound(exchange, "Эпик " + stringId + " не найден");
        }
    }
}