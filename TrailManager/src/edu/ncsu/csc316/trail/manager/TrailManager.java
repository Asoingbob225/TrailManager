package edu.ncsu.csc316.trail.manager;

public class TrailManager {
    
    public TrailManager(String pathToLandmarkFile, String pathToTrailFile) throws FileNotFoundException {
        // TODO: Complete this constructor
    }
    
    public Map<Landmark, Integer> getDistancesToDestinations(String originLandmark) {
        // TODO: Complete this method
        // Remember to use DSAFactory to get instances of data structures or sorters that you will need!
        // For example: DSAFactory.getIndexedList() or DSAFactory.getMap(null)
        // See the project writeup for more information about using DSAFactory.
    }
    
    public Landmark getLandmarkByID(String landmarkID) {
        // TODO: Complete this method
    }
    
    public Map<Landmark, List<Trail>> getProposedFirstAidLocations(int numberOfIntersectingTrails) {
        // TODO: Complete this method
    }
}
