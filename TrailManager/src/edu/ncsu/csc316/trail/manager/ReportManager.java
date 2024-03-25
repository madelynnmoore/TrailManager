package edu.ncsu.csc316.trail.manager;

import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Iterator;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.map.Map.Entry;
import edu.ncsu.csc316.dsa.sorter.Sorter;
import edu.ncsu.csc316.trail.data.Landmark;
import edu.ncsu.csc316.trail.data.Trail;
import edu.ncsu.csc316.trail.dsa.Algorithm;
import edu.ncsu.csc316.trail.dsa.DSAFactory;
import edu.ncsu.csc316.trail.dsa.DataStructure;

/**
 * ReportManager is responsible for keeping track of all the information within the system. ReportManager works
 * closely with TrailManager to effectively print out all that the user asks for, given the information. 
 *
 * @author Maddie Moore
 *
 */
public class ReportManager {

	/** Constant that represents the feet in a mile for converstions */
	private final double FEET_IN_MILE = 5280.0;
	
	/** TrailManager object that is used for manipulation */
	private TrailManager manager;
	
	/** 
	 * Constructs a new ReportManager object by initializing the manager field and setting all the DSAFactory types.
	 * @param pathToLandmarkFile String of the path to the landmark file
	 * @param pathToTrailFile String of the path to the trail file 
	 * @throws FileNotFoundException if the file does not exist or cannot be found
	 */
    public ReportManager(String pathToLandmarkFile, String pathToTrailFile) throws FileNotFoundException {
    	DSAFactory.setMapType(DataStructure.SKIPLIST);
    	DSAFactory.setListType(DataStructure.ARRAYBASEDLIST);
    	DSAFactory.setNonComparisonSorterType(Algorithm.COUNTING_SORT);
    	DSAFactory.setComparisonSorterType(Algorithm.MERGESORT);
        manager = new TrailManager(pathToLandmarkFile, pathToTrailFile);
    }

    /** 
     * Returns a String of the distance report based off of the map returned from the getDistancesToDestinations method
     * in TrailManager
     * @param originLandmark the ID of the origin Landmark
     * @return a string of the distances between the given landmark and other landmarks in the trails 
     */
    public String getDistancesReport(String originLandmark) {
    	Map<Landmark, Integer> entryMap = manager.getDistancesToDestinations(originLandmark);
    	if (entryMap.size() == 0) {
    		return "The provided landmark ID (" + originLandmark + ") is invalid for the park.";
    	}
    	Landmark ogLandmark = manager.getLandmarkByID(originLandmark);
    	if (entryMap.size() == 1) { 
    		return "No landmarks are reachable from " + ogLandmark.getDescription() + " (" + originLandmark + ").";
    	}
    	Entry<Landmark, Integer>[] entryArray = new Map.Entry[entryMap.size() - 1];
    	Sorter<Entry<Landmark, Integer>> sorter = DSAFactory.getComparisonSorter(new DistanceReportComparator());
    	int i = 0;
    	for(Map.Entry<Landmark, Integer> entry : entryMap.entrySet()) {
    		if (entry != null && !entry.getKey().getId().equals(originLandmark)) {
    			entryArray[i] = entry;
    			i++;
    		}
    	}
    	sorter.sort(entryArray);
    	String report = "Landmarks Reachable from " + ogLandmark.getDescription() + " (" + ogLandmark.getId() + ") {\n";
    	for(Map.Entry<Landmark, Integer> entry : entryArray) {
    		report += "   " + entry.getValue() + " feet";
    		if (entry.getValue() > FEET_IN_MILE) {
    			double distanceInMiles = (double) entry.getValue() / FEET_IN_MILE;
    			report += " (" + Math.round(distanceInMiles * 100.0) / 100.0 + " miles)";
    		}
    		report += " to " + entry.getKey().getDescription() + " (" + entry.getKey().getId() + ")\n";
    	}
    	report += "}";
        return report;
    }

    /**
     * Returns a string of the proposed first aid locations, which is determined from the getProposedFirstAidLocations
     * in Trail Manager. 
     * @param numberOfIntersectingTrails the number of intersecting trails needed to be found
     * @return a string that explains the first aid locations 
     */
    public String getProposedFirstAidLocations(int numberOfIntersectingTrails) {
    	if (numberOfIntersectingTrails <= 0) {
    		return "Number of intersecting trails must be greater than 0.";
    	}
    	Map<Landmark, List<Trail>> entryMap = manager.getProposedFirstAidLocations(numberOfIntersectingTrails);
    	if (entryMap.isEmpty()) {
    		return "No landmarks have at least " + numberOfIntersectingTrails + " intersecting trails.";
    	}
    	Entry<Landmark, List<Trail>>[] entryArray = new Map.Entry[entryMap.size()];
    	Sorter<Entry<Landmark, List<Trail>>> sorter = DSAFactory.getComparisonSorter(new FirstAidLocationsComparator());
    	int i = 0;
    	for(Map.Entry<Landmark, List<Trail>> entry : entryMap.entrySet()) {
    		entryArray[i] = entry;
    		i++;
    	}
    	sorter.sort(entryArray);
    	String report = "Proposed Locations for First Aid Stations {\n";
    	for(Map.Entry<Landmark, List<Trail>> entry : entryArray) {
    		report += "   " + entry.getKey().getDescription() + " (" + entry.getKey().getId() + ") - " + entry.getValue().size() + " intersecting trails\n"; 
    	}
    	report += "}";
        return report;
    }
    
    /**
     * Comparator used to compare entries to one another. They are sorted by descending order of list size, and then
     * alphabetical order of description. 
     *
     * @author Maddie Moore
     *
     */
    public class FirstAidLocationsComparator implements Comparator<Entry<Landmark, List<Trail>>> {
    	   
        @Override
        public int compare(Entry<Landmark, List<Trail>> first, Entry<Landmark, List<Trail>> second) {
        	if(first.getValue().size() == second.getValue().size())
        		return first.getKey().getDescription().compareTo(second.getKey().getDescription());
        	else {
	            if (first.getValue().size() < second.getValue().size())
	            	return 1;
	            else
	            	return -1;
	        }
        }
    }
    
    /**
     * Comparator used to sort entries to one another. They are sorted by ascending order of integers, and then 
     * alphabetical order of their description.
     *
     * @author Maddie Moore
     *
     */
    public class DistanceReportComparator implements Comparator<Entry<Landmark, Integer>> {
    	
    	@Override
        public int compare(Entry<Landmark, Integer> first, Entry<Landmark, Integer> second) {
    		if ((int)first.getValue() == (int)second.getValue()) 
    			return first.getKey().getDescription().compareTo(second.getKey().getDescription());
    		else {
    			if (first.getValue() < second.getValue())
    				return -1;
    			else
    				return 1;
    		}
        }
    }
}
