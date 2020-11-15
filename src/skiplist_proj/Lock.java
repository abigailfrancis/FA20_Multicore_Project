package skiplist_proj;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class Lock implements myDB {
    
    public Lock() {
  
    }

    public boolean add(Integer value) {
    	return false;  
    }

    public boolean rm(Integer value) {
		return false;
    }
    public skiplist_proj.LockFree.Node find(Integer value) {
		// TODO Auto-generated method stub
		return null;
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
