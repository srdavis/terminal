/**
 * 	@author Stuart Davis (stuartrdavis@gmail.com)
 * 	Name: List.java
 * 	@version 0.1
 * 	Created: 02/10/2010
 * 	Description: 
 * 	
 */

import java.io.*;
import java.util.concurrent.*;

class List extends Filter {
	// private Directory cwd;
	private String[] children;
	private int i = 0;
	private int id;
	public List(BlockingQueue<Object> in, BlockingQueue<Object> out, int id) {
		super(in, out);
		// cwd = new Directory();
		File dir = new File(Directory.getcwd());
		this.children = dir.list();
		this.id = id;
	}
	protected Object transform(Object o) {
		if (i == -1){
			this.done = true;
			tStatus.setDone(id);
			return null;
		}
		if (children == null) {
			i =- 1;
			return "No files in current directory.";
		}
		else {
			o = children[i];
			i++;
			if (i == children.length) {
				i =- 1;
			}
			return o;
		}
	}
}