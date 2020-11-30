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
import java.util.concurrent.atomic.AtomicMarkableReference;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static skiplist_proj.Skiplist.MAX_HEIGHT;

@RunWith(Parameterized.class)
public class RemoveTests
{
    private Skiplist skiplist;

    private static List<AtomicMarkableReference<Node>> preds = new ArrayList<>();
    private static List<AtomicMarkableReference<Node>> succs = new ArrayList<>();
    private static Node head;

    public RemoveTests(Skiplist skiplist)
    {
        this.skiplist = skiplist;
    }

    @Parameterized.Parameters()
    public static Collection<Skiplist> data()
    {
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

    @Test
    public void removeIntegerFromAnEmptyList() {
        // Arrange
        TestData.setupEmptySkiplist(head);

        // Act
        boolean removeSucceeded = this.skiplist.rm(5);

        // Assert
        assertFalse(removeSucceeded);
    }

    @Test
    public void removeIntegerFromMiddleOfASkiplist() {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);

        // Act
        boolean removeSucceeded = this.skiplist.rm(5);

        // Assert
        assertTrue(removeSucceeded);
    }

    @Test
    public void removeIntegerFromBeginningOfASkiplist() {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);

        // Act
        // Remove first element from test skiplist
        boolean removeSucceeded = this.skiplist.rm(2);

        // Assert
        assertTrue(removeSucceeded);
    }

    @Test
    public void removeIntegerFromEndOfASkiplist() {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);

        // Act
        // Remove last element from test skiplist
        boolean removeSucceeded = this.skiplist.rm(25);

        // Assert
        assertTrue(removeSucceeded);
    }

    @Test
    public void removeIntegerFromSkiplistWhenIntegerIsNotPresent() {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);

        // Act
        boolean removeSucceeded = this.skiplist.rm(500);

        // Assert
        assertFalse(removeSucceeded);
    }
}