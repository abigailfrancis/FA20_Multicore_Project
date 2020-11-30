package skiplist_proj;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockBasedSkiplist implements Skiplist
{
    private AtomicMarkableReference<Node> head = new AtomicMarkableReference<>(null, false);
    private Random rnd = new Random();

    public LockBasedSkiplist(Node head)
    {
        this.head.set(head, false);
    }

    /**
     * @param value the value to be added to the Skiplist
     * @return True, if the integer was added to the Skiplist successfully
     */
    @Override
    public boolean add(Integer value)
    {
        // Chosoe a random level (0 <= x < MAX_HEIGHT) for the new node's top level
        int topLevel = chooseRandomTopLevel();

        // Initialize empty preds and succs lists
        List<AtomicMarkableReference<Node>> preds = new ArrayList<>();
        List<AtomicMarkableReference<Node>> succs = new ArrayList<>();

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
            	AtomicMarkableReference<Node> nodeFound = succs.get(levelFound);

                // If the successor node is not marked
                if (!nodeFound.getReference().isMarked()) {

                    // Wait for it to be fully-linked
                    while (!nodeFound.getReference().isFullyLinked()) {
                        // no-op
                    }

                    return false;
                }

                continue;
            }

            int highestLocked = -1;

            try
            {
            	AtomicMarkableReference<Node> pred;
            	AtomicMarkableReference<Node> succ;

                boolean valid = true;

                // Starting at the bottom of the Skiplist
                // Lock all of the predecessor nodes
                for (int level = 0; valid && (level <= topLevel); level++)
                {
                    pred = preds.get(level);
                    succ = succs.get(level);
                    pred.getReference().lock();
                    highestLocked = level;
                    valid = !pred.getReference().isMarked() && !succ.getReference().isMarked() && pred.getReference().next[level] == succ;
                }

                if (!valid) continue;

                // Create the new Node
                Node node = new Node(value, topLevel);
                AtomicMarkableReference<Node> newNode = new AtomicMarkableReference<>(node, false);

                // Starting at the bottom of the Skiplist
                // Attach the new Node's successors
                for (int level = 0; level <= topLevel; level++)
                {
                    newNode.getReference().next[level].set(succs.get(level).getReference(), false);
                }

                // Starting at the bottom of the Skiplist
                // Attach the new Node's predecessors
                for (int level = 0; level <= topLevel; level++)
                {
                    preds.get(level).getReference().next[level] = newNode;
                }

                newNode.getReference().setFullyLinked(true);

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
    @Override
    public boolean rm(Integer value)
    {
    	AtomicMarkableReference<Node> victim = null;
        boolean isMarked = false;
        Integer topLevel = -1;

        // Initialize empty preds and succs lists
        List<AtomicMarkableReference<Node>> preds = new ArrayList<>();
        List<AtomicMarkableReference<Node>> succs = new ArrayList<>();

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
                            && (victim.getReference().isFullyLinked()
                            && victim.getReference().getTopLevel().equals(levelFound)
                            && !victim.getReference().isMarked()));
			if(shouldEvaluateNode)
            {
                if (!isMarked)
                {
                    // Determine the highest level of the Skiplist that this value
                    // is present on.
                    topLevel = victim.getReference().getTopLevel();

                    victim.getReference().lock();

                    if (victim.getReference().isMarked())
                    {
                        victim.getReference().unlock();
                        return false;
                    }

                    victim.getReference().setMarked(true);
                    isMarked = true;
                }

                Integer highestLocked = -1;

                try
                {
                	AtomicMarkableReference<Node> pred;

                    boolean valid = true;

                    // Starting at the bottom of the Skiplist
                    // Lock the predecessor nodes
                    for (int level = 0; valid && (level <= topLevel); level++)
                    {
                        pred = preds.get(level);
                        pred.getReference().lock();
                        highestLocked = level;
                        valid = !pred.getReference().isMarked() && pred.getReference().next[level].getReference() == victim.getReference();
                    }

                    if (!valid) continue;

                    // Starting at the top of the Skiplist
                    // Reassign the predecessor nodes
                    reassignPredecessorNodes(victim, topLevel, preds);

                    // The victim has been removed, unlock it
                    victim.getReference().unlock();

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
    @Override
    public Integer find(Integer value, List<AtomicMarkableReference<Node>> preds, List<AtomicMarkableReference<Node>> succs)
    {
        // To begin, set the predecessor node to the HEAD of the Skiplist
    	AtomicMarkableReference<Node> predecessor = head;

        // If the value is not found, we will return -1
        int lFound = -1;

        // Start the search at the highest level of the Skiplist
        for (int level = MAX_HEIGHT; level >= 0; level--)
        {
            // Set curr to the predecessor node's successor
        	AtomicMarkableReference<Node> curr = predecessor.getReference().next[level];

            // If curr is still less than the value we are trying to find
            while (value > curr.getReference().getItem())
            {
                // Advance the predecessor
                predecessor = curr;

                // Set curr to the predecessor node's successor
                curr = predecessor.getReference().next[level];
            }

            // If the key is found, update the return variable
            if (lFound == -1 && curr.getReference().getItem().equals(value))
            {
                lFound = level;
            }

            // Set the new predecessor and successor
            preds.set(level, predecessor);
            succs.set(level, curr);
        }

        return lFound;
	}

	@Override
    public void display()
    {
        System.out.println("---------------------");
        for (int level = MAX_HEIGHT; level >= 0; level--)
        {
            System.out.print("Level " + level + ": ");

            Node node = this.head.getReference();

            while (node != null)
            {
                System.out.print(" -> " + node);
                node = node.next[level].getReference();
            }

            System.out.println();
        }
        System.out.println("---------------------");
        System.out.println();
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
    private void unlockAtAllLevels(List<AtomicMarkableReference<Node>> nodeCollection, int highestLocked) {
        for (int level = 0; level <= highestLocked; level++)
        {
            nodeCollection.get(level).getReference().unlock();
        }
    }

    /**
     * Reassign the predecessor nodes, to fully remove the victim
     * @param victim The node we are trying to remove
     * @param topLevel The highest level of the Skiplist that we need to consider
     * @param preds The predecessor nodes
     */
    private void reassignPredecessorNodes(AtomicMarkableReference<Node> victim, Integer topLevel, List<AtomicMarkableReference<Node>> preds) {
        for (int level = topLevel; level >= 0; level--)
        {
            AtomicMarkableReference<Node> newSuccessor = victim.getReference().next[level];
            Node predecessor = preds.get(level).getReference();
            predecessor.next[level] = newSuccessor;
        }
    }
}