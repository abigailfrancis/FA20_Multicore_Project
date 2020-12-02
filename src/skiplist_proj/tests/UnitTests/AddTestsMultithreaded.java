package skiplist_proj.tests.UnitTests;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import skiplist_proj.*;
import skiplist_proj.tests.TestData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.assertTrue;
import static skiplist_proj.Skiplist.MAX_HEIGHT;

@RunWith(Parameterized.class)
public class AddTestsMultithreaded
{
    private Skiplist skiplist;

    private static List<Node> preds = new ArrayList<>();
    private static List<Node> succs = new ArrayList<>();
    private static Node head;

    public AddTestsMultithreaded(Skiplist skiplist)
    {
        this.skiplist = skiplist;
    }

    @Parameterized.Parameters()
    public static Collection<Skiplist> data() {
        ArrayList<Skiplist> testParams = new ArrayList<>();
        preds = new ArrayList<>();
        succs = new ArrayList<>();

        // Initialize the head node
        head = new Node(Integer.MIN_VALUE, MAX_HEIGHT);

        // Run with both types of Skiplist
        testParams.add(new LockFreeSkiplist(head));
        testParams.add(new LockBasedSkiplist(head));
        
        return testParams;
    }

    @After
    public void printSkiplist()
    {
        this.skiplist.display();
    }

    /**
     * Verifies behavior when multiple threads add integers to the same empty Skiplist
     */
    @Test
    public void addIntegersToAnEmptySkiplist() {
        // Arrange
        TestData.setupEmptySkiplist(head);
        int[] listOfIntegersToAdd = {1, 3, 5};
        int[] listOfIntegersToRemove = {};
        boolean useLockBasedSkiplist = this.skiplist instanceof LockBasedSkiplist;

        // Act
        SkiplistRunnable.runTest(useLockBasedSkiplist, head, listOfIntegersToAdd, listOfIntegersToRemove);
        
        // Assert
        // Verify that the bottom level has 1 -> 3 -> 5
        Node node1 = head.next[0].getReference();
        
        assertTrue(head.next[0].getReference().getItem() == 1);

        Node node2 = node1.next[0].getReference();
        assertTrue(node2.getItem() == 3);

        Node node3 = node2.next[0].getReference();
        assertTrue(node3.getItem() == 5);
    }

    /**
     * Verifies behavior when multiple threads add integers to the same populated Skiplist
     */
    @Test
    public void addIntegersToAPopulatedSkiplist() {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);
        this.skiplist.display();

        int[] listOfIntegersToAdd = {7, 20, 21};
        int[] listOfIntegersToRemove = {};
        boolean useLockBasedSkiplist = this.skiplist instanceof LockBasedSkiplist;
        System.out.println("START ADD INTEGERS TO A POPULATED SKIPLIST");
        System.out.println("BEFORE:");
        this.skiplist.display();
        // Act
        SkiplistRunnable.runTest(useLockBasedSkiplist, this.head, listOfIntegersToAdd, listOfIntegersToRemove);
        System.out.println("AFTER:");
        this.skiplist.display();
        // Assert
        
        // Go through the bottom level of the skiplist and look for all 3 of the added integers
        Node next = head.next[0].getReference();
        int indexOfAddedItem = 0;
        int skipListLength = 0;

        while(next.getItem() != Integer.MAX_VALUE)
        {
            if(indexOfAddedItem < listOfIntegersToAdd.length && next.getItem() == listOfIntegersToAdd[indexOfAddedItem])
            {
                indexOfAddedItem++;
            }
            next = next.next[0].getReference();
            skipListLength++;
        }
        System.out.println("INDEX OF ADDED ITEM == 3: "+ indexOfAddedItem);
        assertTrue(indexOfAddedItem == 3);
        
        System.out.println("INDEX OF ADDED ITEM == 8: "+ skipListLength);
        assertTrue(skipListLength == 10);
        System.out.println("END ADD INTEGERS TO A POPULATED SKIPLIST");
    }

    /**
     * Large test for adding integers to Skiplist
     */
    @Test
    public void addLotsOfIntegersToAnEmptySkiplist() {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);

        int[] listOfIntegersToAdd = new int[1000];
        TestData.createListOfConsecutiveIntegers(listOfIntegersToAdd);

        int[] listOfIntegersToRemove = {};
        boolean useLockBasedSkiplist = this.skiplist instanceof LockBasedSkiplist;

        // Act
        SkiplistRunnable.runTest(useLockBasedSkiplist, head, listOfIntegersToAdd, listOfIntegersToRemove);
        
        // Assert
        // Go through the bottom level of the skiplist and look for all 3 of the added integers
        Node next = head.next[0].getReference();
        int indexOfAddedItem = 0;
        int skipListLength = 0;
        this.skiplist.display();
        while(next.getItem() != Integer.MAX_VALUE)
        {
            if(indexOfAddedItem < listOfIntegersToAdd.length && next.getItem() == listOfIntegersToAdd[indexOfAddedItem])
            {
                indexOfAddedItem++;
            }
            next = next.next[0].getReference();
            skipListLength++;
        }
        System.out.println("INDEX OF ADDED ITEM (100 expected): " + indexOfAddedItem);
        System.out.println("SKIPLIST LENGTH (100 expected): " + skipListLength);
        assertTrue(indexOfAddedItem == 1000);
        assertTrue(skipListLength == 1000);
    }
}