/**
 * 	@author Stuart Davis (stuartrdavis@gmail.com)
 * 	Name: PrintWorkingDirectory.java
 * 	@version 0.1
 * 	Created: 02/10/2010
 * 	Description: 
 * 	
 */

import java.util.concurrent.*;

class PrintWorkingDirectory extends Filter {
	private int id;
	private boolean r = false;
	public PrintWorkingDirectory(BlockingQueue<Object> in, BlockingQueue<Object> out, int id) {
		super(in, out);
		this.id = id;
	}
	protected Object transform(Object o) {
		if (!r) {
			r =! r;
			return Directory.getcwd();
		}
		else {
			this.done = true;
			tStatus.setDone(id);
			return null;
		}
	}
}