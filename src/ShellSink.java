/**
 * 	@author Stuart Davis (stuartrdavis@gmail.com)
 * 	Name: ShellSink.java
 * 	@version 0.1
 * 	Created: 02/10/2010
 * 	Description: 
 * 		This method prints the output
 * 		of the commands to the user.
 */

import java.util.concurrent.*;

class ShellSink extends Filter {
	private int id;
	public ShellSink(BlockingQueue<Object> in, int id) {
		super(in, null);
		this.id = id;
	}
	protected Object transform(Object o) {
		if (o == null) {
			// if the incoming object is null
			if (empty && tStatus.pDone(id)) {
				// if the previous thread is done
				// and the in queue is empty
				// these variables end the thread's loop
				// and establish that the thread is done
				this.done = true;
				tStatus.setDone(id);
				tStatus.reset();
				return null;
			}
			else {
				//waits 10 milliseconds to recheck
				wait(0.10);
			}
		}
		else {
			// otherwise print the next value taken from in
			System.out.println("\t"+o.toString());
		}
		return o;
	}
	//wait method for waiting
	public static void wait (double n) {
		long t0,t1;
		t0 = System.currentTimeMillis();
		do {
			t1 = System.currentTimeMillis();
		}
		while (t1 - t0 < 1000 * n);
	}
}