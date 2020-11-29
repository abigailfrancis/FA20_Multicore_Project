package skiplist_proj;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeSkiplist implements Skiplist
{
	//static final int MAX_LEVEL = 10;
	
    final AtomicReference<Node> head = new AtomicReference<>();
    final AtomicReference<Node> tail = new AtomicReference<>();

    //FIXME
    public LockFreeSkiplist(Node head)
    {
        this.head.set(head);
        this.tail.set(head);
    }

    /**
     * @param value the value to be added to the Skiplist
     * @return True, if the integer was added to the Skiplist successfully
     */
    @Override
    public boolean add(Integer value)
    {
        int topLevel = getRandomLevel();
        int bottomLevel = 0;
     // Initialize empty preds and succs lists
        List<AtomicReference<Node>> preds = new ArrayList<>();
        List<AtomicReference<Node>> succs = new ArrayList<>();
        //for (int i = 0; i < MAX_HEIGHT; i++)
        for (int i = 0; i <= topLevel; i++)
        {
            preds.add(i, null);
            succs.add(i, null);
        }
        while(true) {
        	boolean found = findLF(value, preds, succs);
        	if(found) {
        		return false;
        	}else {
        		Node newNode = new Node(value, topLevel+1);
        		newNode.setMarked(false);
        		for(int level = bottomLevel; level<= topLevel; level++) {
        			Node succ = succs.get(level).get();
        			newNode.next[level].set(succ);
        			//newNode.next[level].get().setMarked(false); //not totally sure this is right
        		}
        		Node pred = preds.get(bottomLevel).get();
        		Node succ = succs.get(bottomLevel).get();
        		if(!pred.next[bottomLevel].compareAndSet(succ, newNode)) {
        				continue;
        		}
        		for(int level = bottomLevel+1; level<= topLevel; level++) {
        			while(true) {
        				pred = preds.get(level).get();
        				succ = succs.get(level).get();
        				if(pred.next[level].compareAndSet(succ, newNode)) {
        					break;
        					//findLF(value, preds, succs);
        				}
        			}
        			return true;
        		}
        		
        		
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
        List<AtomicReference<Node>> preds = new ArrayList<>();
        List<AtomicReference<Node>> succs = new ArrayList<>();
        Node succ;
        Node succ_new;
        for (int i = 0; i < MAX_HEIGHT; i++)
        {
            preds.add(i, null);
            succs.add(i, null);
        }
        while(true) {
        	boolean found = contains(value);
        	found = findLF(value, preds, succs);
        	if(!found) {
        		return false;
        	}else {
        		Node nodeToRemove = succs.get(bottomLevel).get();
        		for(int level = nodeToRemove.getTopLevel(); level>= bottomLevel+1; level--) {
        			boolean[] marked = {false};
        			succ = nodeToRemove.next[level].get();
        			succ_new = succ;
        			marked[0] = succ.isMarked();
        			succ_new.setMarked(true);
        			while(!marked[0]) {
        				nodeToRemove.next[level].compareAndSet(succ, succ_new);
        				succ = nodeToRemove.next[level].get();
            			succ_new = succ;
            			marked[0] = succ.isMarked();
            			succ_new.setMarked(true);
        			}
        		}
        		boolean [] marked = {false};
        		succ = nodeToRemove.next[bottomLevel].get();
    			succ_new = succ;
    			marked[0] = succ.isMarked();
    			succ_new.setMarked(true);
    			while(true) {
    				boolean iMarkedIt = nodeToRemove.next[bottomLevel].compareAndSet(succ, succ_new);
    				succ = nodeToRemove.next[bottomLevel].get();
        			succ_new = succ;
        			marked[0] = succ.isMarked();
        			succ_new.setMarked(true);
        			if(iMarkedIt) {
        				find(value, preds, succs);
        				return true;
        			}
        			else if(marked[0]) return false;
    			}
        	}
        }

    
    }

    private boolean findLF(Integer value, List<AtomicReference<Node>> preds, List<AtomicReference<Node>> succs)
    {
    	int bottomLevel = 0;
    	long key = value.hashCode();
    	boolean [] marked = {false};
    	boolean snip;
		Node succ_temp = null, curr_temp = null;
		AtomicReference<Node> pred = null, succ = null, curr = null;
    	retry:
    		while(true) {
    			pred = head;
    			for(int level = pred.get().getTopLevel(); level >= bottomLevel; level--) {
    				curr_temp = pred.get().next[level].get();
    				curr = pred.get().next[level]; 
    				while(true) {
    					succ_temp = curr_temp.next[level].get();
    					succ = curr_temp.next[level];
    					if (succ_temp != null) marked[0] = succ_temp.isMarked();
    					while(marked[0]) {
    						snip = pred.get().next[level].compareAndSet(curr_temp, succ_temp);
    						if(!snip) continue retry;
    						curr_temp = pred.get().next[level].get();
    						curr = pred.get().next[level];
    						succ_temp = curr_temp.next[level].get();
    						succ = curr_temp.next[level];
    						if(succ_temp != null) marked[0] = succ_temp.isMarked();
    					}
    					if(curr_temp.key< key) {
    						pred = curr; curr = succ;
    						curr_temp = succ_temp;
    					}else {
    						break;
    					}
    				}
    				preds.set(level, pred);
    				succs.set(level, succ);
    			}
    			return(curr_temp.key == key);
    		}
	}
    @Override
    public Integer find(Integer value, List<AtomicReference<Node>> preds, List<AtomicReference<Node>> succs)
    {
    	return 0;
	}
    
    public boolean contains(Integer value) {
    	int bottomLevel = 0;
    	int v = value.hashCode();
    	boolean [] marked = {false};
    	AtomicReference<Node> pred = head, succ = null, curr = null;
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
    }
    
	@Override
    public void display()
    {
        System.out.println("---------------------");
        for (int level = MAX_HEIGHT -1; level >= 0; level--)
        {
            System.out.print("Level " + level + ": ");

            Node node = this.head.get();

            while (node != null)
            {
                System.out.print(" -> " + node);
                node = node.next[level].get();
            }

            System.out.println();
        }
        System.out.println("---------------------");
        System.out.println();
    }
}