package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import model.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestedMethod = exchange.getRequestMethod();
        String requestedPath = exchange.getRequestURI().getPath();
        String[] splitPath = requestedPath.split("/");
        if (requestedMethod.equals("GET") && splitPath.length == 2 && splitPath[1].equals("history")) {
            try {
                List<Task> history = taskManager.getHistory();
                String text = gson.toJson(history);
                sendText(exchange, text);
            } catch (Exception exception) {
                sendNotFound(exchange, "При выполнении запроса возникла ошибка");
            }
        } else {
            sendNotAllowed(exchange, "Такого метода не существует");
        }
    }
}
