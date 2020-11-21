package skiplist_proj;

import java.util.concurrent.atomic.AtomicReference;

public class Node
{
    private Integer item;
    private Integer topLevel;
    public AtomicReference<Node>[] next;

    /**
     * Initializes a new LockBasedNode
     * @param value The value to associate with the LockBasedNode
     * @param height The height of the Skiplist, where the node will be included
     */
    public Node(Integer value, int height)
    {
        item = value;
        next = new AtomicReference[height];
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
}