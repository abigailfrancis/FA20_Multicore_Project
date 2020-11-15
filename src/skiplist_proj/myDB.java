// You do NOT need to modify this file
// The interface of the MyQueue
// You should implement LockQueue and LockFreeQueue by extending this class
package skiplist_proj;


import skiplist_proj.LockFree.Node;

public interface myDB {
    // return true if successfully enqueue a new value
    public boolean add(Integer value);
    public boolean rm(Integer value);
    public Node find(Integer value);
   
}
