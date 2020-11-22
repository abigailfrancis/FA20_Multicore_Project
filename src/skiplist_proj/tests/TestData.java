package skiplist_proj.tests;

import skiplist_proj.Node;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static skiplist_proj.Skiplist.MAX_HEIGHT;

/**
 * A helper class for creating test skiplist data
 */
public class TestData
{
    private TestData()
    {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Populates head, preds, and succs with values for an empty Skiplist
     * @param head The head node for the Skiplist
     */
    public static void setupEmptySkiplist(Node head)
    {
        int height = MAX_HEIGHT;

        AtomicReference<Node> tail = new AtomicReference<>();
        tail.set(new Node(Integer.MAX_VALUE, height));

        // Test Skiplist
        // Level
        //   0        head -> tail

        for (int i = 0; i < height; i++)
        {
            // Set all of head's 'next' values to the tail
            head.next[i] = tail;
        }
    }

    /**
     * Populates head, preds, and succs with values for a full Skiplist with height = 4
     * @param head The head node for the Skiplist
     * @param preds The collection of predecessor nodes
     * @param succs The collection of successor nodes
     */
    public static void setupTestSkiplist1(Node head, List<AtomicReference<Node>> preds, List<AtomicReference<Node>> succs)
    {
        // Test Skiplist
        // Level
        //   3        head ->                                       end
        //   2        head ->                9 ->                   end
        //   1        head ->      5 ->      9 ->       18 ->       end
        //   0        head -> 2 -> 5 -> 8 -> 9 -> 11 -> 18 -> 25 -> end

        AtomicReference<Node> node2 = new AtomicReference<>();
        node2.set(createNewFullyLinkedNode(2, 1));

        AtomicReference<Node> node5 = new AtomicReference<>();
        node5.set(createNewFullyLinkedNode(5, 2));

        AtomicReference<Node> node8 = new AtomicReference<>();
        node8.set(createNewFullyLinkedNode(8, 1));

        AtomicReference<Node> node9 = new AtomicReference<>();
        node9.set(createNewFullyLinkedNode(9, 3));

        AtomicReference<Node> node11 = new AtomicReference<>();
        node11.set(createNewFullyLinkedNode(11, 1));

        AtomicReference<Node> node18 = new AtomicReference<>();
        node18.set(createNewFullyLinkedNode(18, 2));

        AtomicReference<Node> node25 = new AtomicReference<>();
        node25.set(createNewFullyLinkedNode(25, 1));

        AtomicReference<Node> tail = new AtomicReference<>();
        tail.set(new Node(Integer.MAX_VALUE, MAX_HEIGHT));

        head.next[0] = node2;
        head.next[1] = node5;
        head.next[2] = node9;
        head.next[3] = tail;

        node2.get().next[0] = node5;

        node5.get().next[0] = node8;
        node5.get().next[1] = node9;

        node8.get().next[0] = node9;

        node9.get().next[0] = node11;
        node9.get().next[1] = node18;
        node9.get().next[2] = tail;

        node11.get().next[0] = node18;

        node18.get().next[0] = node25;
        node18.get().next[1] = tail;

        node25.get().next[0] = tail;

        // Initialize empty values for preds & succs
        for (int i = 0; i < MAX_HEIGHT; i++)
        {
            preds.add(i, null);
            succs.add(i, null);
        }
    }

    /**
     * Helper method to create a new fully-linked Node
     * @param value The value to associate with the node
     * @param height The toplevel that the node should be evaluated for
     * @return
     */
    private static Node createNewFullyLinkedNode(Integer value, Integer height)
    {
        Node n = new Node(value, height);
        n.setFullyLinked(true);
        return n;
    }
}
