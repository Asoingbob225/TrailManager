package edu.ncsu.csc316.trail.manager;

import edu.ncsu.csc316.trail.dsa.Algorithm;
import edu.ncsu.csc316.trail.dsa.DSAFactory;
import edu.ncsu.csc316.trail.dsa.DataStructure;

public class ReportManager {

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
    
}
