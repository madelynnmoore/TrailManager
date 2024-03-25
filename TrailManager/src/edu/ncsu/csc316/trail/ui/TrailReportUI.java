package edu.ncsu.csc316.trail.ui;

import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.ncsu.csc316.trail.manager.ReportManager;

/**
 * UI for handling the TrailReport
 *
 * @author Maddie Moore
 *
 */
public class TrailReportUI {
	
	/** ReportManager field that is responsible for the manipulation of the program */
	private static ReportManager manager;
	
	/**
	 * Main method of the program
	 * @param args the arguments from the user
	 */
	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        boolean valid = false;
        while (!valid) {
	        System.out.println("Please enter a landmark file and trail file separate by a space (landmark.csv trail.csv) or type 'quit' to quit");
	    	input = scanner.nextLine();
	    	if (input.indexOf(' ') == -1) {
	    		System.out.println("File invalid (no space)");
	    		continue;
	    	}
	    	String pathToLandmarkFile = input.substring(0, input.indexOf(' '));
	    	String pathToTrailFile = input.substring(input.indexOf(' ') + 1);
	    	try {
				manager = new ReportManager(pathToLandmarkFile, pathToTrailFile); // access the manager variable through the instance
			} catch (Exception e) {
				System.out.println("File invalid");
				continue;
			}
	    	valid = true;
        }
        while (!input.equals("quit")) {
	        System.out.println("Pick an option (Enter the number associated):\n"
	        		+ "1. Get Distances Report\n"
	        		+ "2. Get Proposed First Aid Locations\n"
	        		+ "quit: Quit\n");
	        input = scanner.next();
	        if (input.equals("1")) {
	        	input = "";
	        	System.out.println("For what landmark ID would you like to locate distances?");
	        	input = scanner.next();
	        	System.out.println(manager.getDistancesReport(input));
	        }
	        else if (input.equals("2")) {
	        	input = "";
	        	System.out.println("How many intersecting trails?");
	        	input = scanner.next();
	        	System.out.println(manager.getProposedFirstAidLocations(Integer.parseInt(input)));
	        }
	        else if (input.equals("quit")) {
	        	break;
	        }
	        else {
	        	System.out.println("Please enter a valid option.");
	        }
        }
        System.out.println("Goodbye!");
    }
}
