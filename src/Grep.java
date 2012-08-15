/**
 * 	@author Stuart Davis (stuartrdavis@gmail.com)
 * 	Name: Grep.java
 * 	@version 0.1
 * 	Inception: 02/10/2010
 * 	Description: 
 * 	
 */

import java.util.concurrent.*;

class Grep extends Filter {
	private String searchString;
	private int id;
	public Grep(BlockingQueue<Object> in, BlockingQueue<Object> out, String searchString, int id) {
		super(in, out);
		this.id = id;
		this.searchString = searchString;
	}
	protected Object transform(Object o) {
		if (empty && tStatus.pDone(id)) {
				tStatus.setDone(id);
				this.done = true;
		}
		if (o == null) {  }
		else if (o.toString().indexOf(searchString) >= 0) {
			return o;
		}
		return null;
	}
}