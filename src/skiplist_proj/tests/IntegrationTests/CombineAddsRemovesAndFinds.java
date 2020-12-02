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
import java.util.concurrent.atomic.AtomicMarkableReference;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static skiplist_proj.Skiplist.MAX_HEIGHT;

@RunWith(Parameterized.class)
public class CombineAddsRemovesAndFinds
{
    private Skiplist skiplist;

    private static List<Node> preds = new ArrayList<>();
    private static List<Node> succs = new ArrayList<>();
    private static Node head;

    public CombineAddsRemovesAndFinds(Skiplist skiplist)
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
        testParams.add(new LockFreeSkiplist(head));

        return testParams;
    }

    @After
    public void printSkiplist()
    {
        this.skiplist.display();
    }

    /**
     * Verifies behavior when an integer is added and then we try to find it
     */
    @Test
    public void addIntegerThenFindInteger() {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);

        // Act
        Integer find1Level = this.skiplist.find(3, preds, succs);
        boolean addSucceeded = this.skiplist.add(3);
        Integer find2Level = this.skiplist.find(3, preds, succs);

        // Assert
        assertTrue(find1Level == -1);
        assertTrue(addSucceeded);
        assertTrue(find2Level > -1);
    }

    /**
     * Verifies behavior when an integer is removed from and then we try to find it
     */
    @Test
    public void removeIntegerThenFindInteger() {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);

        // Act
        Integer find1Level = this.skiplist.find(5, preds, succs);
        boolean removeSucceeded = this.skiplist.rm(5);
        Integer find2Level = this.skiplist.find(5, preds, succs);

        // Assert
        assertTrue(find1Level > -1);
        assertTrue(removeSucceeded);
        assertTrue(find2Level == -1);
    }

    /**
     * Verifies behavior when multiple integers are searched for, added, and removed from the Skiplist
     */
    @Test
    public void addRemoveAndFindMultipleIntegers() {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);

        // Act
        Integer find1Level = this.skiplist.find(50, preds, succs);
        boolean add1Succeeded = this.skiplist.add(50);
        boolean add2Succeeded = this.skiplist.add(1);
        boolean remove1Succeeded = this.skiplist.rm(2);
        Integer find2Level = this.skiplist.find(2, preds, succs);
        boolean add3Succeeded = this.skiplist.add(3);
        Integer find3Level = this.skiplist.find(3, preds, succs);

        // Assert
        assertTrue(find1Level == -1);
        assertTrue(add1Succeeded);
        assertTrue(add2Succeeded);
        assertTrue(remove1Succeeded);
        assertTrue(find2Level == -1);
        assertTrue(add3Succeeded);
        assertTrue(find3Level > -1);
    }
}