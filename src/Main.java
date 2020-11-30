import java.io.FileWriter;
import java.io.IOException;

import skiplist_proj.LockBasedSkiplist;
import skiplist_proj.TimingData.TimingData;

public class Main {

    public static void main(String[] args) {
    	//take timing data
    	RunLockFreeTests();
    	RunLockBasedTests();
    	
    }
    private static void RunLockFreeTests() {
    	long [] LF_Add_StartTimes_small = new long [1000];
    	long [] LF_Add_EndTimes_small = new long [1000];
    	long [] LF_Add_StartTimes_large = new long [1000];
    	long [] LF_Add_EndTimes_large = new long [1000];
    	
    	long [] LF_Rm_StartTimes_small = new long [1000];
    	long [] LF_Rm_EndTimes_small = new long [1000];
    	long [] LF_Rm_StartTimes_large = new long [1000];
    	long [] LF_Rm_EndTimes_large = new long [1000];
    	
    	
    	TimingData LockFreeTimingData = new TimingData();
    	TimingData LockBasedTimingData = new TimingData();
    	for(int i = 0; i < 10; i++) {
    		
    		//add few nodes
    		LF_Add_StartTimes_small[i] = System.nanoTime();
    		LockFreeTimingData.TimingTest_add(5, false);
    		LF_Add_EndTimes_small[i] = System.nanoTime();
    		
    		//print skiplist
    		LockFreeTimingData.printSkiplist(); //divide by 1000000 to get milliseconds.
    		
    		//remove few nodes
    		LF_Rm_StartTimes_small[i] = System.nanoTime();
    		LockFreeTimingData.TimingTest_rm(5, false);
    		LF_Rm_EndTimes_small[i] = System.nanoTime();
    		
    		LockFreeTimingData.printSkiplist(); //divide by 1000000 to get milliseconds.
    		//add lots of nodes
        	//write timing output

    		
        	//remove lots of nodes
    		//write timing output
    		LF_Add_StartTimes_large[i] = System.nanoTime();
    		LockFreeTimingData.TimingTest_add(100, false);
    		LF_Add_EndTimes_large[i] = System.nanoTime();
    		
    		LF_Rm_StartTimes_large[i] = System.nanoTime();
    		LockFreeTimingData.TimingTest_rm(100, false);
    		LF_Rm_EndTimes_large[i] = System.nanoTime();
    		
    	}

		try {
		write(LF_Add_StartTimes_small, "LF_Add_StartTimes_small.csv");
		write(LF_Add_StartTimes_large, "LF_Add_StartTimes_large.csv");
		write(LF_Add_EndTimes_small, "LF_Add_EndTimes_small.csv");
		write(LF_Add_EndTimes_large, "LF_Add_EndTimes_large.csv");
		}catch(IOException e){
		}
    }
    public static void RunLockBasedTests() {
    	
    }
    private static void write(long [] data, String fileName) throws IOException {
    	FileWriter writer;
		try {
			writer = new FileWriter(fileName);
			for (int j = 0; j < data.length; j++) {
	    	    writer.append(String.valueOf(data[j]));
	    	writer.append("\n");
	    	}
			writer.toString();    
		    writer.flush();
		    writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    }
}
