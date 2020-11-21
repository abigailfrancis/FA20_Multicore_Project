package skiplist_proj;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static junit.framework.Assert.assertTrue;

@RunWith(Parameterized.class)
public class Tests {

    private Skiplist skiplist;
    private static int height = 4;

    private static AtomicReference<Node>[] preds = new AtomicReference[height];
    private static AtomicReference<Node>[] succs = new AtomicReference[height];
    private static Node head;

    public Tests(Skiplist skiplist)
    {
        this.skiplist = skiplist;
    }

    @Parameterized.Parameters()
    public static Collection<Skiplist> data() {
        ArrayList<Skiplist> testParams = new ArrayList<>();

        // Initialize the head node
        head = new Node(-9999, height);

        // Run with both types of Skiplist
        testParams.add(new LockBasedSkiplist(head));
        testParams.add(new LockFree());

        return testParams;
    }

    @Test
    public void findIntegerThatIsPresentInMiddleLevelOfSkipList()
    {
        // Arrange
        setupTestSkiplist1();

        // Act
        Integer level = this.skiplist.find(5, preds, succs);

        // Assert
        assertTrue(level == 1);
    }

    @Test
    public void findIntegerThatIsPresentInTopLevelOfSkipList()
    {
        // Arrange
        setupTestSkiplist1();

        // Act
        Integer level = this.skiplist.find(2, preds, succs);

        // Assert
        assertTrue(level == 0);
    }

    @Test
    public void findIntegerThatIsNotPresentInSkipList()
    {
        // Arrange
        setupTestSkiplist1();

        // Act
        Integer level = this.skiplist.find(500, preds, succs);

        // Assert
        assertTrue(level == -1);
    }

    private void setupTestSkiplist1()
    {
        AtomicReference<Node> node2 = new AtomicReference<>();
        node2.set(new Node(2, height));

        AtomicReference<Node> node5 = new AtomicReference<>();
        node5.set(new Node(5, height));

        AtomicReference<Node> node8 = new AtomicReference<>();
        node8.set(new Node(8, height));

        AtomicReference<Node> node9 = new AtomicReference<>();
        node9.set(new Node(9, height));

        AtomicReference<Node> node11 = new AtomicReference<>();
        node11.set(new Node(11, height));

        AtomicReference<Node> node18 = new AtomicReference<>();
        node18.set(new Node(18, height));

        AtomicReference<Node> node25 = new AtomicReference<>();
        node25.set(new Node(25, height));

        AtomicReference<Node> end = new AtomicReference<>();
        end.set(new Node(9999, height));

        // Test Skiplist
        // Level
        //   3        head ->                                       end
        //   2        head ->                9 ->                   end
        //   1        head ->      5 ->      9 ->       18 ->       end
        //   0        head -> 2 -> 5 -> 8 -> 9 -> 11 -> 18 -> 25 -> end

        head.next[0] = node2;
        head.next[1] = node5;
        head.next[2] = node9;
        head.next[3] = end;

        node2.get().next[0] = node5;

        node5.get().next[0] = node8;
        node5.get().next[1] = node9;

        node8.get().next[0] = node9;

        node9.get().next[0] = node11;
        node9.get().next[1] = node18;
        node9.get().next[2] = end;

        node11.get().next[0] = node18;

        node18.get().next[0] = node25;
        node18.get().next[1] = end;

        node25.get().next[0] = end;
    }
}