package edu.ncsu.csc316.trail.manager;

import java.io.FileNotFoundException;
import java.util.Iterator;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.queue.Queue;
import edu.ncsu.csc316.trail.data.Landmark;
import edu.ncsu.csc316.trail.data.Trail;
import edu.ncsu.csc316.trail.dsa.DSAFactory;
import edu.ncsu.csc316.trail.io.TrailInputReader;

/**
 * TrailManager is responsible for setting up the manager, lists, and maps that are used for manipulation
 * of the program. This class is heavily used by ReportManager.
 *
 * @author Maddie Moore
 *
 */
public class TrailManager {
    
	/** List of landmarks */
	private List<Landmark> landmarkList;
	
	/** List of trails */
	private List<Trail> trailList;
	
	/** Map of landmark ID's and the associated landmark */
	private Map<String, Landmark> landmarkMap;
	
	/** Map of landmarks and a list of trails that involve the given landmark */
	private Map<Landmark, List<Trail>> trailMap;
	
	/**
	 * Constructor that is responsible for taking in the input files and initializing the fields given the
	 * output of the IO methods with the file parameters.
	 * @param pathToLandmarkFile file that contains info about all landmarks
	 * @param pathToTrailFile file that contains info about all trails
	 * @throws FileNotFoundException if the file does not exist
	 */
    public TrailManager(String pathToLandmarkFile, String pathToTrailFile) throws FileNotFoundException {
    	landmarkList = TrailInputReader.readLandmarks(pathToLandmarkFile);
    	trailList = TrailInputReader.readTrails(pathToTrailFile);
    	landmarkMap = DSAFactory.getMap(null);
    	for (Landmark landmark : landmarkList) {
    		landmarkMap.put(landmark.getId(), landmark);
    	}
    	trailMap = DSAFactory.getMap(null);
    	for (Trail trail : trailList) {
    		Landmark landmarkOne = landmarkMap.get(trail.getLandmarkOne());
			Landmark landmarkTwo = landmarkMap.get(trail.getLandmarkTwo());
			List<Trail> l1trails = trailMap.get(landmarkOne);
			if (l1trails == null) {
				l1trails = DSAFactory.getIndexedList();
				l1trails.addFirst(trail);
				trailMap.put(landmarkOne, l1trails);
			}
			else {
				l1trails.addFirst(trail);
				trailMap.put(landmarkOne, l1trails);
			}
			List<Trail> l2trails = trailMap.get(landmarkTwo);
			if (l2trails == null) {
				l2trails = DSAFactory.getIndexedList();
				l2trails.addFirst(trail);
				trailMap.put(landmarkTwo, l2trails);
			}
			else {
				l2trails.addFirst(trail);
				trailMap.put(landmarkTwo, l2trails);
			}
    	}
    }
    
    /**
     * Responsible for returning a map in which each entry is a landmark and how far it is from the given
     * landmark, which is found using the ID.
     * @param originLandmark ID of the origin landmark to locate to each landmark
     * @return a map of landmarks and their distances from the origin landmark
     */
    public Map<Landmark, Integer> getDistancesToDestinations(String originLandmark) {
    	Map<Landmark, Integer> mapField = DSAFactory.getMap(null);
    	Queue<Landmark> landmarkQueue = DSAFactory.getQueue();
    	Landmark ogLandmark = landmarkMap.get(originLandmark);
    	if (ogLandmark == null) {
    		return mapField;
    	}
    	landmarkQueue.enqueue(ogLandmark);
    	mapField.put(ogLandmark, 0);
    	while (!landmarkQueue.isEmpty()) {
    		Landmark currentLandmark = landmarkQueue.dequeue();
    		if (trailMap.get(currentLandmark) == null) {
    			continue;
    		}
    		int num = mapField.get(currentLandmark);
    		for (Trail trail : trailMap.get(currentLandmark)) {
    			Landmark landmarkOne = landmarkMap.get(trail.getLandmarkOne());
    			Landmark landmarkTwo = landmarkMap.get(trail.getLandmarkTwo());
    			if (trail.getLandmarkOne().equals(currentLandmark.getId()) && (mapField.get(landmarkTwo) == null)) {
    				landmarkQueue.enqueue(landmarkTwo);
    				mapField.put(landmarkTwo, trail.getLength() + num);
    			}
    			else if (trail.getLandmarkTwo().equals(currentLandmark.getId()) && (mapField.get(landmarkOne) == null)) {
        			landmarkQueue.enqueue(landmarkOne);
					mapField.put(landmarkOne, trail.getLength() + num);
        		}
    		}
    	}
    	if (mapField.size() == 1) {
    		return mapField;
    	}
    	return mapField;
    }
    
    /**
     * Returns the landmark that contains the given ID.
     * @param landmarkID the ID of the landmark to find
     * @return the landmark with the given ID
     */
    public Landmark getLandmarkByID(String landmarkID) {
    	if (landmarkMap.isEmpty()) {
    		return null;
    	}
    	return landmarkMap.get(landmarkID);
    }
    
    /**
     * Responsible for creating and returning a map of entries that contain landmarks and their trails. The map
     * is only returned with entries that contain more than the given amount of number of intersecting trails.
     * @param numberOfIntersectingTrails the number of intersecting trails to locate
     * @return a map of entries (key = landmark, value = list of intersecting trails)
     */
    public Map<Landmark, List<Trail>> getProposedFirstAidLocations(int numberOfIntersectingTrails) {
    	Map<Landmark, List<Trail>> mapField = DSAFactory.getMap(null);
    	if (numberOfIntersectingTrails <= 0) {
    		return mapField;
    	}
    	for (Landmark entry : trailMap) {
    		if (trailMap.get(entry).size() >= numberOfIntersectingTrails) {
    			mapField.put(entry, trailMap.get(entry));
    		}
    	}
    	return mapField;
    }
}
