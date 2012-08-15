/**
 * 	@author Stuart Davis (stuartrdavis@gmail.com)
 * 	Name: LineCount.java
 * 	@version 0.1
 * 	Created: 02/10/2010
 * 	Description: 
 * 	
 */

import java.util.concurrent.*;

class LineCount extends Filter {
	private int count;
	private int id;
	private boolean rc;
	public LineCount(BlockingQueue<Object> in, BlockingQueue<Object> out, int id) {
		super(in, out);
		this.id = id;
		this.count = 0;
		rc = false;
	}
	protected Object transform(Object o) {
		if (o != null) {
			count++;
		}
		if (empty && tStatus.pDone(id)) {
			if(!rc) {
				rc =! rc;
				return (Object) new Integer(count);
			}
			else {
				tStatus.setDone(id);
				this.done = true;
			}
		}
		return null;
	}
}