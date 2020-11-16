package skiplist_proj;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class Tests {

    private Skiplist skiplist;

    public Tests(Skiplist skiplist)
    {
        this.skiplist = skiplist;
    }

    @Parameterized.Parameters()
    public static Collection<Skiplist> data() {
        ArrayList<Skiplist> testParams = new ArrayList<>();

        // Run with both types of Skiplist
        testParams.add(new Lock());
        testParams.add(new LockFree());

        return testParams;
    }
}