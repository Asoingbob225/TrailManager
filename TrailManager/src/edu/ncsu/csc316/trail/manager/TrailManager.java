package edu.ncsu.csc316.trail.manager;

import java.io.FileNotFoundException;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.map.Map.Entry;
import edu.ncsu.csc316.trail.data.Landmark;
import edu.ncsu.csc316.trail.data.Trail;
import edu.ncsu.csc316.trail.io.TrailInputReader;
import edu.ncsu.csc316.trail.dsa.DSAFactory;

/**
 * This class contains the main algorithms for acquiring the distance and
 * proposed first aid location reports.
 * 
 * @author Jimin Yu, jyu34
 *
 */
public class TrailManager {

	private List<Landmark> landmarks;
	private List<Trail> trails;

	/**
	 * The constructor for TrailManager
	 * 
	 * @param pathToLandmarkFile path to file including landmark information
	 * @param pathToTrailFile    path to file including trail information
	 * @throws FileNotFoundException if the provided file path does not exist
	 */
	public TrailManager(String pathToLandmarkFile, String pathToTrailFile) throws FileNotFoundException {

		landmarks = TrailInputReader.readLandmarks(pathToLandmarkFile);
		trails = TrailInputReader.readTrails(pathToTrailFile);
	}

	public Map<Landmark, Integer> getDistancesToDestinations(String originLandmark) {

		Landmark origin = getLandmarkByID(originLandmark);

		if (origin == null) {
			return null;
		}

		Map<Landmark, Integer> minDistMap = DSAFactory.getMap(null);
		
		Map<Landmark, Integer> reachFromOrigin = DSAFactory.getMap(null);

		Map<Landmark, Integer> distances = DSAFactory.getMap(null);

		for (Landmark landmark : landmarks) {
			distances.put(landmark, Integer.MAX_VALUE);
		}

		Map<Landmark, Map<Landmark, Integer>> neighborDist = DSAFactory.getMap(null);
		
		for (Landmark landmark : landmarks) {
			neighborDist.put(landmark, getDistancesToNeighbors(landmark));
		}

		distances.put(origin, 0);

		reachFromOrigin.put(origin, distances.get(origin));

		while (reachFromOrigin.size() != 0) {
			Landmark current = getLowestDistanceLandmark(reachFromOrigin);
			reachFromOrigin.remove(current);

			for (Entry<Landmark, Integer> neighboringLandmark : neighborDist.get(current).entrySet()) {
				Landmark neighbor = neighboringLandmark.getKey();
				Integer lenTrail = neighboringLandmark.getValue();

				if (minDistMap.get(neighbor) == null) {
					getMinimumDistance(neighbor, current, lenTrail, distances);
					reachFromOrigin.put(neighbor, distances.get(neighbor));
				}
			}
			minDistMap.put(current, distances.get(current));
		}

		return minDistMap;
		
	}

	private Landmark getLowestDistanceLandmark(Map<Landmark, Integer> reachFromOrigin) {
		Landmark lowestDistLandmark = null;
		int lowestDist = Integer.MAX_VALUE;
		for (Landmark landmark : reachFromOrigin) {
			int distance = reachFromOrigin.get(landmark);
			if (distance < lowestDist) {
				lowestDist = distance;
				lowestDistLandmark = landmark;
			}
		}
		return lowestDistLandmark;
	}

	private void getMinimumDistance(Landmark lowestDist, Landmark origin, Integer lenTrail,
			Map<Landmark, Integer> distances) {
		if (lowestDist != null && origin != null && distances != null) {
			int startDist = distances.get(origin);
			if (startDist + lenTrail < distances.get(lowestDist)) {
				distances.put(lowestDist, startDist + lenTrail);
			}
		}
	}

	private Map<Landmark, Integer> getDistancesToNeighbors(Landmark landmark) {
		Map<Landmark, Integer> distancesToNeighbors = DSAFactory.getMap(null);
		for (Trail trail: trails) {
			if (trail.getLandmarkOne().equals(landmark.getId())) {
				distancesToNeighbors.put(getLandmarkByID(trail.getLandmarkTwo()), trail.getLength());
			}
			else if (trail.getLandmarkTwo().equals(landmark.getId())) {
				distancesToNeighbors.put(getLandmarkByID(trail.getLandmarkOne()), trail.getLength());
			}
		}
		return distancesToNeighbors;
	}

	public Landmark getLandmarkByID(String landmarkID) {
		for (Landmark landmark : landmarks) {
			if (landmark.getId().equals(landmarkID)) {
				return landmark;
			}
		}
		return null;
	}

	public Map<Landmark, List<Trail>> getProposedFirstAidLocations(int numberOfIntersectingTrails) {
		Map<Landmark, List<Trail>> intersectingMap = DSAFactory.getMap(null);
		for (Landmark landmark : landmarks) {
			if (getIntersectingTrails(landmark).size() >= numberOfIntersectingTrails) {
				intersectingMap.put(landmark, getIntersectingTrails(landmark));
			}
		}
		return intersectingMap;
	}

	private List<Trail> getIntersectingTrails(Landmark origin) {
		List<Trail> list = DSAFactory.getIndexedList();
		for (Trail trail : trails) {
			if (origin.getId().equals(trail.getLandmarkOne()) || origin.getId().equals(trail.getLandmarkTwo())) {
				list.addLast(trail);
			}
		}
		return list;
	}
}
