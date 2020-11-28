package skiplist_proj.tests.unit_tests;

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
public class RemoveTestsMultithreaded
{
    private Skiplist skiplist;

    private static List<AtomicReference<Node>> preds = new ArrayList<>();
    private static List<AtomicReference<Node>> succs = new ArrayList<>();
    private static Node head;

    public RemoveTestsMultithreaded(Skiplist skiplist)
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
     * Verifies behavior when multiple threads remove integers from the same empty Skiplist
     */
    @Test
    public void removeIntegersFromEmptyList() {
        // Arrange
        TestData.setupEmptySkiplist(this.head);
        int[] listOfIntegersToAdd = {};
        int[] listOfIntegersToRemove = {1, 2, 3};
        boolean useLockBasedSkiplist = this.skiplist instanceof LockBasedSkiplist;

        // Act
        SkiplistRunnable.runTest(useLockBasedSkiplist, this.head, listOfIntegersToAdd, listOfIntegersToRemove);

        // Assert
        Node node1 = this.head.next[0].get();
        assertTrue(node1.getItem() == Integer.MAX_VALUE);
    }

    /**
     * Verifies behavior when multiple threads remove all the integers from the same populated Skiplist
     */
    @Test
    public void removeAllIntegersFromPopulatedSkiplist() {
        // Arrange
        TestData.setupTestSkiplist1(this.head, preds, succs);
        int[] listOfIntegersToAdd = {};
        int[] listOfIntegersToRemove = {2, 5, 8, 9, 11, 18, 25};
        boolean useLockBasedSkiplist = this.skiplist instanceof LockBasedSkiplist;

        // Act
        SkiplistRunnable.runTest(useLockBasedSkiplist, this.head, listOfIntegersToAdd, listOfIntegersToRemove);

        // Assert
        for (int i = 0; i < MAX_HEIGHT; i++)
        {
            Node node1 = this.head.next[i].get();
            assertTrue(node1.getItem() == Integer.MAX_VALUE);
        }
    }
}