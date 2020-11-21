package skiplist_proj;

import java.util.concurrent.atomic.AtomicReference;

public class LockBasedSkiplist implements Skiplist
{
    private AtomicReference<Node> head = new AtomicReference<>();

    // Todo: Make this height dynamic
    private static Integer SKIPLIST_HEIGHT = 3;

    public LockBasedSkiplist(Node head)
    {
        this.head.set(head);
    }

    @Override
    public boolean add(Integer value) {
        // Todo: Implement method
        return false;
    }

    @Override
    public boolean rm(Integer value) {
        // Todo: Implement method
        return false;
    }

    /**
     * @param value the value to search for in the Skiplist
     * @param preds the list of predecessors for the node we are searching for
     * @param succs the list of successors for the node we are searching for
     * @return The level where the Integer was found, or -1 if it is not present in the Skiplist
     */
    @Override
    public Integer find(Integer value, AtomicReference<Node>[] preds, AtomicReference<Node>[] succs)
    {
        // To begin, set the predecessor node to the HEAD of the Skiplist
        AtomicReference<Node> predecessor = head;

        // If the value is not found, we will return -1
        int lFound = -1;

        // Start the search at the highest level of the Skiplist
        for (int level = SKIPLIST_HEIGHT; level >= 0; level--)
        {
            // Set curr to the predecessor node's successor
            AtomicReference<Node> curr = predecessor.get().next[level];

            // If curr is still less than the value we are trying to find
            while (value > curr.get().getItem())
            {
                // Advance the predecessor
                predecessor = curr;

                // Set curr to the predecessor node's successor
                curr = predecessor.get().next[level];
            }

            // If the key is found, update the return variable
            if (lFound == -1 && value == curr.get().getItem())
            {
                lFound = level;
            }

            // Set the new predecessor and successor
            preds[level] = predecessor;
            succs[level] = curr;
        }

        return lFound;
	}
}