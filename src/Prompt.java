/**
 * 	@author Stuart Davis (stuartrdavis@gmail.com)
 * 	Name: Prompt.java
 * 	@version 0.1
 * 	Created: 02/10/2010
 * 	Description: 
 * 		My implementation of a multi-threaded REPL.
 * 		In order to see what each class is for and
 * 		the class/instance variables, please read
 * 		the comments in that particular class  
 * 		For my implementation, I had some difficulty
 * 		choosing the ideal data structures.
 */

import java.util.*;
import java.util.concurrent.*;

public class Prompt {
	private static final int ABQ_SIZE = 10; //Global constant for queue size
	
	public static void main(String[] args) throws InterruptedException {
		// inbound pipe for threads
		BlockingQueue<Object> in = new ArrayBlockingQueue<Object>(ABQ_SIZE);
		// outbound pipe for threads
		BlockingQueue<Object> out = new ArrayBlockingQueue<Object>(ABQ_SIZE);
		// the threadQueue is a list of all the instantiated threads during one loop
		Queue<Thread> threadQueue = new LinkedList<Thread>();
		
		Directory.initialize(); // initialize the cwd to the directory where the JVM is started (only run once)
		
		boolean quit = false; // boolean for the REPL's done condition. Once REPL recognizes quit, this becomes true
		boolean toFile = false; // if the user uses " > filename" to output to file, this becomes true
		String filename = null; // life previous variable, this is to store the filename if outputting to a file
		int id = 0; // thread id, used to identify thread's location in the status array
		
		Scanner scanner = new Scanner(System.in); // Scanner object to scan in the user input
		String[] input = null; // String array to store input after it is split
		
		System.out.println("\n----- Start of REPL -----");
		while (!quit) {
			id = tStatus.getID(); // set ID to the ID stored in the tStatus class
			
			System.out.print("\n >  ");
			
			String cmds = scanner.nextLine();
			// the following block identifies if the output should be placed in a file
			// if it should be, " > filename" is removed by splitting at " > "
			if (cmds.lastIndexOf(" > ") != -1) {
				input = cmds.split("\\s+\076\\s+");
				filename = input[1];
				cmds = input[0];
				toFile = true;
			}
			
			input = cmds.split("\\s+\\|\\s+"); //this splits the command string at each " | "
			
			// the following for loop identifies each command in the String array
			for (int i=0; i<input.length; i++) {
				// sets in to out, for the first command or only one command this doesn't matter
				// but for more then one, this is the creation of the pipe. One command's out
				// queue becomes the next command's in queue
				in = out;
				out = new ArrayBlockingQueue<Object>(ABQ_SIZE);
				
				// identifies if the command is cat
				if (input[i].startsWith("cat")) {
					if (i != 0) {
						// if cat is not first command
						System.out.println("ERROR: cat does not accept piped input.");
						threadQueue.clear(); //clears any previous commands input to the threadQueue
						break; //breaks the loop
						// since the previous commands are in each command identifying block,
						// the comments are not repeated each time
					}
					else {
						// since cat takes an input, the rest of the cat string is the filenames
						String[] s = input[i].replaceFirst("cat\\s+","").split("\\s+");
						// creates cat object for the thread
						// cat only has an out queue, the string of filenames and its id (will be 0)
						Concatenate cmd = new Concatenate(null,out,s,id);
						//creates the thread
						threadQueue.add(new Thread(cmd));
						// increments thread id for next thread
						id++;
						// since each command's thread is created in the same manor, the comments
						// for this are not repeated except where it is different
					}
				}
				
				// identifies if the command is grep
				else if (input[i].startsWith("grep")) {
					String[] arg = input[i].split("\\s+");
					if (i == 0) {
						// if grep is first index, grep is first command and will not get a piped input
						System.out.println("ERROR: grep needs a piped input.");
						threadQueue.clear();
						break;
					}
					else if (arg.length != 2) {
						// grep should only get 1 search string, a longer length means too many args
						System.out.println("\t\tWrong number of search strings provided.");
						System.out.println("\t\tPlease provide only one.");
						threadQueue.clear();
						break;
					}
					else {
						// grep takes an in queue, an out queue, the search string, and its thread id
						Grep cmd = new Grep(in,out,arg[1],id);
						threadQueue.add(new Thread(cmd));
						id++;
					}
				}
				
				// identifies if the command is lc
				else if (input[i].equalsIgnoreCase("lc")) {
					if (i == 0) {
						// same as grep, if lc is first index in array of commands, it is an error
						System.out.println("ERROR: lc needs a piped input.");
						threadQueue.clear();
						break;
					}
					else {
						// lc takes an in and out queue and its thread id
						LineCount cmd = new LineCount(in,out,id);
						threadQueue.add(new Thread(cmd));
						id++;
					}
				}
				
				// identifies if the command is pwd
				else if (input[i].equalsIgnoreCase("pwd")) {
					if (i != 0) {
						// if pwd is in a string of commands, it is an error since pwd does not accept piped input
						System.out.println("ERROR: pwd does not accept piped input.");
						threadQueue.clear();
						break;
					}
					else {
						PrintWorkingDirectory cmd = new PrintWorkingDirectory(null,out,id);
						threadQueue.add(new Thread(cmd));
						id++;
					}
				}
				
				// identifies if the command is ls
				else if (input[i].equalsIgnoreCase("ls")) {
					if (i != 0) {
						// ls has the same error as pwd
						System.out.println("ERROR: ls does not accept piped input.");
						threadQueue.clear();
						break;
					}
					else {
						List cmd = new List(null,out,id);
						threadQueue.add(new Thread(cmd));
						id++;
					}
				}
				
				// identifies if the command is cd
				else if (input[i].startsWith("cd")) {
					// cd does not need to create its own thread
					// ChangeDirectory cd = new ChangeDirectory(input[i].replaceFirst("cd\\s+",""));
					new ChangeDirectory(input[i].replaceFirst("cd\\s+",""));
				}
				
				// identifies if the command is quit
				else if (input[i].equalsIgnoreCase("quit")) {
					quit = true;
					threadQueue.clear();
					break;
				}
				
				// identifies if the command is --list
				else if (input[i].equals("--list")) {
					String[] c = {"cat filename","grep searchString","lc","pwd","ls","cd directory","quit","--list"};
					System.out.println("\t\tCommands:");
					for (String s:c) { System.out.println("\t\t\t"+s); }
					threadQueue.clear();
				}
				
				// identifies if nothing was input
				else if (input[i].equals("")) {
					threadQueue.clear();
					break;
				}
				
				// otherwise, the command must be unrecognized
				else {
					System.out.println("\t\tAn unrecognized command was entered.");
					System.out.println("\t\tUse --list for command list.");
					threadQueue.clear();
					break;
				}
			}
			
			// this initializes the thread status array to the the number of threads +1
			// the size is one larger to leave space for ShellSink/Output
			// look at tStatus to see what this thread status array is
			tStatus.init(id+1); 
			
			if (threadQueue.peek() != null) { //will not be null if a thread was created
				if (toFile) { //if toFile is true, output to file
					threadQueue.add(new Thread(new Output(out, filename, id)));
				}
				else { //otherwise print with ShellSink
					threadQueue.add(new Thread(new ShellSink(out, id)));
				}
				// start all threads in queue
				// start all threads here so they all run together
				while (threadQueue.peek() != null) {
					Thread thread = threadQueue.remove(); //remove the next thread in list
					thread.start(); //each thread gets started
					if (threadQueue.peek() == null) {
						//if all threads have been pulled from list, wait for final thread to finish
						thread.join(); // wait for final thread to finish (either ShellSink or Output)
					}
				}
			}
			//to prepare for next loop, clear the threadQueue and reset variables
			threadQueue.clear();
			filename = null;
			toFile = false;
		}
		System.out.println("\n----- End of REPL -----");
		scanner.close();
	}
}