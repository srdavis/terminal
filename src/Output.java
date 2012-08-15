/**
 * 	@author Stuart Davis (stuartrdavis@gmail.com)
 * 	Name: Output.java
 * 	@version 0.1
 * 	Created: 02/10/2010
 * 	Description: 
 * 	
 */

import java.io.*;
import java.util.concurrent.*;

class Output extends Filter {
	private int id;
	private String filename;
	private String file = "";
	public Output(BlockingQueue<Object> in, String filename, int id) {
		super(in, null);
		this.id = id;
		this.filename = filename;
	}
	protected Object transform(Object o) {
		if (filename == null) {
			System.out.println("ERROR: incorrect filename");
			this.done = true;
			tStatus.setDone(id);
		}
		if (o == null) {
			if (empty && tStatus.pDone(id)) {
				// if the previous thread is done
				// and the in queue is empty
				// try to create a file writer and write the output
				try {
					FileWriter fstream = new FileWriter(Directory.getcwd()+File.separator+filename);
					BufferedWriter out = new BufferedWriter(fstream);
					out.write(file);
					out.close();
				}
				catch (Exception e) {
					// if there is an exception, prints the message to the user
					System.err.println("Exception "+e.getMessage()+" Caught.");
				}
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
			if (file == null) {
				//if file is first string
				file = o.toString();
			}
			else {
				//else if file is not null
				//includes a newline symbol
				file = file + "\n"+o.toString();
			}
			
		}
		return o;
	}
	// wait method for waiting
	public static void wait (double n) {
		long t0,t1;
		t0 = System.currentTimeMillis();
		do {
			t1 = System.currentTimeMillis();
		}
		while (t1 - t0 < 1000 * n);
	}
}