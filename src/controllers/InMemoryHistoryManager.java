package controllers;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager{
    private static class Node {
        Task task;
        Node prev;
        Node next;

        private Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "task=" + task +
                    ", prev=" + prev +
                    ", next=" + next +
                    '}';
        }
    }

    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    private void linkLast(Task task) {
        final Node node = new Node(task, last, null);
        if (nodeMap.isEmpty()) {
            first = node;
            last = node;
        }
        remove(task.getId());
        removeNode(node);
        nodeMap.put(task.getId(), node);
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        final Node node = nodeMap.remove(id);
        if (node != null) {
            if (node.prev != null) {
                node.prev.next = node.next;
                if (node.next == null) {
                    last = node.prev;
                } else {
                    node.prev.next = node.prev;
                }
            } else {
                first = node.next;
                if (first == null) {
                    last = null;
                } else {
                    first.prev = null;
                }
            }
        }
    }

    private void removeNode(Node node) {
        nodeMap.remove(node.task.getId());
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> history =  new ArrayList<>();
        for(Map.Entry<Integer, Node> entry : nodeMap.entrySet()) {
            history.add(entry.getValue().task);
        }
        return history;
    }
}
