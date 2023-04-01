package edu.ncsu.csc316.trail.manager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.trail.data.Landmark;
import edu.ncsu.csc316.trail.data.Trail;
import edu.ncsu.csc316.trail.dsa.Algorithm;
import edu.ncsu.csc316.trail.dsa.DSAFactory;
import edu.ncsu.csc316.trail.dsa.DataStructure;



class TrailManagerTest {
	
	private static final String LANDMARKPATH1 = "input/landmark_file1.txt";
	private static final String TRAILPATH1 = "input/trail_file1.txt";

	@BeforeEach
	void setUp() {
		DSAFactory.setMapType(DataStructure.SKIPLIST);
		DSAFactory.setListType(DataStructure.ARRAYBASEDLIST);
		DSAFactory.setComparisonSorterType(Algorithm.MERGESORT);
	}
	
	@Test
	void testTrailManager() {
		assertDoesNotThrow(() -> new TrailManager(LANDMARKPATH1, TRAILPATH1));
	}
	
	@Test
	void testGetDistancesToDestinations() throws FileNotFoundException {
		TrailManager t = new TrailManager(LANDMARKPATH1, TRAILPATH1);
		Map<Landmark, Integer> map = t.getDistancesToDestinations("L02");
		
		assertEquals(0, map.get(t.getLandmarkByID("L02")));
		assertEquals(3013, map.get(t.getLandmarkByID("L01")));
		assertEquals(3613, map.get(t.getLandmarkByID("L10")));
		assertEquals(4059, map.get(t.getLandmarkByID("L03")));
		assertEquals(4192, map.get(t.getLandmarkByID("L04")));
		assertEquals(6503, map.get(t.getLandmarkByID("L09")));
		assertEquals(8263, map.get(t.getLandmarkByID("L05")));
		assertEquals(9302, map.get(t.getLandmarkByID("L06")));
		assertEquals(12214, map.get(t.getLandmarkByID("L07")));
		assertEquals(14105, map.get(t.getLandmarkByID("L08")));
		
		assertEquals(0, t.getDistancesToDestinations("L40").size());
	}
	
	@Test
	void testGetLandmarkById() throws FileNotFoundException {
		TrailManager t = new TrailManager(LANDMARKPATH1, TRAILPATH1);
		
		assertEquals("Park Entrance", t.getLandmarkByID("L01").getDescription());
		assertEquals("Entrance Fountain", t.getLandmarkByID("L02").getDescription());
		assertEquals("Waste Station 1", t.getLandmarkByID("L03").getDescription());
		assertEquals("Entrance Restrooms", t.getLandmarkByID("L04").getDescription());
		assertEquals("Overlook 1", t.getLandmarkByID("L05").getDescription());
		assertEquals("Rock Formation 1", t.getLandmarkByID("L06").getDescription());
		assertEquals("Overlook 2", t.getLandmarkByID("L07").getDescription());
		assertEquals("Overlook Restrooms", t.getLandmarkByID("L08").getDescription());
		assertEquals("Waste Station 2", t.getLandmarkByID("L09").getDescription());
		assertEquals("Hidden Gardens", t.getLandmarkByID("L10").getDescription());
		assertNull(t.getLandmarkByID("L20"));
	}
	
	@Test
	void testGetProposedFirstAidLocations() throws FileNotFoundException {
		TrailManager t = new TrailManager(LANDMARKPATH1, TRAILPATH1);
		Map<Landmark, List<Trail>> map = t.getProposedFirstAidLocations(2);
		assertEquals(3, map.get(t.getLandmarkByID("L01")).size());
		assertEquals(2, map.get(t.getLandmarkByID("L02")).size());
		assertEquals(2, map.get(t.getLandmarkByID("L03")).size());
		assertEquals(2, map.get(t.getLandmarkByID("L04")).size());
		assertEquals(2, map.get(t.getLandmarkByID("L05")).size());
		assertEquals(2, map.get(t.getLandmarkByID("L06")).size());
		assertEquals(2, map.get(t.getLandmarkByID("L07")).size());
	}
}
