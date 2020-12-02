package skiplist_proj.tests.IntegrationTests;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import skiplist_proj.*;
import skiplist_proj.tests.TestData;
import skiplist_proj.tests.UnitTests.SkiplistRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static skiplist_proj.Skiplist.MAX_HEIGHT;

@RunWith(Parameterized.class)
public class CombineAddsAndRemovesMultithreaded
{
    private Skiplist skiplist;

    private static List<Node> preds = new ArrayList<>();
    private static List<Node> succs = new ArrayList<>();
    private static Node head;

    public CombineAddsAndRemovesMultithreaded(Skiplist skiplist)
    {
        this.skiplist = skiplist;
    }

    @Parameterized.Parameters()
    public static Collection<Skiplist> data() {
        ArrayList<Skiplist> testParams = new ArrayList<>();

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
     * Verifies behavior when many integers are added and then removed from an empty Skiplist
     */
    @Test
    public void addAndRemoveLargeTest() {
        // Arrange
        TestData.setupEmptySkiplist(head);

        // Add nodes 0-149
        int[] listOfIntegersToAdd = new int[1500];
        TestData.createListOfConsecutiveIntegers(listOfIntegersToAdd);

        // Remove nodes 0-147
        int[] listOfIntegersToRemove = new int[1498];
        TestData.createListOfConsecutiveIntegers(listOfIntegersToRemove);

        // Determine if we should create a lock-based or lock-free skiplist
        boolean useLockBasedSkiplist = this.skiplist instanceof LockBasedSkiplist;

        // Act
        SkiplistRunnable.runTest(useLockBasedSkiplist, head, listOfIntegersToAdd, listOfIntegersToRemove);

        // Assert
        // Verify that the bottom level contains at least Node 98 and 99
        // (The rest of the nodes may or may not be present depending on when the individual threads executed)
        Node node = head.next[0].getReference();
        boolean found1498 = false;
        boolean found1499 = false;
        while (node.getItem() != Integer.MAX_VALUE) {
            if (node.getItem() == 1498) found1498 = true;
            if (node.getItem() == 1499) found1499 = true;

            node = node.next[0].getReference();
        }
        assertTrue(found1498);
        assertTrue(found1499);
    }

    /**
     * Verifies behavior when many integers are added and then removed from a pre-populated Skiplist
     */
    @Test
    public void addAndRemoveLargeTest2() {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);

        // Add nodes 0-149
        int[] listOfIntegersToAdd = new int[1500];
        TestData.createListOfConsecutiveIntegers(listOfIntegersToAdd);

        // Remove nodes 0-147
        int[] listOfIntegersToRemove = new int[1498];
        TestData.createListOfConsecutiveIntegers(listOfIntegersToRemove);

        // Determine if we should create a lock-based or lock-free skiplist
        boolean useLockBasedSkiplist = this.skiplist instanceof LockBasedSkiplist;

        // Act
        SkiplistRunnable.runTest(useLockBasedSkiplist, head, listOfIntegersToAdd, listOfIntegersToRemove);

        // Assert
        // Verify that the bottom level contains at least Node 98 and 99
        // (The rest of the nodes may or may not be present depending on when the individual threads executed)
        Node node = head.next[0].getReference();
        boolean found1498 = false;
        boolean found1499 = false;
        while (node.getItem() != Integer.MAX_VALUE)
        {
            if (node.getItem() == 1498) found1498 = true;
            if (node.getItem() == 1499) found1499 = true;

            node = node.next[0].getReference();
        }
        assertTrue(found1498);
        assertTrue(found1499);
    }
}