package skiplist_proj.tests.UnitTests;

import skiplist_proj.LockBasedSkiplist;
import skiplist_proj.LockFreeSkiplist;
import skiplist_proj.Node;
import skiplist_proj.Skiplist;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static skiplist_proj.Skiplist.MAX_HEIGHT;

public class SkiplistRunnable implements Runnable
{
    private Integer integerToAdd;
    private Integer integerToRemove;
    private Skiplist skiplist;

    public SkiplistRunnable(boolean lockBasedSkiplist, Node head, Integer integerToAdd, Integer integerToRemove)
    {
        this.integerToAdd = integerToAdd;
        this.integerToRemove = integerToRemove;

        if(lockBasedSkiplist)
        {
            this.skiplist = new LockBasedSkiplist(head);
        }
        else {
            this.skiplist = new LockFreeSkiplist(head);
        }
    }

    public static void runTest(boolean useLockBasedSkiplist, Node head, int[] listOfIntegersToAdd, int[] listOfIntegersToRemove)
    {
        int numAdds = listOfIntegersToAdd.length;
        int numRemoves = listOfIntegersToRemove.length;
        int numChanges = numAdds + numRemoves;

        ExecutorService executorService = Executors.newFixedThreadPool(8);
        List<Future> futures = new ArrayList<>();
        for (int i = 0; i < numAdds; i++) {
            futures.add(executorService.submit(new SkiplistRunnable(useLockBasedSkiplist, head, listOfIntegersToAdd[i], null)));
        }

        for (int i = 0; i < numRemoves; i++) {
            futures.add(executorService.submit(new SkiplistRunnable(useLockBasedSkiplist, head, null, listOfIntegersToRemove[i])));
        }

        executorService.shutdown();

        // Wait for the results to become available
        for (int i = 0; i < numChanges; i++) {
            try {
                futures.get(i).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        if (this.integerToAdd != null)
        {
            this.skiplist.add(this.integerToAdd);
        }
        else{
            this.skiplist.rm(this.integerToRemove);
        }
    }
}
