package edu.ncsu.csc316.trail.manager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc316.trail.dsa.Algorithm;
import edu.ncsu.csc316.trail.dsa.DSAFactory;
import edu.ncsu.csc316.trail.dsa.DataStructure;

class TrailManagerTest {
	
	private TrailManager manager;
	private String pathToLandmarkFile = "input/landmarks_sample.csv";
	private String pathToTrailFile = "input/trails_sample.csv";

	@Before
	public void setUp() {
		DSAFactory.setMapType(DataStructure.SKIPLIST);
    	DSAFactory.setListType(DataStructure.ARRAYBASEDLIST);
    	DSAFactory.setComparisonSorterType(Algorithm.BUBBLE_SORT);
    	DSAFactory.setNonComparisonSorterType(Algorithm.COUNTING_SORT);
    	try {
        	manager = new TrailManager(pathToLandmarkFile, pathToTrailFile);
        } catch (Exception e) {
        	fail("Unexpected exception thrown: " + e.getMessage());
        }
	}
	
	@Test
	public void testTrailManager() {
		setUp();
		try {
			manager = new TrailManager("wrong", pathToTrailFile);
		} catch (Exception e) {
			assertTrue(e instanceof FileNotFoundException);
		}
		try {
			manager = new TrailManager(pathToLandmarkFile, "wrong");
		} catch (Exception e) {
			assertTrue(e instanceof FileNotFoundException);
		}
	}

	@Test
	public void testGetDistancesToDestinations() {
		setUp();
		assertFalse(manager.getDistancesToDestinations("L02").isEmpty());
		assertEquals(10, manager.getDistancesToDestinations("L02").size());
	}

	@Test
	public void testGetLandmarkByID() {
		setUp();
		assertEquals(manager.getLandmarkByID("L02").getId(), "L02");
	}

	@Test
	public void testGetProposedFirstAidLocations() {
		setUp();
		assertTrue(manager.getProposedFirstAidLocations(0).isEmpty());
		assertTrue(manager.getProposedFirstAidLocations(4).isEmpty());
		assertEquals(1, manager.getProposedFirstAidLocations(3).size());
		assertEquals(7, manager.getProposedFirstAidLocations(2).size());
		assertEquals(12, manager.getProposedFirstAidLocations(1).size());
	}

}
