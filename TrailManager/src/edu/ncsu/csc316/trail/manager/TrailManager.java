package edu.ncsu.csc316.trail.manager;

import java.io.FileNotFoundException;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.trail.data.Landmark;
import edu.ncsu.csc316.trail.data.Trail;
import edu.ncsu.csc316.trail.io.TrailInputReader;
import edu.ncsu.csc316.trail.dsa.DSAFactory;

public class TrailManager {

	private List<Landmark> landmarks;
	private List<Trail> trails;
	
	private Map<Landmark, List<Trail>> map = DSAFactory.getMap(null);

	public TrailManager(String pathToLandmarkFile, String pathToTrailFile) throws FileNotFoundException {

    	landmarks = TrailInputReader.readLandmarks(pathToLandmarkFile);
    	trails = TrailInputReader.readTrails(pathToTrailFile);
    	
    	List<Trail> intersecting = DSAFactory.getIndexedList();

    	for (int i = 0; i < landmarks.size(); i++) {
    		for (int j = 0; j < trails.size(); j++) {
    			if (landmarks.get(i).getId().equals(trails.get(j).getLandmarkOne()) || landmarks.get(i).getId().equals(trails.get(j).getLandmarkTwo())) {
    				intersecting.addLast(trails.get(j));
    			}
    		}
    		for (int k = 0; k < intersecting.size(); i++) {
    			if (!landmarks.get(i).getId().equals(intersecting.get(k).getLandmarkOne()) && !landmarks.get(i).getId().equals(intersecting.get(k).getLandmarkTwo())) {
    				intersecting.remove(k);
    			}
    		}
    		map.put(landmarks.get(i), intersecting);
    	}
    }

	public Map<Landmark, Integer> getDistancesToDestinations(String originLandmark) {
		// TODO: Complete this method
		// Remember to use DSAFactory to get instances of data structures or sorters
		// that you will need!
		// For example: DSAFactory.getIndexedList() or DSAFactory.getMap(null)
		// See the project writeup for more information about using DSAFactory.

		Map<Landmark, Integer> distMap = DSAFactory.getMap(null);
		List<Landmark> landList = DSAFactory.getIndexedList();

		for (Landmark k: map) {
			if (k.getId().equals(originLandmark)) {
				// If the landmark is the starting point it would be 0 feet away
				landList.addFirst(k);
				distMap.put(k, 0);
			}
			else {
				landList.addLast(k);
				//todo
			}
		}
	}
	private int startingToLandmark(Landmark origin, Landmark k) {
		// The distance between the target Landmark and the current trail
		int currentDist = 0;
		// The total distance of all the Landmark’s trails
		int totalDist = 0;
		// Boolean signifying whether a Landmark’s trails lead to the starting point or not
		boolean atDest = false;
		
//		int numTrails = map.get(k).size();
//		for (int i = 0; i < numTrails; i++) {
//			currentDist = map.get(k).get(i).getLength();
//		}
		
		while (!atDest) {
			
		}
		
	}

	public Landmark getLandmarkByID(String landmarkID) {
		// TODO: Complete this method
		return null;
	}

	public Map<Landmark, List<Trail>> getProposedFirstAidLocations(int numberOfIntersectingTrails) {
		// TODO: Complete this method
		return null;
	}
}
