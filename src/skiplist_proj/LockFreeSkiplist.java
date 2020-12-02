package skiplist_proj;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LockFreeSkiplist implements Skiplist
{
    Node head = null;

    public LockFreeSkiplist(Node head)
    {
        this.head = head;
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
        List<Node> preds = new ArrayList<>();
        List<Node> succs = new ArrayList<>();

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

		Node pred = null, succ = null, curr = null;
    	retry:
    		while(true)
			{
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

    				preds.set(level, pred);
    				succs.set(level, curr);
    			}
    			
    			return lfound_ret;
    		}
	}
    
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