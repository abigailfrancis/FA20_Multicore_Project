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
//import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
//import static junit.framework.Assert.assertFalse;
//import static junit.framework.Assert.assertTrue;
import static skiplist_proj.Skiplist.MAX_HEIGHT;

@RunWith(Parameterized.class)
public class AddTests
{
    private Skiplist skiplist;

    private static List<AtomicMarkableReference<Node>> preds = new ArrayList<>();
    private static List<AtomicMarkableReference<Node>> succs = new ArrayList<>();
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
        //testParams.add(new LockBasedSkiplist(head));
        testParams.add(new LockFreeSkiplist(head));

        return testParams;
    }

    @After
    public void printSkiplist()
    {
        this.skiplist.display();
    }

    /**
     * Verifies behavior when one integer is added to an empty Skiplist
     */
    @Test
    public void addIntegerToAnEmptySkiplist() {
        // Arrange
        TestData.setupEmptySkiplist(head);
        printSkiplist();
        // Act
        boolean addSucceeded = this.skiplist.add(5);

        // Assert
        assertTrue(addSucceeded);
    }

    /*
    Verifies behavior when one integer is added to a populated skiplist
     */
    @Test
    public void addIntegerToAFullSkiplist() {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);

        // Act
        boolean addSucceeded = this.skiplist.add(15);

        // Assert
        assertTrue(addSucceeded);
    }

    /**
     * Verifies behavior when one integer (that is already in the SKiplist) is added to the Skiplist
     */
    @Test
    public void addIntegerToASkiplistThatAlreadyHasThatInteger() {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);
        printSkiplist();
        // Act
        boolean addSucceeded = this.skiplist.add(5);

        // Assert
        assertFalse(addSucceeded);
    }

    /**
     * Verifies behavior when an integer is added to the front of a Skiplist
     */
    @Test
    public void addIntegerToFrontOfSkiplist() {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);

        // Act
        boolean addSucceeded = this.skiplist.add(1);

        // Assert
        assertTrue(addSucceeded);
    }

    /**
     * Verifies behavior when one integer is added to the back of the Skiplist
     */
    @Test
    public void addIntegerToEndOfSkiplist() {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);

        // Act
        boolean addSucceeded = this.skiplist.add(500);

        // Assert
        assertTrue(addSucceeded);
    }
}