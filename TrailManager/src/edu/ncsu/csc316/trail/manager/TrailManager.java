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

	/** A list of all landmarks in the park */
	private List<Landmark> landmarks;
	/** A list of all trails in the park */
	private List<Trail> trails;

	/**
	 * The constructor for TrailManager
	 * 
	 * @param pathToLandmarkFile path to file including landmark information
	 * @param pathToTrailFile    path to file including trail information
	 * @throws FileNotFoundException if the provided file path does not exist
	 */
	public TrailManager(String pathToLandmarkFile, String pathToTrailFile) throws FileNotFoundException {

		// the list of landmarks is read in from an input file containing all the
		// landmarks' information
		landmarks = TrailInputReader.readLandmarks(pathToLandmarkFile);
		// the list of trails is read in from an input file containing all the trails'
		// information
		trails = TrailInputReader.readTrails(pathToTrailFile);

	}

	/**
	 * This is the algorithm that takes a origin landmark as an input, and returns a
	 * map containing all the reachable landmarks from the starting point as the
	 * key, and the distance to that landmark as the value. It uses several maps to
	 * carry out its implementation, including a map of landmarks and the minimum
	 * distance to the origin landmark, a map of all landmarks reachable from the
	 * origin, a map of landmarks and their distances to the closest neighboring
	 * landmark, and lastly a map of landmarks and its distances to all neighboring
	 * landmarks. It starts by setting all distances to the highest value possible,
	 * and until it finds the smallest distance, it keeps changing the set distance
	 * to a smaller distance. The origin landmark is then initialized to have a
	 * distance of 0. For the other landmarks, It finds the lowest distance to a
	 * neighboring landmark until it reaches the origin, and then adds that to the
	 * map of minimum distances. This algorithm was influenced by Dijkstra's
	 * Algorithm which is explained and implemented from pages 653 - 661 in the
	 * course textbook "Data Structures and Algorithms" by Goodrich, Tamassia,
	 * Goldwasser. However, that algorithm is meant for graphs, and since we are
	 * only allowed to use maps and don't necessarily require the shortest path, the
	 * running times were longer than intended. If the inputed origin landmark does
	 * not exist, then an empty map will be returned.
	 * 
	 * @param originLandmark the starting point landmark
	 * @return a map containing each landmark reachable from the origin and it's
	 *         corresponding shortest distance. If the origin does not exist, then
	 *         an empty map is returned.
	 */
	public Map<Landmark, Integer> getDistancesToDestinations(String originLandmark) {

		Landmark origin = getLandmarkByID(originLandmark);

		// a map of landmarks and the minimum distance to the origin landmark
		Map<Landmark, Integer> minDistMap = DSAFactory.getMap(null);
		// map of all landmarks reachable from the origin
		Map<Landmark, Integer> reachFromOrigin = DSAFactory.getMap(null);
		// a map of landmarks and their distances to the closest neighboring landmark
		Map<Landmark, Integer> distances = DSAFactory.getMap(null);

		// if origin landmark doesn't exist, return empty map
		if (origin == null) {
			return minDistMap;
		}

		// Initialize all distances to highest possible value
		for (Landmark landmark : landmarks) {
			distances.put(landmark, Integer.MAX_VALUE);
		}
		// a map of landmarks and its distances to all neighboring landmarks
		Map<Landmark, Map<Landmark, Integer>> neighborDist = DSAFactory.getMap(null);

		for (Landmark landmark : landmarks) {
			neighborDist.put(landmark, getDistancesToNeighbors(landmark));
		}

		distances.put(origin, 0);
		reachFromOrigin.put(origin, distances.get(origin));

		while (!reachFromOrigin.isEmpty()) {
			Landmark current = getLowestDistanceLandmark(reachFromOrigin);
			reachFromOrigin.remove(current);

			for (Entry<Landmark, Integer> neighboringLandmark : neighborDist.get(current).entrySet()) {
				Landmark neighbor = neighboringLandmark.getKey();
				Integer lenTrail = neighboringLandmark.getValue();

				if (minDistMap.get(neighbor) == null) {
					getMinimumDistance(current, neighbor, lenTrail, distances);
					reachFromOrigin.put(neighbor, distances.get(neighbor));
				}
			}
			minDistMap.put(current, distances.get(current));
		}

		return minDistMap;

	}

	/**
	 * This is a helper method for the getDistancesToDestinations() algorithm. It
	 * obtains the shortest distance landmark from the origin by iterating through a
	 * map of all reachable landmarks and finding the one with the shortest distance
	 * from the origin (the value of the map).
	 * 
	 * @param reachFromOrigin a map of all reachable landmarks from the origin
	 * @return the closest landmark, or the one with the shortest distance from the
	 *         origin
	 */
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

	/**
	 * This is a helper method for the getDistancesToDestinations() algorithm. It
	 * takes the map of landmarks and the distance to their closest neighbor as a
	 * parameter. The three other parameters represent a starting point landmark and
	 * any other neighboring landmark, as well as the distance to that neighboring
	 * landmark. If the distance from the starting point to its neighbor is shorter
	 * than any previously calculated neighbor, then it updates the distance of the
	 * neighbor to be the shorter distance. In getDistancesToDestinations(), this
	 * method will run until the shortest distance to a neighbor (so the closest
	 * neighbor, resulting overall in the shortest path from origin to destination)
	 * is found.
	 * 
	 * @param start      the starting point landmark
	 * @param lowestDist the landmark with the closest distance to the starting
	 *                   point at the given time this method is called
	 * @param lenTrail   length of the trail connecting the starting point to the
	 *                   closest landmark
	 * @param distances  map of landmarks and the distance to their closest neighbor
	 */
	private void getMinimumDistance(Landmark start, Landmark lowestDist, Integer lenTrail,
			Map<Landmark, Integer> distances) {
		if (lowestDist != null && start != null && distances != null) {
			int startDist = distances.get(start);
			if (startDist + lenTrail < distances.get(lowestDist)) {
				distances.put(lowestDist, startDist + lenTrail);
			}
		}
	}

	/**
	 * This is a helper method for the getDistancesToDestinations() algorithm. It
	 * outputs a map including all neighboring landmarks to the parameter landmark,
	 * as well as the distances to them. In getDistancesToDestination(), this method
	 * is called for each landmark in the original list of landmarks read from an
	 * input file, so that it can form a larger map of all landmarks and its
	 * distances to all neighboring landmarks.
	 * 
	 * @param landmark the origin landmark to find neighbors for
	 * @return a map of neighboring landmarks and the corresponding distances to
	 *         them
	 */
	private Map<Landmark, Integer> getDistancesToNeighbors(Landmark landmark) {
		Map<Landmark, Integer> distancesToNeighbors = DSAFactory.getMap(null);
		for (Trail trail : trails) {
			if (trail.getLandmarkOne().equals(landmark.getId())) {
				distancesToNeighbors.put(getLandmarkByID(trail.getLandmarkTwo()), trail.getLength());

			} else if (trail.getLandmarkTwo().equals(landmark.getId())) {
				distancesToNeighbors.put(getLandmarkByID(trail.getLandmarkOne()), trail.getLength());
			}
		}
		return distancesToNeighbors;
	}

	/**
	 * This method returns a landmark object that corresponds to the inputed
	 * landmark ID field. It iterates through the list of landmarks until a matching
	 * one is found. If none is found, or if the landmark with the given ID field
	 * does not exist, it returns null.
	 * 
	 * @param landmarkID the ID to find a corresponding landmark for
	 * @return the landmark which matches the given ID, or null if no match is found
	 */
	public Landmark getLandmarkByID(String landmarkID) {
		for (Landmark landmark : landmarks) {
			if (landmark.getId().equals(landmarkID)) {
				return landmark;
			}
		}
		return null;
	}

	/**
	 * This method returns a map of landmarks and a list of trails that intersect
	 * each corresponding landmark. However, the landmark must have at least the
	 * given parameter's number of intersecting trails. If not, it will not be
	 * included in the output map.
	 * 
	 * @param numberOfIntersectingTrails the minimum number of intersecting trails a
	 *                                   landmark must have
	 * @return a map of landmarks and a list of trails that intersect each
	 *         corresponding landmark, only if the number of intersecting trails is
	 *         greater than or equal to the given parameter
	 */
	public Map<Landmark, List<Trail>> getProposedFirstAidLocations(int numberOfIntersectingTrails) {
		Map<Landmark, List<Trail>> intersectingMap = DSAFactory.getMap(null);
		for (Landmark landmark : landmarks) {
			if (getIntersectingTrails(landmark).size() >= numberOfIntersectingTrails) {
				intersectingMap.put(landmark, getIntersectingTrails(landmark));
			}
		}
		return intersectingMap;
	}

	/**
	 * This is a helper method for getProposedFirstAidLocations(). It returns a list
	 * of all trails that intersect the given parameter landmark.
	 * 
	 * @param origin the landmark to find intersecting trails for
	 * @return a list of all trails that intersect the given landmark
	 */
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
