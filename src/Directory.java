/**
 * 	@author Stuart Davis (stuartrdavis@gmail.com)
 * 	Name: Directory.java
 * 	@version 0.1
 * 	Inception: 02/10/2010
 * 	Description: 
 * 		A class to store and maintain the current
 * 		working directory for my REPL.
 */

import java.io.*;

class Directory {
	private static File cwd; // File to maintain the current working directory
	
	// method to initialize the working dir to the dir where the JVM is started
	public static void initialize() {
		cwd = new File(System.getProperty("user.dir"));
	}
	
	// method to change the cwd
	public static void chdir(String wd) {
		cwd = new File(cwd, File.separator+wd);
	}
	
	// method to change the cwd to the parent of the cwd
	public static void chparent() {
		cwd = new File(cwd.getParent());
	}
	
	// method to get what the cwd is
	public static String getcwd() {
		return cwd.getPath();
	}
	
	// method to print the cwd
	// (this is for testing and is not used by any threads)
	public static void printcwd() {
		System.out.println("cwd: " + cwd.getPath());
	}
	
	// method that returns a list of the files in the cwd
	public String[] listFiles() {
		return Directory.cwd.list();	// return this.cwd.list();
	}
}