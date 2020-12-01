import java.io.FileWriter;
import java.io.IOException;

import skiplist_proj.LockBasedSkiplist;
import skiplist_proj.TimingData.TimingData;

public class Main {

	public static void main(String[] args) {
		// take timing data
		//RunLockFreeTests();
		RunLockBasedTests();

	}

	private static void RunLockFreeTests() {
		long[] LF_Add_StartTimes_small = new long[100];
		long[] LF_Add_EndTimes_small = new long[100];
		long[] LF_Add_StartTimes_large = new long[100];
		long[] LF_Add_EndTimes_large = new long[100];

		long[] LF_Rm_StartTimes_small = new long[100];
		long[] LF_Rm_EndTimes_small = new long[100];
		long[] LF_Rm_StartTimes_large = new long[100];
		long[] LF_Rm_EndTimes_large = new long[100];

		// TimingData LockFreeTimingData = new TimingData();
		for (int i = 0; i < 60; i++) {
			System.out.println("Run " + i);
			TimingData LockFreeTimingData = new TimingData();
			// add few nodes
			boolean successful = false;
			/*
			 * while(!successful) { LF_Add_StartTimes_small[i] = System.nanoTime();
			 * LockFreeTimingData.TimingTest_add(50, false); LF_Add_EndTimes_small[i] =
			 * System.nanoTime();
			 * 
			 * successful = LockFreeTimingData.isSize(50); System.out.println(successful);
			 * // print skiplist // LockFreeTimingData.printSkiplist(); //divide by 1000000
			 * to get milliseconds.
			 * 
			 * // remove few nodes LF_Rm_StartTimes_small[i] = System.nanoTime();
			 * LockFreeTimingData.TimingTest_rm(50, false); LF_Rm_EndTimes_small[i] =
			 * System.nanoTime(); }
			 */
			// LockFreeTimingData.printSkiplist(); //divide by 1000000 to get milliseconds.
			// add lots of nodes
			// write timing output

			// remove lots of nodes
			// write timing output
			successful = false;
			while(!successful) {
				LF_Add_StartTimes_large[i] = System.nanoTime();
				LockFreeTimingData.TimingTest_add(50, false);
				LF_Add_EndTimes_large[i] = System.nanoTime();
				
				successful = LockFreeTimingData.isSize(50);
				System.out.println(successful);
				LF_Rm_StartTimes_large[i] = System.nanoTime();
				LockFreeTimingData.TimingTest_rm(50, false);
				LF_Rm_EndTimes_large[i] = System.nanoTime();
			}

		}
		try {
			String[] columnNames = { "LF_Add_StartTimes_small", 
					"LF_Add_EndTimes_small", 
					"LF_Add_StartTimes_large",
					"LF_Add_EndTimes_large", 
					"LF_Rm_StartTimes_small",
					"LF_Rm_EndTimes_small", 
					"LF_Rm_StartTimes_large",
					"LF_Rm_EndTimes_large" };
			write(columnNames, LF_Add_StartTimes_small, LF_Add_EndTimes_small, LF_Add_StartTimes_large,
					LF_Add_EndTimes_large, LF_Rm_StartTimes_small, LF_Rm_EndTimes_small, LF_Rm_StartTimes_large,
					LF_Rm_EndTimes_large, "LF_Data.csv");
		} catch (IOException e) {
		}

	}

	public static void RunLockBasedTests() {
		long[] LB_Add_StartTimes_small = new long[100];
		long[] LB_Add_EndTimes_small = new long[100];
		long[] LB_Add_StartTimes_large = new long[100];
		long[] LB_Add_EndTimes_large = new long[100];

		long[] LB_Rm_StartTimes_small = new long[100];
		long[] LB_Rm_EndTimes_small = new long[100];
		long[] LB_Rm_StartTimes_large = new long[100];
		long[] LB_Rm_EndTimes_large = new long[100];

		// TimingData LockFreeTimingData = new TimingData();

		for (int i = 0; i < 60; i++) {
			TimingData LockBasedTimingData = new TimingData();
			System.out.println("Run " + i);
			// add few nodes
			boolean successful = false;
			/*
			 * while(false) { LB_Add_StartTimes_small[i] = System.nanoTime();
			 * LockBasedTimingData.TimingTest_add(50, false); LB_Add_EndTimes_small[i] =
			 * System.nanoTime();
			 * 
			 * // print skiplist successful = LockBasedTimingData.isSize(50);
			 * System.out.println(successful);
			 * 
			 * // remove few nodes
			 * 
			 * LB_Rm_StartTimes_small[i] = System.nanoTime();
			 * LockBasedTimingData.TimingTest_rm(50, false); LB_Rm_EndTimes_small[i] =
			 * System.nanoTime(); }
			 */
			// add lots of nodes
			// write timing output

			// remove lots of nodes
			// write timing output
			successful = false;
			while(!successful) {
				LB_Add_StartTimes_large[i] = System.nanoTime();
				LockBasedTimingData.TimingTest_add(50, false);
				LB_Add_EndTimes_large[i] = System.nanoTime();
	
				successful = LockBasedTimingData.isSize(50);
				System.out.println(successful);
				 
				LB_Rm_StartTimes_large[i] = System.nanoTime();
				LockBasedTimingData.TimingTest_rm(50, false);
				LB_Rm_EndTimes_large[i] = System.nanoTime();
				System.out.println("clear " + LockBasedTimingData.isSize(0));
			}

		}
		try {
			String[] columnNames = { "LB_Add_StartTimes_small", 
					"LB_Add_EndTimes_small", 
					"LB_Add_StartTimes_large",
					"LB_Add_EndTimes_large", 
					"LB_Rm_StartTimes_small",
					"LB_Rm_EndTimes_small", 
					"LB_Rm_StartTimes_large",
					"LB_Rm_EndTimes_large" };
			write(columnNames, LB_Add_StartTimes_small, LB_Add_EndTimes_small, LB_Add_StartTimes_large,
					LB_Add_EndTimes_large, LB_Rm_StartTimes_small, LB_Rm_EndTimes_small, LB_Rm_StartTimes_large,
					LB_Rm_EndTimes_large, "LB_Data.csv");
		} catch (IOException e) {
		}

	}

	private static void print(long[] data, String label) {
		System.out.println(label);
		for (int i = 0; i < data.length; i++) {
			System.out.println(data[i]);
		}
	}

	private static void write(String[] names, long[] data, long[] data1, long[] data2, long[] data3, long[] data4,
			long[] data5, long[] data6, long[] data7, String fileName) throws IOException {
		FileWriter writer;
		try {
			writer = new FileWriter(fileName);
			for(int i = 0; i < names.length; i++) {
				writer.append(names[i]);
				writer.append(",");
			}
			writer.append("\n");
			for (int j = 0; j < data.length; j++) {
				writer.append(String.valueOf(data[j]));
				writer.append(",");
				writer.append(String.valueOf(data1[j]));
				writer.append(",");
				writer.append(String.valueOf(data2[j]));
				writer.append(",");
				writer.append(String.valueOf(data3[j]));
				writer.append(",");
				writer.append(String.valueOf(data4[j]));
				writer.append(",");
				writer.append(String.valueOf(data5[j]));
				writer.append(",");
				writer.append(String.valueOf(data6[j]));
				writer.append(",");
				writer.append(String.valueOf(data7[j]));
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
