/**
 * 	@author Stuart Davis (stuartrdavis@gmail.com)
 * 	Name: tStatus.java
 * 	@version 0.1
 * 	Created: 02/10/2010
 * 	Description: 
 * 		This was my solution to a problem I encountered
 * 		where I realized that each thread needs to know
 * 		if the previous thread had completed. I solved 
 * 		this with an array of booleans, where each index
 * 		is the status of a thread. The indices coorespond
 * 		to the thread that was started in that order
 * 		(first thread is the 0 index, second thread is the
 * 		1 index, ...) 
 * 		This is to solve the problem I ran into where if I
 * 		ran cat on a file with many lines and it would only
 * 		match the final line, the subsequent commands would 
 * 		think cat had finished before it had outputted 
 * 		anything. Each thread now checks if the previous
 * 		is done with pDone(int id)
 */

@SuppressWarnings("unused")

class tStatus {
	private static boolean[] status; // status array
	private static int id; // thread id variable
	
	public static void init(int size) {
		status = new boolean[size];
		for (boolean i:status) {
			i = false; // sets each boolean to false
		}
	}
	
	// this method is run by the final commands ShellSink and Output
	public static void reset() {
		for (int b=0; b<status.length; b++) {
			status[b] = false; // resets each boolean to false
		}
		id = 0;
	}
	
	public static int getID() {
		return id;
	}
	
	public static boolean prev(int id) {
		return status[id - 1];
	}
	
	public static boolean pDone(int id) {
		return status[id - 1];
	}
	
	public static boolean curr() {
		return status[id];
	}
	
	public static void setDone() {
		status[id] = true;
		id++;
	}
	
	public static void setDone(int id) {
		status[id] = true;
		id++;
	}
}