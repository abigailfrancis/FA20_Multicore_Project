package skiplist_proj;

import java.util.concurrent.atomic.AtomicReference;

public class Node {
    public Integer value;
    public AtomicReference<Node> next;
    public AtomicReference<Node> prev;
    public AtomicReference<Node> up;
    public AtomicReference<Node> down;

    public Node(Integer x) {
        value = x;
        next = new AtomicReference<>();
        prev = new AtomicReference<>();
        up = new AtomicReference<>();
        down = new AtomicReference<>();
    }
}