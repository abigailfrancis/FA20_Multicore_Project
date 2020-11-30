package skiplist_proj.tests;

import skiplist_proj.Node;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicMarkableReference;

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
        AtomicMarkableReference<Node> tail = new AtomicMarkableReference<>(null, false);
        tail.set(new Node(Integer.MAX_VALUE, MAX_HEIGHT),false);

        // Test Skiplist
        // Level
        //   3        head -> tail
        //   2        head -> tail
        //   1        head -> tail
        //   0        head -> tail

        for (int i = 0; i <= MAX_HEIGHT; i++)
        {
            // Set all of head's 'next' values to the tail
            //head.next[i] = tail;
        	head.next[i].set(new Node(Integer.MAX_VALUE, MAX_HEIGHT),false);
        }
    }

    /**
     * Populates head, preds, and succs with values for a full Skiplist with height = 4
     * @param head The head node for the Skiplist
     * @param preds The collection of predecessor nodes
     * @param succs The collection of successor nodes
     */
    public static void setupTestSkiplist1(Node head, List<AtomicMarkableReference<Node>> preds, List<AtomicMarkableReference<Node>> succs)
    {
        // Test Skiplist
        // Level
        //   3        head ->                                       end
        //   2        head ->                9 ->                   end
        //   1        head ->      5 ->      9 ->       18 ->       end
        //   0        head -> 2 -> 5 -> 8 -> 9 -> 11 -> 18 -> 25 -> end


        AtomicMarkableReference<Node> node2 = new AtomicMarkableReference<>(null, false);
        node2.set(createNewFullyLinkedNode(2, 0), false);

        AtomicMarkableReference<Node> node5 = new AtomicMarkableReference<>(null, false);
        node5.set(createNewFullyLinkedNode(5, 1), false);

        AtomicMarkableReference<Node> node8 = new AtomicMarkableReference<>(null, false);
        node8.set(createNewFullyLinkedNode(8, 0), false);

        AtomicMarkableReference<Node> node9 = new AtomicMarkableReference<>(null, false);
        node9.set(createNewFullyLinkedNode(9, 2), false);

        AtomicMarkableReference<Node> node11 = new AtomicMarkableReference<>(null, false);
        node11.set(createNewFullyLinkedNode(11, 0), false);

        AtomicMarkableReference<Node> node18 = new AtomicMarkableReference<>(null, false);
        node18.set(createNewFullyLinkedNode(18, 1), false);

        AtomicMarkableReference<Node> node25 = new AtomicMarkableReference<>(null, false);
        node25.set(createNewFullyLinkedNode(25, 0), false);

        AtomicMarkableReference<Node> tail = new AtomicMarkableReference<>(null, false);
        tail.set(new Node(Integer.MAX_VALUE, MAX_HEIGHT), false);

//        head.next[0] = node2;
//        head.next[1] = node5;
//        head.next[2] = node9;
//        head.next[3] = tail;
        head.next[0].set(node2.getReference(), node2.isMarked());
        head.next[1].set(node5.getReference(), node5.isMarked());
        head.next[2].set(node9.getReference(), node9.isMarked());
        head.next[3].set(tail.getReference(), tail.isMarked());
        
        node2.getReference().next[0].set(node5.getReference(), node5.isMarked());;

        node5.getReference().next[0].set(node8.getReference(), node8.isMarked());
        node5.getReference().next[1].set(node9.getReference(), node9.isMarked());

        node8.getReference().next[0].set(node9.getReference(), node9.isMarked());

        node9.getReference().next[0].set(node11.getReference(), node11.isMarked());
        node9.getReference().next[1].set(node18.getReference(), node18.isMarked());
        node9.getReference().next[2].set(tail.getReference(), tail.isMarked());

        node11.getReference().next[0].set(node18.getReference(), node18.isMarked());

        node18.getReference().next[0].set(node25.getReference(), node25.isMarked());
        node18.getReference().next[1].set(tail.getReference(), tail.isMarked());

        node25.getReference().next[0].set(tail.getReference(), tail.isMarked());

        // Initialize empty values for preds & succs
        for (int i = 0; i <= MAX_HEIGHT; i++)
        {
            preds.add(i, null);
            succs.add(i, null);
        }
    }

    /**
     * Populates the provided array with a list of consecutive integers
     * @param listOfIntegers
     */
    public static void createListOfConsecutiveIntegers(int[] listOfIntegers) {
        for(int i = 0; i < listOfIntegers.length; i++)
        {
            listOfIntegers[i] = i;
        }
    }

    /**
     * Populates the provided array with a list of random integers
     * @param listOfIntegers
     */
    public static void createListOfRandomIntegers(int[] listOfIntegers) {
        Random rand = new Random();
        for(int i = 0; i < listOfIntegers.length; i++)
        {
            listOfIntegers[i] = rand.nextInt();
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
