package skiplist_proj;

import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node
{
    // Used for both implementations
    private Integer item;
    private Integer topLevel;
    public long key;
    public AtomicMarkableReference<Node>[] next;
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
        key = (long)value.hashCode(); 
        next = new AtomicMarkableReference[height + 1];
        for (int i = 0; i < next.length; i++)
        {
            next[i] = new AtomicMarkableReference<>(null, false);
        }

        // Levels are zero-indexed. Subtract one from height.
        topLevel = height;
    }

    /*** Public Accessor Methods ***/
    /**
     * @return The value of key
     */
    public long getKey()
    {
        return key;
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
        return this.item;
    }

    public boolean isMarked()
    {
        return this.marked;
    }

    public void setMarked(boolean value)
    {
        this.marked = value;
    }

    public boolean isFullyLinked()
    {
        return this.fullyLinked;
    }

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