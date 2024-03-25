package edu.ncsu.csc316.trail.manager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.jupiter.api.Test;

/**
 * Class that is responsible for testing the ReportManager classs
 *
 * @author Maddie Moore
 *
 */
class ReportManagerTest {
	
	/** Manager option used to test the class */
	private ReportManager manager;
	
	/** String that holds the path to the landmark file */
	private String pathToLandmarkFile = "input/landmarks_sample.csv";
	
	/** String that holds the path to the trail file */
	private String pathToTrailFile = "input/trails_sample.csv";
	
	/**
	 * Method used to set up the manager before each test
	 */
	@Before
	public void setUp() {
		try {
        	manager = new ReportManager(pathToLandmarkFile, pathToTrailFile);
        } catch (Exception e) {
        	fail("Unexpected exception thrown: " + e.getMessage());
        }
	}
	
	/**
	 * Tests using the ReportManager constructor incorrectly
	 */
	@Test
	public void testReportManager() {
		try {
			manager = new ReportManager("wrong", pathToTrailFile);
		} catch (Exception e) {
			assertTrue(e instanceof FileNotFoundException);
		}
		try {
			manager = new ReportManager(pathToLandmarkFile, "wrong");
		} catch (Exception e) {
			assertTrue(e instanceof FileNotFoundException);
		}
	}
	
	/** 
	  * Tests the getDisancesReport method with incorrect and correct uses
	  */
	@Test
	public void testGetDistancesReport() {
		setUp();
		assertEquals("The provided landmark ID (N/A) is invalid for the park.", manager.getDistancesReport("N/A"));
		assertEquals("Landmarks Reachable from Entrance Fountain (L02) {\n"
				+ "   3013 feet to Park Entrance (L01)\n"
				+ "   3613 feet to Hidden Gardens (L10)\n"
				+ "   4059 feet to Waste Station 1 (L03)\n"
				+ "   4192 feet to Entrance Restrooms (L04)\n"
				+ "   6503 feet (1.23 miles) to Waste Station 2 (L09)\n"
				+ "   8263 feet (1.56 miles) to Overlook 1 (L05)\n"
				+ "   9302 feet (1.76 miles) to Rock Formation 1 (L06)\n"
				+ "   12214 feet (2.31 miles) to Overlook 2 (L07)\n"
				+ "   14105 feet (2.67 miles) to Overlook Restrooms (L08)\n"
				+ "}", manager.getDistancesReport("L02"));
		// Testing manager with all landmarks the same, except different descriptions and ids 
		try {
        	manager = new ReportManager("input/landmarks-2.csv", "input/trails-2.csv");
        } catch (Exception e) {
        	fail("Unexpected exception thrown: " + e.getMessage());
        }
		assertEquals("Landmarks Reachable from ORIGINAL (L001) {\n"
				+ "   794 feet to ABCDE (L005)\n"
				+ "   794 feet to BCDEF (L002)\n"
				+ "   794 feet to CDEFG (L003)\n"
				+ "   794 feet to DEFGH (L004)\n"
				+ "   794 feet to EFGHI (L006)\n"
				+ "}", manager.getDistancesReport("L001"));
		// Testing two locations that are the same distance 
		try {
        	manager = new ReportManager("input/landmarks-1.csv", "input/trails-1.csv");
        } catch (Exception e) {
        	fail("Unexpected exception thrown: " + e.getMessage());
        }
		assertEquals("Landmarks Reachable from ORIGINAL (L001) {\n"
				+ "   794 feet to FNUEKHG (L004)\n"
				+ "   794 feet to JAXUVFL (L005)\n"
				+ "   1931 feet to CAURHEC (L003)\n"
				+ "   1931 feet to GKGWTVD (L002)\n"
				+ "}", manager.getDistancesReport("L001"));
	}
	
	/** 
	  * Tests the getProposedFirstAidLocations method with incorrect and correct uses
	  */
	@Test
	public void testGetProposedFirstAidLocations() {
		setUp();
		assertEquals("Number of intersecting trails must be greater than 0.", manager.getProposedFirstAidLocations(0));
		assertEquals("No landmarks have at least 4 intersecting trails.", manager.getProposedFirstAidLocations(4));
		assertEquals("Proposed Locations for First Aid Stations {\n"
				+ "   Park Entrance (L01) - 3 intersecting trails\n"
				+ "}", manager.getProposedFirstAidLocations(3));
		assertEquals("Proposed Locations for First Aid Stations {\n"
				+ "   Park Entrance (L01) - 3 intersecting trails\n"
				+ "   Entrance Fountain (L02) - 2 intersecting trails\n"
				+ "   Entrance Restrooms (L04) - 2 intersecting trails\n"
				+ "   Overlook 1 (L05) - 2 intersecting trails\n"
				+ "   Overlook 2 (L07) - 2 intersecting trails\n"
				+ "   Rock Formation 1 (L06) - 2 intersecting trails\n"
				+ "   Waste Station 1 (L03) - 2 intersecting trails\n"
				+ "}", manager.getProposedFirstAidLocations(2));
		assertEquals("Proposed Locations for First Aid Stations {\n"
				+ "   Park Entrance (L01) - 3 intersecting trails\n"
				+ "   Entrance Fountain (L02) - 2 intersecting trails\n"
				+ "   Entrance Restrooms (L04) - 2 intersecting trails\n"
				+ "   Overlook 1 (L05) - 2 intersecting trails\n"
				+ "   Overlook 2 (L07) - 2 intersecting trails\n"
				+ "   Rock Formation 1 (L06) - 2 intersecting trails\n"
				+ "   Waste Station 1 (L03) - 2 intersecting trails\n"
				+ "   Campsite 1 (L11) - 1 intersecting trails\n"
				+ "   Campsite Restrooms (L12) - 1 intersecting trails\n"
				+ "   Hidden Gardens (L10) - 1 intersecting trails\n"
				+ "   Overlook Restrooms (L08) - 1 intersecting trails\n"
				+ "   Waste Station 2 (L09) - 1 intersecting trails\n"
				+ "}", manager.getProposedFirstAidLocations(1));
	}

}
