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
	
    final AtomicMarkableReference<Node> head = new AtomicMarkableReference<>(null, false);
    final AtomicMarkableReference<Node> tail = new AtomicMarkableReference<>(null, false);
    //private boolean [] foo;
    //FIXME
    public LockFreeSkiplist(Node head)
    {
        this.head.set(head, head.isMarked());
        this.tail.set(head, head.isMarked());
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
       // Node initNode = new Node(null, topLevel + 1);
        List<AtomicMarkableReference<Node>> preds = new ArrayList<>();
        List<AtomicMarkableReference<Node>> succs = new ArrayList<>();
        //for (int i = 0; i < MAX_HEIGHT; i++)
        for (int i = 0; i <= MAX_HEIGHT; i++)
        {
            preds.add(i, null);
            succs.add(i, null);
        }
        while(true) {
        	boolean found = findLF(value, preds, succs);
        	if(found) {
        		return false;
        	}else {
        		Node newNode = new Node(value, topLevel);
        		newNode.setMarked(false);
        		for(int level = bottomLevel; level<= topLevel; level++) {
        			Node succ = succs.get(level).getReference();
        			newNode.next[level].set(succ, false);
        			//newNode.next[level].get().setMarked(false); //not totally sure this is right
        		}
        		AtomicMarkableReference<Node> pred = preds.get(bottomLevel);
        		Node succ = succs.get(bottomLevel).getReference();
        		if(!pred.getReference().next[bottomLevel].compareAndSet(succ, newNode, false, false)) {
        				continue;
        		}
        		for(int level = bottomLevel+1; level<= topLevel; level++) {
        			while(true) {
        				pred = preds.get(level);
        				succ = succs.get(level).getReference();
        				if(pred.getReference().next[level].compareAndSet(succ, newNode, false, false)) {
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
        List<AtomicMarkableReference<Node>> preds = new ArrayList<>();
        List<AtomicMarkableReference<Node>> succs = new ArrayList<>();
        Node succ;
       // Node succ_new;

        while(true) {
        	
        	boolean found = findLF(value, preds, succs);
            preds.clear();
            succs.clear();
            for (int i = 0; i < succs.get(bottomLevel).getReference().getTopLevel(); i++)
            {
                preds.add(i, null);
                succs.add(i, null);
            }
        	if(!found) {
        		return false;
        	}else {
        		Node nodeToRemove = succs.get(bottomLevel).getReference();
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
    				succ = nodeToRemove.next[bottomLevel].get(marked);
        			if(iMarkedIt) {
        				findLF(value, preds, succs);
        				return true;
        			}
        			else if(marked[0]) return false;
    			}
        	}
        }

    
    }

    private boolean findLF(Integer value, List<AtomicMarkableReference<Node>> preds, List<AtomicMarkableReference<Node>> succs)
    {
    	int bottomLevel = 0;
    	long key = value.hashCode();
    	boolean [] marked = {false};
    	boolean snip;
		Node succ_temp = null, curr_temp = null;
		AtomicMarkableReference<Node> pred = null, succ = null, curr = null;
    	retry:
    		while(true) {
    			pred = head;
    			for(int level = MAX_HEIGHT-1; level >= bottomLevel; level--) {
    				curr_temp = pred.getReference().next[level].getReference();
    				curr = pred.getReference().next[level]; 
    				while(true) {
    					succ_temp = curr.getReference().next[level].get(marked);
    					succ = curr_temp.next[level];
    					while(marked[0]) {
    						snip = pred.getReference().next[level].compareAndSet(curr_temp, succ_temp, false, false);
    						if(!snip) continue retry;
    						curr_temp = pred.getReference().next[level].getReference();
    						curr = pred.getReference().next[level];
    						succ_temp = curr_temp.next[level].get(marked);
    						succ = curr_temp.next[level];
    					}
    					if(curr_temp.key< key) {
    						pred = curr; curr = succ;
    						curr_temp = succ_temp;
    					}else {
    						break;
    					}
    				}
    				preds.set(level, pred);
    				succs.set(level, curr);
    			}
    			return(curr_temp.key == key);
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
        for (int level = MAX_HEIGHT -1; level >= 0; level--)
        {
            System.out.print("Level " + level + ": ");

            Node node = this.head.getReference();

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

	@Override
	public Integer find(Integer value, List<AtomicReference<Node>> preds, List<AtomicReference<Node>> succs) {
		// TODO Auto-generated method stub
		return null;
	}
}