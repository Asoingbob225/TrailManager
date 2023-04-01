package edu.ncsu.csc316.trail.manager;

import java.io.FileNotFoundException;
import java.util.Comparator;

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
 * This class creates the reports for proposed first aid locations and distances
 * to all reachable destinations from an origin. It also carries the
 * functionality of sorting each report in corresponding order.
 * 
 * @author Jimin Yu, jyu34
 *
 */
public class ReportManager {

	/** Represents one mile */
	public static final double MILE = 5280.0;
	/** Represents the decimal place to round mile values to */
	public static final double ROUND = 100.0;

	/** An instance of a sorter used to sort the distance reports */
	private Sorter<Entry<Landmark, Integer>> distanceSorter;
	/** An instance of a sorter used to sort the first aid location reports */
	private Sorter<Entry<Landmark, List<Trail>>> intersectSorter;

	/** An instance of the TrailManager object */
	private TrailManager t;

	/**
	 * This is the constructor for ReportManager. It initializes all data structures
	 * and sorting algorithms used in this project and initializes an instance of a
	 * TrailManager
	 * 
	 * @param pathToLandmarkFile path to file including landmark information
	 * @param pathToTrailFile    path to file including trail information
	 * @throws FileNotFoundException if the provided file path does not exist
	 */
	public ReportManager(String pathToLandmarkFile, String pathToTrailFile) throws FileNotFoundException {

		DSAFactory.setMapType(DataStructure.SKIPLIST);
		DSAFactory.setListType(DataStructure.ARRAYBASEDLIST);
		DSAFactory.setComparisonSorterType(Algorithm.MERGESORT);

		t = new TrailManager(pathToLandmarkFile, pathToTrailFile);

	}

	/**
	 * This method outputs the proper report for the distances to destinations from
	 * an origin point. If no landmark exists with the given ID or if none are
	 * reachable from the landmark with the given ID, it returns the proper invalid
	 * output statement. If any of the distances are over a mile, it returns the
	 * standard output, but for the distances greater than a mile, it is also
	 * displayed in miles to the nearest hundredth.
	 * 
	 * @param originLandmark the origin point to calculate all reachable locations
	 * @return the proper output statement corresponding to the given ID
	 */
	public String getDistancesReport(String originLandmark) {
		if (t.getDistancesToDestinations(originLandmark).size() == 0) {
			return "The provided landmark ID (" + originLandmark + ") is invalid for the park.";
		}

		Map<Landmark, Integer> distanceMap = t.getDistancesToDestinations(originLandmark);
		Entry<Landmark, Integer>[] sortedList = distanceSort(distanceMap);

		if (sortedList.length == 1 && sortedList[0].getKey().getId().equals(originLandmark)) {
			return "No landmarks are reachable from " + t.getLandmarkByID(originLandmark).getDescription() + " ("
					+ originLandmark + ")" + ".";
		}
		StringBuilder output = new StringBuilder();
		output.append("Landmarks Reachable from " + sortedList[0].getKey().getDescription() + " (" + originLandmark
				+ ") {\n");
		for (int i = 1; i < sortedList.length; i++) {
			if (sortedList[i].getValue() > MILE) {
				double miles = sortedList[i].getValue() / MILE;
				miles = Math.round(miles * ROUND) / ROUND;
				output.append("   " + sortedList[i].getValue() + " feet (" + miles + " miles) to "
						+ sortedList[i].getKey().getDescription() + " (" + sortedList[i].getKey().getId() + ")\n");
			} else {
				output.append("   " + sortedList[i].getValue() + " feet to " + sortedList[i].getKey().getDescription()
						+ " (" + sortedList[i].getKey().getId() + ")\n");
			}
		}
		output.append("}");

		return output.toString();
	}

	/**
	 * This method outputs the proper report for proposed first aid locations. If
	 * the inputted number of intersecting trails is less than or equal to 0, or is
	 * too large of a number, it returns the proper invalid output statement.
	 * 
	 * @param numberOfIntersectingTrails the minimum number of intersecting trails
	 *                                   that an outputted landmark must have
	 * @return the proper output statement corresponding to the given number of
	 *         intersecting trails
	 */
	public String getProposedFirstAidLocations(int numberOfIntersectingTrails) {
		if (numberOfIntersectingTrails <= 0) {
			return "Number of intersecting trails must be greater than 0.";
		}

		Map<Landmark, List<Trail>> intersectMap = t.getProposedFirstAidLocations(numberOfIntersectingTrails);
		Entry<Landmark, List<Trail>>[] sortedList = intersectSort(intersectMap);

		if (sortedList.length == 0) {
			return "No landmarks have at least " + numberOfIntersectingTrails + " intersecting trails.";
		}

		StringBuilder output = new StringBuilder();
		output.append("Proposed Locations for First Aid Stations {\n");
		for (int i = 0; i < sortedList.length; i++) {
			if (sortedList[i].getValue().size() >= numberOfIntersectingTrails) {
				output.append("   " + sortedList[i].getKey().getDescription() + " (" + sortedList[i].getKey().getId()
						+ ") - " + sortedList[i].getValue().size() + " intersecting trails\n");
			}
		}
		output.append("}");
		return output.toString();
	}

	/**
	 * This is a helper method which sorts the output of
	 * TrailManager.getDistancesToDestinations(). It creates a new array of map
	 * entries, iterates through map, and adds each element into the array. It is
	 * then sorted using a custom comparator.
	 * 
	 * @param map the map of landmarks and their distances to the origin that needs
	 *            to be sorted
	 * @return a sorted list of the map's entries
	 */
	private Entry<Landmark, Integer>[] distanceSort(Map<Landmark, Integer> map) {
		DistanceComparator c = new DistanceComparator();
		distanceSorter = DSAFactory.getComparisonSorter(c);

		@SuppressWarnings("unchecked")
		Entry<Landmark, Integer>[] e = new Entry[map.size()];

		int i = 0;
		for (Entry<Landmark, Integer> entry : map.entrySet()) {
			e[i] = entry;
			i++;
		}

		distanceSorter.sort(e);
		return e;
	}

	/**
	 * This is a helper method which sorts the output of
	 * TrailManager.getProposedFirstAidLocation(). It creates a new array of map
	 * entries, iterates through map, and adds each element into the array. It is
	 * then sorted using a custom comparator.
	 * 
	 * @param map the map of landmarks and their intersecting trails that needs to
	 *            be sorted
	 * @return a sorted list of the map's entries
	 */
	private Entry<Landmark, List<Trail>>[] intersectSort(Map<Landmark, List<Trail>> map) {
		IntersectComparator c = new IntersectComparator();
		intersectSorter = DSAFactory.getComparisonSorter(c);

		@SuppressWarnings("unchecked")
		Entry<Landmark, List<Trail>>[] e = new Entry[map.size()];
		int i = 0;
		for (Entry<Landmark, List<Trail>> entry : map.entrySet()) {
			e[i] = entry;
			i++;
		}

		intersectSorter.sort(e);
		return e;
	}

	/**
	 * This class contains a custom comparator object used to compare landmarks by
	 * their distance to the starting point. If the distance of the first landmark
	 * parameter is shorter or longer than the second landmark parameter, it returns
	 * -1 or 1 respectively. If the distances are equal, the landmarks are sorted
	 * accordingly based on alphabetical order (ascending A-Z) of their description.
	 * 
	 * @author Jimin Yu, jyu34
	 *
	 */
	private class DistanceComparator implements Comparator<Entry<Landmark, Integer>> {

		@Override
		public int compare(Entry<Landmark, Integer> one, Entry<Landmark, Integer> two) {
			if (one.getValue() < two.getValue()) {
				return -1;
			} else if (one.getValue() > two.getValue()) {
				return 1;
			} else {
				return one.getKey().getDescription().compareTo(two.getKey().getDescription());
			}
		}
	}

	/**
	 * This class contains a custom comparator object used to compare landmarks by
	 * their number of intersecting trails. If the first landmark parameter has more
	 * or less intersecting trails than the second landmark parameter, it returns -1
	 * or 1 respectively. If the distances are equal, the landmarks are sorted
	 * accordingly based on alphabetical order (ascending A-Z) of their description.
	 * 
	 * @author Jimin Yu, jyu34
	 *
	 */
	private class IntersectComparator implements Comparator<Entry<Landmark, List<Trail>>> {

		@Override
		public int compare(Entry<Landmark, List<Trail>> one, Entry<Landmark, List<Trail>> two) {
			if (one.getValue().size() > two.getValue().size()) {
				return -1;
			} else if (one.getValue().size() < two.getValue().size()) {
				return 1;
			}
			return one.getKey().getDescription().compareTo(two.getKey().getDescription());
		}
	}
}
