package skiplist_proj;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicMarkableReference;

import static skiplist_proj.Skiplist.MAX_HEIGHT;

public class LockBasedSkiplist implements Skiplist
{
    private Node head = null;
    private static Random rnd = new Random();

    public LockBasedSkiplist(Node head)
    {
        this.head = head;
    }

    /**
     * @param value the value to be added to the Skiplist
     * @return True, if the integer was added to the Skiplist successfully
     */
    public boolean add(Integer value)
    {
        // Chosoe a random level (0 <= x < MAX_HEIGHT) for the new node's top level
        int topLevel = chooseRandomTopLevel();

        // Initialize empty preds and succs lists
        List<Node> preds = new ArrayList<>();
        List<Node> succs = new ArrayList<>();

        for (int i = 0; i <= MAX_HEIGHT; i++)
        {
            preds.add(i, null);
            succs.add(i, null);
        }

        while (true)
        {
            // Determine if the value is already in the Skiplist
            Integer levelFound = find(value, preds, succs);

            // If the value is already in the Skiplist
            if (levelFound != -1)
            {
                // Get a reference to the successor node
            	Node nodeFound = succs.get(levelFound);

                // If the successor node is not marked
                if (!nodeFound.isMarked()) {

                    // Wait for it to be fully-linked
                    while (!nodeFound.isFullyLinked()) {
                        // no-op
                    }

                    return false;
                }

                continue;
            }

            int highestLocked = -1;

            try
            {
            	Node pred;
            	Node succ;

                boolean valid = true;

                // Starting at the bottom of the Skiplist
                // Lock all of the predecessor nodes
                for (int level = 0; valid && (level <= topLevel); level++)
                {
                    pred = preds.get(level);
                    succ = succs.get(level);
                    pred.lock();
                    highestLocked = level;
                    valid = !pred.isMarked() && !succ.isMarked() && pred.next[level].getReference() == succ;
                }

                if (!valid) continue;

                // Create the new Node
                Node newNode = new Node(value, topLevel);

                // Starting at the bottom of the Skiplist
                // Attach the new Node's successors
                for (int level = 0; level <= topLevel; level++)
                {
                    newNode.next[level].set(succs.get(level), false);
                }

                // Starting at the bottom of the Skiplist
                // Attach the new Node's predecessors
                for (int level = 0; level <= topLevel; level++)
                {
                    preds.get(level).next[level].set(newNode, false);
                }

                newNode.setFullyLinked(true);

                return true;
            }
            finally
            {
                // Unlock at all levels
                unlockAtAllLevels(preds, highestLocked);
            }
        }
    }

    /**
     * @param value the value to be removed from the Skiplist
     * @return True, if the Integer was removed successfully
     */
    public boolean rm(Integer value)
    {
    	Node victim = null;
        boolean isMarked = false;
        Integer topLevel = -1;

        // Initialize empty preds and succs lists
        List<Node> preds = new ArrayList<>();
        List<Node> succs = new ArrayList<>();

        for (int i = 0; i <= MAX_HEIGHT; i++)
        {
            preds.add(i, null);
            succs.add(i, null);
        }

        while (true)
        {
            // Determine if the value is already in the Skiplist
            Integer levelFound = find(value, preds, succs);

            // If the value is already in the Skiplist
            if (levelFound != -1)
            {
                // Get a reference to it
                victim = succs.get(levelFound);
            }

            // If the node is marked OR
            // If the node is present in the Skiplist, fully linked, not marked, and is on the right level
            boolean shouldEvaluateNode = isMarked ||
                    ((levelFound != -1)
                            &&( victim != null)
                            && (victim != null)
                            && (victim.isFullyLinked()
                            && victim.getTopLevel().equals(levelFound)
                            && !victim.isMarked()));
			if(shouldEvaluateNode)
            {
                if (!isMarked)
                {
                    // Determine the highest level of the Skiplist that this value
                    // is present on.
                    topLevel = victim.getTopLevel();

                    victim.lock();

                    if (victim.isMarked())
                    {
                        victim.unlock();
                        return false;
                    }

                    victim.setMarked(true);
                    isMarked = true;
                }

                Integer highestLocked = -1;

                try
                {
                	Node pred;

                    boolean valid = true;

                    // Starting at the bottom of the Skiplist
                    // Lock the predecessor nodes
                    for (int level = 0; valid && (level <= topLevel); level++)
                    {
                        pred = preds.get(level);
                        pred.lock();
                        highestLocked = level;
                        valid = !pred.isMarked() && pred.next[level].getReference() == victim;
                    }

                    if (!valid) continue;

                    // Starting at the top of the Skiplist
                    // Reassign the predecessor nodes
                    reassignPredecessorNodes(victim, topLevel, preds);

                    // The victim has been removed, unlock it
                    victim.unlock();

                    return true;
                }
                finally
                {
                    // Unlock at all levels
                    unlockAtAllLevels(preds, highestLocked);
                }
            }
            else {
                return false;
            }
        }
    }

    /**
     * @param value the value to search for in the Skiplist
     * @param preds the list of predecessors for the node we are searching for
     * @param succs the list of successors for the node we are searching for
     * @return The level where the Integer was found, or -1 if it is not present in the Skiplist
     */
    public Integer find(Integer value, List<Node> preds, List<Node> succs)
    {
        // To begin, set the predecessor node to the HEAD of the Skiplist
    	Node predecessor = head;

        // If the value is not found, we will return -1
        int lFound = -1;

        // Start the search at the highest level of the Skiplist
        for (int level = MAX_HEIGHT; level >= 0; level--)
        {
            // Set curr to the predecessor node's successor
        	Node curr = predecessor.next[level].getReference();

            // If curr is still less than the value we are trying to find
            while (value > curr.getItem())
            {
                // Advance the predecessor
                predecessor = curr;

                // Set curr to the predecessor node's successor
                curr = predecessor.next[level].getReference();
            }

            // If the key is found, update the return variable
            if (lFound == -1 && curr.getItem().equals(value))
            {
                lFound = level;
            }

            // Set the new predecessor and successor
            preds.set(level, predecessor);
            succs.set(level, curr);
        }

        return lFound;
	}

    public void display()
    {
        System.out.println("---------------------");
        for (int level = MAX_HEIGHT; level >= 0; level--)
        {
            System.out.print("Level " + level + ": ");

            Node node = this.head;

            while (node != null)
            {
                System.out.print(" -> " + node);
                node = node.next[level].getReference();
            }

            System.out.println();
        }
        System.out.println("---------------------");
    }

    /**
     * @return The highest level (0 <= x < MAX_HEIGHT) of the Skiplist that the new node will be added to
     */
    private int chooseRandomTopLevel() {
        return rnd.nextInt(MAX_HEIGHT);
    }

    /**
     * Unlock the nodes in the collection between level 0 & highestLocked
     * @param nodeCollection The collection of nodes to be unlocked
     * @param highestLocked The highest level where the node is present
     */
    private void unlockAtAllLevels(List<Node> nodeCollection, int highestLocked) {
        for (int level = 0; level <= highestLocked; level++)
        {
            nodeCollection.get(level).unlock();
        }
    }

    /**
     * Reassign the predecessor nodes, to fully remove the victim
     * @param victim The node we are trying to remove
     * @param topLevel The highest level of the Skiplist that we need to consider
     * @param preds The predecessor nodes
     */
    private void reassignPredecessorNodes(Node victim, Integer topLevel, List<Node> preds) {
        for (int level = topLevel; level >= 0; level--)
        {
            Node newSuccessor = victim.next[level].getReference();
            Node predecessor = preds.get(level);
            predecessor.next[level].set(newSuccessor, false);
        }
    }
}