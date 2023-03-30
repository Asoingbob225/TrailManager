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

public class ReportManager {

	public static final double MILE = 5280.0;
	public static final double ROUND = 100.0;

	private Sorter<Entry<Landmark, Integer>> distanceSorter;
	private Sorter<Entry<Landmark, List<Trail>>> intersectSorter;

	private TrailManager t;

	public ReportManager(String pathToLandmarkFile, String pathToTrailFile) throws FileNotFoundException {

		DSAFactory.setMapType(DataStructure.UNORDEREDLINKEDMAP);
		DSAFactory.setListType(DataStructure.SINGLYLINKEDLIST);
		DSAFactory.setComparisonSorterType(Algorithm.MERGESORT);

		t = new TrailManager(pathToLandmarkFile, pathToTrailFile);

	}

	public String getDistancesReport(String originLandmark) {
		if (t.getLandmarkByID(originLandmark) == null) {
			return "The provided landmark ID (" + originLandmark + ") is invalid for the park.";
		}

		Map<Landmark, Integer> distanceMap = t.getDistancesToDestinations(originLandmark);
		Entry<Landmark, Integer>[] sortedList = distanceSort(distanceMap);

		if (sortedList.length == 1 && sortedList[0].getKey().getId().equals(originLandmark)) {
			return "No landmarks are reachable from " + originLandmark + ".";
		}

		String output = "";
		output += "Landmarks Reachable from " + sortedList[0].getKey().getDescription() + " (" + originLandmark
				+ ") {\n";
		for (int i = 1; i < sortedList.length; i++) {
			if (sortedList[i].getValue() > MILE) {
				double miles = sortedList[i].getValue() / MILE;
				miles = Math.round(miles * ROUND) / ROUND;
				output += "   " + sortedList[i].getValue() + " feet (" + miles + " miles) to "
						+ sortedList[i].getKey().getDescription() + " (" + sortedList[i].getKey().getId() + ")\n";
			} else {
				output += "   " + sortedList[i].getValue() + " feet to " + sortedList[i].getKey().getDescription() + " ("
						+ sortedList[i].getKey().getId() + ")\n";
			}
		}
		output += "}";
		return output;
	}

	public String getProposedFirstAidLocations(int numberOfIntersectingTrails) {
		if (numberOfIntersectingTrails <= 0) {
			return "Number of intersecting trails must be greater than 0.";
		}

		Map<Landmark, List<Trail>> intersectMap = t.getProposedFirstAidLocations(numberOfIntersectingTrails);
		Entry<Landmark, List<Trail>>[] sortedList = intersectSort(intersectMap);

		if (sortedList.length == 0) {
			return "No landmarks have at least " + numberOfIntersectingTrails + " intersecting trails";
		}

		String output = "";
		output += "Proposed Locations for First Aid Stations {\n";
		for (int i = 0; i < sortedList.length; i++) {
			if (sortedList[i].getValue().size() >= numberOfIntersectingTrails) {
				output += "   " + sortedList[i].getKey().getDescription() + " (" + sortedList[i].getKey().getId()
						+ ") - " + sortedList[i].getValue().size() + " intersecting trails\n";
			}
		}
		output += "}";
		return output;
	}

	@SuppressWarnings("unchecked")
	private Entry<Landmark, Integer>[] distanceSort(Map<Landmark, Integer> map) {
		DistanceComparator c = new DistanceComparator();
		distanceSorter = DSAFactory.getComparisonSorter(c);

		List<Entry<Landmark, Integer>> list = DSAFactory.getIndexedList();
		for (Entry<Landmark, Integer> entry : map.entrySet()) {
			list.addLast(entry);
		}

		Entry<Landmark, Integer>[] e = new Entry[list.size()];
		
		int i = 0;
		for (Entry<Landmark, Integer> entry: list) {
			e[i] = entry;
			i++;
		}

		distanceSorter.sort(e);
		return e;
	}

	@SuppressWarnings("unchecked")
	private Entry<Landmark, List<Trail>>[] intersectSort(Map<Landmark, List<Trail>> map) {
		IntersectComparator c = new IntersectComparator();
		intersectSorter = DSAFactory.getComparisonSorter(c);

		List<Entry<Landmark, List<Trail>>> list = DSAFactory.getIndexedList();
		for (Entry<Landmark, List<Trail>> entry : map.entrySet()) {
			list.addLast(entry);
		}

//		Entry<Landmark, List<Trail>>[] e = (Entry<Landmark, List<Trail>>[]) (new Object[list.size()]);
//		for (int i = 0; i < list.size(); i++) {
//			e[i] = list.get(i);
//		}
		Entry<Landmark, List<Trail>>[] e = new Entry[list.size()];
		int i = 0;
		for (Entry<Landmark, List<Trail>> entry: list) {
			e[i] = entry;
			i++;
		}
		
		intersectSorter.sort(e);
		return e;
	}

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
