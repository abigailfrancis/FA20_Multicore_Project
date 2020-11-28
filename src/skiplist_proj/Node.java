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
    private Lock lock = new ReentrantLock();
    private volatile boolean marked = false;
    private volatile boolean fullyLinked = false;

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
            next[i] = new AtomicReference<>();
        }

        // Levels are zero-indexed. Subtract one from height.
        topLevel = height-1;
    }

    /*** Public Accessor Methods ***/

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
        return this.item;
    }

    /**
     * @return True, if the node is marked
     */
    public boolean isMarked()
    {
        return this.marked;
    }

    /**
     * @param value The value to set 'marked' to
     */
    public void setMarked(boolean value)
    {
        this.marked = value;
    }

    /**
     * @return True, if the node is fully linked
     */
    public boolean isFullyLinked()
    {
        return this.fullyLinked;
    }

    /**
     * @param value The value to set 'fullyLinked' to
     */
    public void setFullyLinked(boolean value)
    {
        this.fullyLinked = value;
    }


    /*** Public methods ***/

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

    /**
     * @return The node's value, as a string
     */
    @Override
    public String toString()
    {
        if (this.item == Integer.MAX_VALUE)
        {
            return "TAIL";
        }

        if (this.item == Integer.MIN_VALUE)
        {
            return "HEAD";
        }

        return this.item.toString();
    }
}