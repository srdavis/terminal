/**
 * 	@author Stuart Davis (stuartrdavis@gmail.com)
 * 	Name: ChangeDirectory.java
 * 	@version 0.1
 * 	Created: 02/10/2010
 * 	Description: 
 * 	
 */

import java.io.*;

class ChangeDirectory{
	private String target;
	public ChangeDirectory(String target){
		this.target=target;
		cwd();
	}
	protected void cwd(){
		if(target.equals("")){
			System.out.println("\t\tNo directory specified.");
		}
		else if(!new File(Directory.getcwd(), File.separator+target).isDirectory()){
			System.out.println("\t\tNo directory with that name.");
		}
		else if(target.equals("..")){
			Directory.chparent();
		}
		else{
			Directory.chdir(target);
		}
	}
}