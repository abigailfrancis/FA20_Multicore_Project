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
import java.util.concurrent.atomic.AtomicReference;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static skiplist_proj.Skiplist.MAX_HEIGHT;

@RunWith(Parameterized.class)
public class AddTestsMultithreaded
{
    private Skiplist skiplist;

    private static List<AtomicReference<Node>> preds = new ArrayList<>();
    private static List<AtomicReference<Node>> succs = new ArrayList<>();
    private static Node head;

    public AddTestsMultithreaded(Skiplist skiplist)
    {
        this.skiplist = skiplist;
    }

    @Parameterized.Parameters()
    public static Collection<Skiplist> data() {
        ArrayList<Skiplist> testParams = new ArrayList<>();

        // Initialize the head node
        head = new Node(Integer.MIN_VALUE, MAX_HEIGHT);

        // Run with both types of Skiplist
        testParams.add(new LockBasedSkiplist(head));
        testParams.add(new LockFree());

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
        TestData.setupEmptySkiplist(this.head);
        int[] listOfIntegersToAdd = {1, 3, 5};
        int[] listOfIntegersToRemove = {};
        boolean useLockBasedSkiplist = this.skiplist instanceof LockBasedSkiplist;

        // Act
        SkiplistRunnable.runTest(useLockBasedSkiplist, this.head, listOfIntegersToAdd, listOfIntegersToRemove);

        // Assert
        // Verify that the bottom level has 1 -> 3 -> 5
        Node node1 = this.head.next[0].get();
        assertTrue(this.head.next[0].get().getItem() == 1);

        Node node2 = node1.next[0].get();
        assertTrue(node2.getItem() == 3);

        Node node3 = node2.next[0].get();
        assertTrue(node3.getItem() == 5);
    }

    /**
     * Verifies behavior when multiple threads add integers to the same populated Skiplist
     */
    @Test
    public void addIntegersToAPopulatedSkiplist() {
        // Arrange
        TestData.setupTestSkiplist1(this.head, preds, succs);
        int[] listOfIntegersToAdd = {7, 9, 11};
        int[] listOfIntegersToRemove = {};
        boolean useLockBasedSkiplist = this.skiplist instanceof LockBasedSkiplist;

        // Act
        SkiplistRunnable.runTest(useLockBasedSkiplist, this.head, listOfIntegersToAdd, listOfIntegersToRemove);

        // Assert
        // Go through the bottom level of the skiplist and look for all 3 of the added integers
        Node next = this.head.next[0].get();
        int indexOfAddedItem = 0;
        int skipListLength = 0;

        while(next.getItem() != Integer.MAX_VALUE)
        {
            if(indexOfAddedItem < listOfIntegersToAdd.length && next.getItem() == listOfIntegersToAdd[indexOfAddedItem])
            {
                indexOfAddedItem++;
            }
            next = next.next[0].get();
            skipListLength++;
        }
        assertTrue(indexOfAddedItem == 3);
        assertTrue(skipListLength == 8);
    }

    /**
     * Large test for adding integers to Skiplist
     */
    @Test
    public void addLotsOfIntegersToAnEmptySkiplist() {
        // Arrange
        TestData.setupTestSkiplist1(this.head, preds, succs);

        int[] listOfIntegersToAdd = new int[100];
        TestData.createListOfConsecutiveIntegers(listOfIntegersToAdd);

        int[] listOfIntegersToRemove = {};
        boolean useLockBasedSkiplist = this.skiplist instanceof LockBasedSkiplist;

        // Act
        SkiplistRunnable.runTest(useLockBasedSkiplist, this.head, listOfIntegersToAdd, listOfIntegersToRemove);

        // Assert
        // Go through the bottom level of the skiplist and look for all 3 of the added integers
        Node next = this.head.next[0].get();
        int indexOfAddedItem = 0;
        int skipListLength = 0;

        while(next.getItem() != Integer.MAX_VALUE)
        {
            if(indexOfAddedItem < listOfIntegersToAdd.length && next.getItem() == listOfIntegersToAdd[indexOfAddedItem])
            {
                indexOfAddedItem++;
            }
            next = next.next[0].get();
            skipListLength++;
        }

        assertTrue(indexOfAddedItem == 100);
        assertTrue(skipListLength == 100);
    }
}