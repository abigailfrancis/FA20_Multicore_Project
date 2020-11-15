package Homework3.q3a;

import java.util.concurrent.atomic.AtomicReference;

public class LockFree implements myDB {
    
    public LockFree() {
  
    }

    public boolean add(Integer value) {
    	return false;  
    }

    public boolean rm(Integer value) {
		return false;
    }
    
    protected class Node {
        public Integer value;
        public AtomicReference<Node> next;
        public AtomicReference<Node> prev;
        public AtomicReference<Node> up;
        public AtomicReference<Node> down;
        
        public Node(Integer x) {
            value = x;
            next = new AtomicReference<Node>();
            prev = new AtomicReference<Node>();
            up = new AtomicReference<Node>();
            down = new AtomicReference<Node>();
        }
    }
}
