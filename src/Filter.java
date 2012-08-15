/**
 * 	@author Stuart Davis (stuartrdavis@gmail.com)
 * 	Name: Filter.java
 * 	@version 0.1
 * 	Inception: 02/10/2010
 * 	Description: 
 * 	
 */

import java.util.concurrent.*;

public abstract class Filter implements Runnable {
	protected BlockingQueue<Object> in;
	protected BlockingQueue<Object> out;
	protected volatile boolean done;
	protected volatile boolean empty;
	
	public Filter(BlockingQueue<Object> in, BlockingQueue<Object> out) {
		this.in = in;
		this.out = out;
		this.done = false;
		empty = false;
	}
	
	public void run() {
		Object o = null;
		while (!this.done) {
			try {
				if (this.in == null) {
					o = transform("");
					if (o != null) {
						out.put(o);
					}
				}
				else if (this.out == null) {
					empty = isEmpty();
					if (empty == false) {
						o = in.take();
						o = transform(o);
					}
					else {
						o = transform(o);
					}
				}
				else {
					empty = isEmpty();
					if (!empty) {
						o = in.take();
						o = transform(o);
					}
					else {
						o = transform(o);
					}
					if (o == null) {
					}
					else {
						out.put(o);
					}
				}
				o = null;
				empty = false;
			}
			catch (InterruptedException e) {
				System.out.println("Interrupted.");
			}
		}
	}
	
	protected boolean isEmpty() {
		if (this.in == null) { return true; }
		else { return (this.in.peek() == null); }
	}
	
	protected abstract Object transform(Object o);
}