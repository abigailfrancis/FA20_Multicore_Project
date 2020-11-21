package skiplist_proj;

import java.util.concurrent.atomic.AtomicReference;

public class LockFree implements Skiplist {

    public boolean add(Integer value) {
        return false;
    }

    public boolean rm(Integer value) {
        return false;
    }

    public Integer find(Integer value, AtomicReference<Node>[] preds, AtomicReference<Node>[] succs)

    {
        return null;
	}
}
