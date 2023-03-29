package edu.ncsu.csc316.trail.manager;

import java.io.FileNotFoundException;
import java.util.Comparator;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.map.Map.Entry;
import edu.ncsu.csc316.dsa.sorter.Sorter;
import edu.ncsu.csc316.trail.data.Landmark;
import edu.ncsu.csc316.trail.dsa.Algorithm;
import edu.ncsu.csc316.trail.dsa.DSAFactory;
import edu.ncsu.csc316.trail.dsa.DataStructure;

public class ReportManager {
	
	private Sorter<Entry<Landmark, Integer>> sorter;

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
        
    }

    public String getDistancesReport(String originLandmark) {
        // TODO: Complete this method
    }

    public String getProposedFirstAidLocations(int numberOfIntersectingTrails) {
        // TODO: Complete this method
    }
    
    @SuppressWarnings("unchecked")
	private Entry<Landmark, Integer>[] distanceSorter(Map<Landmark, Integer> map) {
		DistanceComparator c = new DistanceComparator();
		sorter = DSAFactory.getComparisonSorter(c);

		List<Entry<Landmark, Integer>> list = DSAFactory.getIndexedList();
		for (Entry<Landmark, Integer> entry: map.entrySet()) {
			list.addLast(entry);
		}
		
		Entry<Landmark, Integer>[] e = (Entry<Landmark, Integer>[]) (new Object[list.size()]);
		for (int i = 0; i < list.size(); i++) {
			e[i] = list.get(i);
		}
		sorter.sort(e);
		return e;
	}
    
    


	
	private class DistanceComparator implements Comparator<Entry<Landmark, Integer>>{
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
    
}
