package skiplist_proj;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class LockBasedSkiplist implements Skiplist
{
    private AtomicReference<Node> head = new AtomicReference<>();

    public LockBasedSkiplist(Node head)
    {
        this.head.set(head);
    }

    /**
     * @param value the value to be added to the Skiplist
     * @return True, if the integer was added to the Skiplist successfully
     */
    @Override
    public boolean add(Integer value)
    {
        int topLevel = MAX_HEIGHT;

        List<AtomicReference<Node>> preds = new ArrayList<>();
        List<AtomicReference<Node>> succs = new ArrayList<>();

        for (int i = 0; i < MAX_HEIGHT; i++)
        {
            // Initialize empty values for preds and succs
            preds.add(i, null);
            succs.add(i, null);
        }

        while (true) {
            Integer lFound = find(value, preds, succs);
            if (lFound != -1) {
                AtomicReference<Node> nodeFound = succs.get(lFound);
                if (!nodeFound.get().marked) {
                    while (!nodeFound.get().fullyLinked) {
                        // no-op
                    }

                    return false;
                }

                continue;
            }

            int highestLocked = -1;
            try {
                AtomicReference<Node> pred;
                AtomicReference<Node> succ;

                boolean valid = true;

                for (int level = 0; valid && (level < topLevel); level++)
                {
                    pred = preds.get(level);
                    succ = succs.get(level);
                    pred.get().lock();
                    highestLocked = level;
                    valid = !pred.get().marked && !succ.get().marked && pred.get().next[level] == succ;
                }

                if (!valid) continue;

                Node node = new Node(value, topLevel);

                AtomicReference<Node> newNode = new AtomicReference<>(node);

                for (int level = 0; level < topLevel; level++)
                {
                    // Null exception here
                    newNode.get().next[level].set(succs.get(level).get());
                }

                for (int level = 0; level < topLevel; level++)
                {
                    preds.get(level).get().next[level].set(newNode.get());
                }

                newNode.get().fullyLinked = true;
                return true;
            }
            finally
            {
                for (int level = 0; level < highestLocked; level++)
                {
                    preds.get(level).get().unlock();
                }
            }
        }
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
    public Integer find(Integer value, List<AtomicReference<Node>> preds, List<AtomicReference<Node>> succs)
    {
        // To begin, set the predecessor node to the HEAD of the Skiplist
        AtomicReference<Node> predecessor = head;

        // If the value is not found, we will return -1
        int lFound = -1;

        // Start the search at the highest level of the Skiplist
        for (int level = MAX_HEIGHT -1; level >= 0; level--)
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
            preds.set(level, predecessor);
            succs.set(level, curr);
        }

        return lFound;
	}
}