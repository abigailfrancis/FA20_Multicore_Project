package skiplist_proj;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class LockFree implements Skiplist {

    public boolean add(Integer value) {
        return false;
    }

    public boolean rm(Integer value) {
        return false;
    }

    public Integer find(Integer value, List<AtomicReference<Node>> preds, List<AtomicReference<Node>> succs)
    {
        return null;
	}
}
