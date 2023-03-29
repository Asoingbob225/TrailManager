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
	
	public static final int MILE = 5280;
	
	private Sorter<Entry<Landmark, Integer>> distanceSorter;
	private Sorter<Entry<Landmark, List<Trail>>> intersectSorter;
	
	private TrailManager t;
	

    public ReportManager(String pathToLandmarkFile, String pathToTrailFile) throws FileNotFoundException {
        // TODO: FIRST set the data structures and algorithms you will need to use for your solution.
        // For example, for Maps: DSAFactory.setMapType(DataStructure.<MAPTYPE>)
        // You'll need to set any Maps, Lists, and/or sorting algorithms that are used.
        // See the project writeup for more information about using DSAFactory.
        // This is the ONLY place you will need to call these DSAFactory setter methods!
        
        // TODO: Complete this constructor
    	
    	DSAFactory.setMapType(DataStructure.UNORDEREDLINKEDMAP);
    	DSAFactory.setListType(DataStructure.SINGLYLINKEDLIST);
    	DSAFactory.setComparisonSorterType(Algorithm.MERGESORT);
    	
    	t = new TrailManager(pathToLandmarkFile, pathToTrailFile);
        
    }

    public String getDistancesReport(String originLandmark) {
    	
    	Map<Landmark, Integer> distanceMap = t.getDistancesToDestinations(originLandmark);
    	Entry<Landmark, Integer>[] sortedList = distanceSort(distanceMap);
    	
    	String output = "";
    	output += "Landmarks Reachable from " + sortedList[0].getKey().getDescription() + " (" + originLandmark + ") {\n";
    	for (int i = 1; i < sortedList.length; i++) {
    		if (sortedList[i].getValue() > MILE) {
    			double miles = sortedList[i].getValue() / MILE;
    			output += "\t" + sortedList[i].getValue() + " feet (" + miles + " miles) to " + sortedList[i].getKey().getDescription() + " (" + sortedList[i].getKey().getId() + ")\n";
    		}
    		else {
    			output += "\t" + sortedList[i].getValue() + " feet to " + sortedList[i].getKey().getDescription() + " (" + sortedList[i].getKey().getId() + ")\n";
    		}
    	}
    	output += "}";
    	return output;
    }

    public String getProposedFirstAidLocations(int numberOfIntersectingTrails) {
    	Map<Landmark, List<Trail>> intersectMap = t.getProposedFirstAidLocations(numberOfIntersectingTrails);
    	Entry<Landmark, List<Trail>>[] sortedList = intersectSort(intersectMap);
    	
    	String output = "";
    	output += "Proposed Locations for First Aid Stations {\n";
    	for (int i = 0; i < sortedList.length; i++) {
    		if (sortedList[i].getValue().size() >= numberOfIntersectingTrails) {
    			output += "\t" + sortedList[i].getKey().getDescription() + " (" + sortedList[i].getKey().getId() + ") - " + sortedList[i].getValue().size() + " intersecting trails\n";
    		}
    	}
    	output += "\n";
    	return output;
    }
    
    @SuppressWarnings("unchecked")
	private Entry<Landmark, Integer>[] distanceSort(Map<Landmark, Integer> map) {
		DistanceComparator c = new DistanceComparator();
		distanceSorter = DSAFactory.getComparisonSorter(c);

		List<Entry<Landmark, Integer>> list = DSAFactory.getIndexedList();
		for (Entry<Landmark, Integer> entry: map.entrySet()) {
			list.addLast(entry);
		}
		
		Entry<Landmark, Integer>[] e = (Entry<Landmark, Integer>[]) (new Object[list.size()]);
		for (int i = 0; i < list.size(); i++) {
			e[i] = list.get(i);
		}
		distanceSorter.sort(e);
		return e;
	}
    
    @SuppressWarnings("unchecked")
	private Entry<Landmark, List<Trail>>[] intersectSort(Map<Landmark, List<Trail>> map) {
    	IntersectComparator c = new IntersectComparator();
    	 intersectSorter = DSAFactory.getComparisonSorter(c);
    	 
    	 List<Entry<Landmark, List<Trail>>> list = DSAFactory.getIndexedList();
    	 for (Entry<Landmark, List<Trail>> entry: map.entrySet()) {
    		 list.addLast(entry);
    	 }
    	 
    	 Entry<Landmark, List<Trail>>[] e = (Entry<Landmark, List<Trail>>[]) (new Object[list.size()]);
    	 for (int i = 0; i < list.size(); i++) {
    		 e[i] = list.get(i);
    	 }
    	 intersectSorter.sort(e);
    	 return e;
    }
    
	private class DistanceComparator implements Comparator<Entry<Landmark, Integer>> {
		
		@Override
		public int compare(Entry<Landmark, Integer> one, Entry<Landmark, Integer> two) {
			if (one.getValue() < two.getValue()) {
				return -1;
			}
			else if (one.getValue() > two.getValue()) {
				return 1;
			}
			else {
				return one.getKey().getDescription().compareTo(two.getKey().getDescription());
			}
		}
	}
	
	private class IntersectComparator implements Comparator<Entry<Landmark, List<Trail>>> {

		@Override
		public int compare(Entry<Landmark, List<Trail>> one, Entry<Landmark, List<Trail>> two) {
			if (one.getValue().size() > two.getValue().size()) {
				return -1;
			}
			else if (one.getValue().size() < two.getValue().size()) {
				return 1;
			}
			return one.getKey().getDescription().compareTo(two.getKey().getDescription());
		}
		
	}
    
}
