package skiplist_proj.TimingData;
import skiplist_proj.LockBasedSkiplist;
import skiplist_proj.LockFreeSkiplist;
import skiplist_proj.Node;
import skiplist_proj.Skiplist;
import skiplist_proj.tests.TestData;
import skiplist_proj.tests.UnitTests.SkiplistRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicMarkableReference;

 
import static skiplist_proj.Skiplist.MAX_HEIGHT;

/**
 * A helper class for creating test skiplist data
 */
public class TimingData
{
	private static Node head;
    private Skiplist skiplist;
    private static List<AtomicMarkableReference<Node>> preds = new ArrayList<>();
    private static List<AtomicMarkableReference<Node>> succs = new ArrayList<>();
    
    public TimingData()
    {
    	// Initialize the head node
        head = new Node(Integer.MIN_VALUE, MAX_HEIGHT);
        
    }
    
public boolean TimingTest_add (Integer numOfNodes, boolean useLockBasedSkiplist) {
	    TestData.setupEmptySkiplist(head);
	    int[] listOfIntegersToAdd = new int[numOfNodes];
	    TestData.createListOfConsecutiveIntegers(listOfIntegersToAdd);
	
	    int[] listOfIntegersToRemove = {};
	    if(useLockBasedSkiplist) {
	    	this.skiplist = new LockBasedSkiplist(head);
	    }else {
	    	this.skiplist = new LockFreeSkiplist(head);
	    }
	   
	    // Act
	    SkiplistRunnable.runTest(useLockBasedSkiplist, head, listOfIntegersToAdd, listOfIntegersToRemove);
	    return true;
	}
public boolean TimingTest_rm (Integer numOfNodes, boolean useLockBasedSkiplist) {
	
    TestData.setupEmptySkiplist(head);
    int[] listOfIntegersToAdd = {};

    int[] listOfIntegersToRemove =  new int[numOfNodes];
    TestData.createListOfConsecutiveIntegers(listOfIntegersToRemove);    

    // Act
    SkiplistRunnable.runTest(useLockBasedSkiplist, head, listOfIntegersToAdd, listOfIntegersToRemove);
    return true;
}
public void printSkiplist()
{
    this.skiplist.display();
}
    
}

