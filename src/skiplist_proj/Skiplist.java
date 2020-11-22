package skiplist_proj;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Interface for the Skiplist implementations
 */
public interface Skiplist {
    /**
     * Adds a new element to the Skiplist
     * @param value the value to be added to the Skiplist
     * @return a boolean indicating whether the operation was successful
     */
    boolean add(Integer value);

    /**
     * Removes a value from the Skiplist
     * @param value the value to be removed from the skiplist
     * @return a boolean indicating whether the operation was successful
     */
    boolean rm(Integer value);

    /**
     * Searches for a specific value in the Skiplist
     * @param value the value to search for in the Skiplist
     * @param preds the list of predecessors for the node we are searching for
     * @param succs the list of successors for the node we are searching for
     * @return The level where the value was found, otherwise -1
     */
    Integer find(Integer value, List<AtomicReference<Node>> preds, List<AtomicReference<Node>> succs);
}
