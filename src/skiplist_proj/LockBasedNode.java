package skiplist_proj;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockBasedNode extends Node
{
    private final Lock lock = new ReentrantLock();
    volatile boolean marked = false;
    volatile boolean fullyLinked = false;

    /**
     * Initializes a new LockBasedNode
     *
     * @param value  The value to associate with the LockBasedNode
     * @param height The height of the Skiplist, where the node will be included
     */
    public LockBasedNode(Integer value, int height) {
        super(value, height);
    }

    /**
     * Locks the node
     */
    public void lock()
    {
        this.lock.lock();
    }

    /**
     * Unlocks the node
     */
    public void unlock()
    {
        this.lock.unlock();
    }
}
