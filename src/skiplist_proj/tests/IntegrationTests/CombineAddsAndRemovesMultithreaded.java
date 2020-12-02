package skiplist_proj.tests.IntegrationTests;

//import jdk.jshell.spi.ExecutionControl;
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
import java.util.concurrent.atomic.AtomicMarkableReference;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
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
        int[] listOfIntegersToAdd = new int[150];
        TestData.createListOfConsecutiveIntegers(listOfIntegersToAdd);

        // Remove nodes 0-147
        int[] listOfIntegersToRemove = new int[148];
        TestData.createListOfConsecutiveIntegers(listOfIntegersToRemove);

        // Determine if we should create a lock-based or lock-free skiplist
        boolean useLockBasedSkiplist = this.skiplist instanceof LockBasedSkiplist;

        // Act
        SkiplistRunnable.runTest(useLockBasedSkiplist, head, listOfIntegersToAdd, listOfIntegersToRemove);

        // Assert
        // Verify that the bottom level contains at least Node 98 and 99
        // (The rest of the nodes may or may not be present depending on when the individual threads executed)
        Node node = head.next[0].getReference();
        boolean found148 = false;
        boolean found149 = false;
        while (node.getItem() != Integer.MAX_VALUE) {
            if (node.getItem() == 148) found148 = true;
            if (node.getItem() == 149) found149 = true;

            node = node.next[0].getReference();
        }
        assertTrue(found148);
        assertTrue(found149);
    }

    /**
     * Verifies behavior when many integers are added and then removed from a pre-populated Skiplist
     */
    @Test
    public void addAndRemoveLargeTest2() {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);

        // Add nodes 0-149
        int[] listOfIntegersToAdd = new int[150];
        TestData.createListOfConsecutiveIntegers(listOfIntegersToAdd);

        // Remove nodes 0-147
        int[] listOfIntegersToRemove = new int[148];
        TestData.createListOfConsecutiveIntegers(listOfIntegersToRemove);

        // Determine if we should create a lock-based or lock-free skiplist
        boolean useLockBasedSkiplist = this.skiplist instanceof LockBasedSkiplist;

        // Act
        SkiplistRunnable.runTest(useLockBasedSkiplist, head, listOfIntegersToAdd, listOfIntegersToRemove);

        // Assert
        // Verify that the bottom level contains at least Node 98 and 99
        // (The rest of the nodes may or may not be present depending on when the individual threads executed)
        Node node = head.next[0].getReference();
        boolean found148 = false;
        boolean found149 = false;
        while (node.getItem() != Integer.MAX_VALUE)
        {
            if (node.getItem() == 148) found148 = true;
            if (node.getItem() == 149) found149 = true;

            node = node.next[0].getReference();
        }
        assertTrue(found148);
        assertTrue(found149);
    }
}