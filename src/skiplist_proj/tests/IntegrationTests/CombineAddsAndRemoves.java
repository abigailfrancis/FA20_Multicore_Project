package skiplist_proj.tests.IntegrationTests;

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
public class CombineAddsAndRemoves
{
    private Skiplist skiplist;

    private static List<AtomicReference<Node>> preds = new ArrayList<>();
    private static List<AtomicReference<Node>> succs = new ArrayList<>();
    private static Node head;

    public CombineAddsAndRemoves(Skiplist skiplist)
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
     * Verifies behavior when an integer is added and then removed from the Skiplist
     */
    @Test
    public void addIntegerThenRemoveSameInteger() {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);

        // Act
        boolean addSucceeded = this.skiplist.add(3);
        boolean removeSucceeded = this.skiplist.rm(3);

        // Assert
        assertTrue(addSucceeded);
        assertTrue(removeSucceeded);
    }

    /**
     * Verifies behavior when an integer is removed from and then added to the Skiplist
     */
    @Test
    public void removeIntegerAndReAddInteger() {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);

        // Act
        boolean removeSucceeded = this.skiplist.rm(5);
        boolean addSucceeded = this.skiplist.add(5);

        // Assert
        assertTrue(removeSucceeded);
        assertTrue(addSucceeded);
    }

    /**
     * Verifies behavior when multiple integers are added to & removed from the Skiplist
     */
    @Test
    public void addAndRemoveMultipleIntegers() {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);

        // Act
        boolean add1Succeeded = this.skiplist.add(50);
        boolean add2Succeeded = this.skiplist.add(1);
        boolean remove1Succeeded = this.skiplist.rm(2);
        boolean add3Succeeded = this.skiplist.add(3);
        boolean add4Succeeded = this.skiplist.add(3);

        // Assert
        assertTrue(add1Succeeded);
        assertTrue(add2Succeeded);
        assertTrue(remove1Succeeded);
        assertTrue(add3Succeeded);
        assertFalse(add4Succeeded);
    }
}