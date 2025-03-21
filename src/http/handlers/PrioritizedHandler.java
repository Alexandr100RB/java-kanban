package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import model.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestedMethod = exchange.getRequestMethod();
        String requestedPath = exchange.getRequestURI().getPath();
        String[] splitPath = requestedPath.split("/");
        if (requestedMethod.equals("GET") && splitPath.length == 2 && splitPath[1].equals("prioritized")) {
            try {
                List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
                String text = gson.toJson(prioritizedTasks);
                sendText(exchange, text);
            } catch (Exception exception) {
                sendNotFound(exchange, "При выполнении запроса возникла ошибка");
            }
        } else {
            sendNotAllowed(exchange, "Такого метода не существует");
        }
    }
}