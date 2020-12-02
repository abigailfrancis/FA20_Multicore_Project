package skiplist_proj.tests.UnitTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import skiplist_proj.*;
import skiplist_proj.tests.TestData;

import java.util.*;

import static org.junit.Assert.assertTrue;
import static skiplist_proj.Skiplist.MAX_HEIGHT;

@RunWith(Parameterized.class)
public class FindTests {

    private Skiplist skiplist;
    private static int height = MAX_HEIGHT;

    private static List<Node> preds = new ArrayList<>();
    private static List<Node> succs = new ArrayList<>();
    private static Node head;

    public FindTests(Skiplist skiplist)
    {
        this.skiplist = skiplist;
    }

    @Parameterized.Parameters()
    public static Collection<Skiplist> data() {
        ArrayList<Skiplist> testParams = new ArrayList<>();

        // Initialize the head node
        head = new Node(Integer.MIN_VALUE, height);

        // Run with both types of Skiplist
        testParams.add(new LockBasedSkiplist(head));
        testParams.add(new LockFreeSkiplist(head));

        return testParams;
    }

    @Test
    public void findIntegerThatIsPresentInMiddleLevelOfSkipList()
    {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);
        this.skiplist.display();
        // Act
        Integer level = this.skiplist.find(5, preds, succs);
        this.skiplist.display();
        // Assert
        assertTrue(level == 1);
    }

    @Test
    public void findIntegerThatIsPresentInTopLevelOfSkipList()
    {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);
        this.skiplist.display();
        // Act
        Integer level = this.skiplist.find(2, preds, succs);
        this.skiplist.display();
        // Assert
        assertTrue(level == 0);
    }

    @Test
    public void findIntegerThatIsNotPresentInSkipList()
    {
        // Arrange
        TestData.setupTestSkiplist1(head, preds, succs);
        this.skiplist.display();
        // Act
        Integer level = this.skiplist.find(500, preds, succs);
        this.skiplist.display();
        // Assert
        assertTrue(level == -1);
    }
}