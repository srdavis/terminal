/**
 * 	@author Stuart Davis (stuartrdavis@gmail.com)
 * 	Name: Concatenate.java
 * 	@version 0.1
 * 	Inception: 02/10/2010
 * 	Description: 
 * 	
 */

import java.util.*;
import java.io.*;
import java.util.concurrent.*;

class Concatenate extends Filter {
	private static Scanner[] input;
	private String[] filenames;
	private int current;
	private int id;
	public Concatenate (BlockingQueue<Object> in, BlockingQueue<Object> out, String[] names, int id) {
		super(in,out);
		this.id = id;
		this.filenames = names;
		Concatenate.input = new Scanner[names.length];
		this.current=0;
		for (int i=0; i<filenames.length; i++) {
			//creates a string for the file directory
			String f = Directory.getcwd() + File.separator + filenames[i];
			try {
				//creates a scanner that scans the file
				input[i] = new Scanner(new File(f));
			}
			catch (IOException e) {
				System.err.println("\n" + e.getMessage() + "\n");
				input[i] = null;
			}
		}
	}
	protected Object transform(Object o) {
		if (current == filenames.length) {
			this.done = true;
			tStatus.setDone(id);
		}
		else if (input[current] == null) {
			current++;
			return "\nSome file(s) not concatenated because of file not found error\n";
		}
		else if (!input[current].hasNextLine()) {
			current++;
		}
		else {
			return input[current].nextLine();
		}
		return null;
	}
}