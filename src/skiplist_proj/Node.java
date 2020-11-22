package skiplist_proj;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node
{
    // Used for both implementations
    private Integer item;
    private Integer topLevel;
    public AtomicReference<Node>[] next;

    // Used for Lock-Based implementation
    private final Lock lock = new ReentrantLock();
    public volatile boolean marked = false;
    public volatile boolean fullyLinked = false;

    /**
     * Initializes a new LockBasedNode
     * @param value The value to associate with the LockBasedNode
     * @param height The height, indicating the number of levels that the node will be added to
     */
    public Node(Integer value, int height)
    {
        item = value;

        next = new AtomicReference[height];
        for (int i = 0; i < height; i++)
        {
            next[i] = new AtomicReference<Node>();
        }

        topLevel = height;
    }

    /**
     * @return The value of topLevel
     */
    public Integer getTopLevel()
    {
        return topLevel;
    }

    /**
     * @return The value of the Item
     */
    public Integer getItem()
    {
        return item;
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