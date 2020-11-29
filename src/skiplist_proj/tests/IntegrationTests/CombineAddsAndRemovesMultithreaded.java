package skiplist_proj.tests.IntegrationTests;

//import jdk.jshell.spi.ExecutionControl;
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
public class CombineAddsAndRemovesMultithreaded
{
    private Skiplist skiplist;

    private static List<AtomicReference<Node>> preds = new ArrayList<>();
    private static List<AtomicReference<Node>> succs = new ArrayList<>();
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
     * Verifies behavior when many integers are added and then removed from the Skiplist
     */
   // @Test
   // public void addAndRemoveLargeTest() throws ExecutionControl.NotImplementedException {
     //   throw new ExecutionControl.NotImplementedException("");
   // }
}