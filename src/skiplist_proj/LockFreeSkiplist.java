package skiplist_proj;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
//import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeSkiplist implements Skiplist
{
	//static final int MAX_LEVEL = 10;
	
    Node head = null;
    //final AtomicMarkableReference<Node> tail = new AtomicMarkableReference<>(null, false);
    //private boolean [] foo;
    //Node newTail = new Node(Integer.MAX_VALUE, MAX_HEIGHT);
    //FIXME
    public LockFreeSkiplist(Node head)
    {
		/*
		 * final Node head_new = new Node(head.getItem(),head.getTopLevel());
		 * this.head.set(head_new, head.isMarked()); for(int i = 0; i <
		 * head.next.length; i++) { this.head.getReference().next[i] = new
		 * AtomicMarkableReference<Node>(null, false);
		 * this.head.getReference().next[i].set(head.next[i].getReference(),
		 * head.next[i].isMarked()); }
		 */
        this.head = head;
        //this.tail.set(head.next[bottomLevel], head.isMarked());
    }

    /**
     * @param value the value to be added to the Skiplist
     * @return True, if the integer was added to the Skiplist successfully
     */
    @Override
    public boolean add(Integer value)
    {
        int topLevel = getRandomLevel();
        //topLevel = 3;
        int bottomLevel = 0;
     // Initialize empty preds and succs lists
       // Node initNode = new Node(null, topLevel + 1);
        List<Node> preds = new ArrayList<>();
        List<Node> succs = new ArrayList<>();
        //for (int i = 0; i < MAX_HEIGHT; i++)
        for (int i = 0; i <= MAX_HEIGHT; i++)
        {
            preds.add(i, null);
            succs.add(i, null);
        }
        while(true) {
        	int found = find(value, preds, succs);
        	if(found != -1) {
        		return false;
        	}else {
        		Node newNode = new Node(value, topLevel);
        		for(int level = bottomLevel; level<= topLevel; level++) {
        			Node succ = succs.get(level);
        			newNode.next[level].set(succ, false);
        		}
        		//AtomicMarkableReference<Node> pred = preds.get(bottomLevel);
        		Node pred = preds.get(bottomLevel);
        		Node succ = succs.get(bottomLevel);

        		if(!pred.next[bottomLevel].compareAndSet(succ, newNode, false, false)) {
        				continue;
        		}
        		for(int level = bottomLevel+1; level<= topLevel; level++) {
        			while(true) {
        				pred = preds.get(level);
        				succ = succs.get(level);
        				if(pred.next[level].compareAndSet(succ, newNode, false, false))
        					break;
        				find(value, preds, succs);	
        			}
        		}
        		return true;
        	}
        }
    }
    private Integer getRandomLevel() {
    	Random random = new Random();
    	return random.nextInt(MAX_HEIGHT);
    }
    /**
     * @param value the value to be removed from the Skiplist
     * @return True, if the Integer was removed successfully
     */
    @Override
    public boolean rm(Integer value)
    {
    	int bottomLevel = 0;
        // Initialize empty preds and succs lists
        List<Node> preds = new ArrayList<>();
        List<Node> succs = new ArrayList<>();
        Node succ;
       // Node succ_new;
        for (int i = 0; i <= MAX_HEIGHT; i++)
        {
            preds.add(i, null);
            succs.add(i, null);
        }
        while(true) {
        	
        	int found = find(value, preds, succs);
        	if(!(found != -1)) {
        		return false;
        	}else {
        		Node nodeToRemove = succs.get(bottomLevel);
        		for(int level = nodeToRemove.getTopLevel(); level>= bottomLevel+1; level--) {
        			boolean[] marked = {false};
        			succ = nodeToRemove.next[level].get(marked);
        			while(!marked[0]) {
        				nodeToRemove.next[level].compareAndSet(succ, succ, false, true);
        				succ = nodeToRemove.next[level].get(marked);
        			}
        		}
        		boolean [] marked = {false};
        		succ = nodeToRemove.next[bottomLevel].get(marked);
    			
    			while(true) {
    				boolean iMarkedIt = nodeToRemove.next[bottomLevel].compareAndSet(succ, succ, false, true);
    				succ = succs.get(bottomLevel).next[bottomLevel].get(marked);
        			if(iMarkedIt) {
        				find(value, preds, succs);
        				return true;
        			}
        			else if(marked[0]) return false;
    			}
        	}
        }
    }
    @Override
    public Integer find(Integer value, List<Node> preds, List<Node> succs)
    {
     	boolean lfound = true;
    	int lfound_ret = -1;
    	int bottomLevel = 0;
    	long key = value.hashCode();
    	boolean [] marked = {false};
    	boolean snip;
		//Node succ_temp = null, curr_temp = null;
		Node pred = null, succ = null, curr = null;
    	retry:
    		while(true) {
    			pred = head;
    			for(int level = MAX_HEIGHT; level >= bottomLevel; level--) {
    				curr = pred.next[level].getReference();
    				while(true) {
    					succ = curr.next[level].get(marked);
    					while(marked[0]) {
    						snip = pred.next[level].compareAndSet(curr, succ, false, false);
    						if(!snip) continue retry;
    						curr = pred.next[level].getReference();
    						succ = curr.next[level].get(marked);
    					}
    					if(curr.key< key) {
    						pred = curr; curr = succ;
    					}else {
    						if(curr.key == key && lfound) {
    							lfound = false;
    							lfound_ret = level;
    						}
    						break;
    					}
    				}
    				//preds.get(level).set(pred, false);
    				//succs.get(level).set(curr, false);
    				preds.set(level, pred);
    				succs.set(level, curr);
    			}
    			
    			return lfound_ret;
    		}
	}
 //   @Override
  /*  public boolean contains(Integer value) {
    	int bottomLevel = 0;
    	int v = value.hashCode();
    	boolean [] marked = {false};
    	AtomicMarkableReference<Node> pred = head, succ = null, curr = null;
    	for(int level = pred.get().getTopLevel(); level >= bottomLevel; level--) {
    		curr = pred.get().next[level];
    		while(true) {
    			succ = curr.get().next[level];
    			marked[0] = succ.get().isMarked();
    			while(marked[0]) {
    				curr = pred.get().next[level];
    				succ = curr.get().next[level];    				
    			}
    			if(curr.get().key < v) {
    				pred = curr;
    				curr = succ;
    			}else {
    				break;
    			}
    		}
    	}
    	return (curr.get().key == v);
    }*/
    
	@Override
    public void display()
    {
        System.out.println("---------------------");
        for (int level = MAX_HEIGHT; level >= 0; level--)
        {
            System.out.print("Level " + level + ": ");

            Node node = this.head;

            while (node != null)
            {
                System.out.print(" -> " + node);
                node = node.next[level].getReference();
            }

            System.out.println();
        }
        System.out.println("---------------------");
        System.out.println();
    }
}