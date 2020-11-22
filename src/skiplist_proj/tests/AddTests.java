package skiplist_proj.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import skiplist_proj.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static skiplist_proj.Skiplist.MAX_HEIGHT;

@RunWith(Parameterized.class)
public class AddTests
{
    private Skiplist skiplist;

    private static List<AtomicReference<Node>> preds = new ArrayList<>();
    private static List<AtomicReference<Node>> succs = new ArrayList<>();
    private static Node head;

    public AddTests(Skiplist skiplist)
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

    @Test
    public void addIntegerToAnEmptySkiplist() {
    // Arrange
    TestData.setupEmptySkiplist(head, preds, succs);

    // Act
    boolean addSucceeded = this.skiplist.add(5);

    // Assert
    assertTrue(addSucceeded);
}

    @Test
    public void addIntegerToAFullSkiplist() {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);

        // Act
        boolean addSucceeded = this.skiplist.add(15);

        // Assert
        assertTrue(addSucceeded);
    }

    @Test
    public void addIntegerToASkiplistThatAlreadyHasThatInteger() {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);

        // Act
        boolean addSucceeded = this.skiplist.add(5);

        // Assert
        assertFalse(addSucceeded);
    }
}