package edu.ncsu.csc316.trail.manager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

class ReportManagerTest {
	
	private static final String LANDMARKPATH1 = "input/landmark_file1.txt";
	private static final String LANDMARKPATH2 = "input/landmark_file2.txt";
	private static final String TRAILPATH1 = "input/trail_file1.txt";

	@Test
	void testReportManager() {
		assertDoesNotThrow(() -> new ReportManager(LANDMARKPATH1, TRAILPATH1));
	}
	
	@Test
	void testGetDistancesReport() throws FileNotFoundException {
		ReportManager r = new ReportManager(LANDMARKPATH1, TRAILPATH1);
		ReportManager r2 = new ReportManager(LANDMARKPATH2, TRAILPATH1);
		assertEquals("The provided landmark ID (L14) is invalid for the park.", r.getDistancesReport("L14"));
		
		assertEquals("No landmarks are reachable from Campsite 2 (L13).", r2.getDistancesReport("L13"));
		
		String s = "Landmarks Reachable from Entrance Fountain (L02) {\n"
				+ "   3013 feet to Park Entrance (L01)\n"
				+ "   3613 feet to Hidden Gardens (L10)\n"
				+ "   4059 feet to Waste Station 1 (L03)\n"
				+ "   4192 feet to Entrance Restrooms (L04)\n"
				+ "   6503 feet (1.23 miles) to Waste Station 2 (L09)\n"
				+ "   8263 feet (1.56 miles) to Overlook 1 (L05)\n"
				+ "   9302 feet (1.76 miles) to Rock Formation 1 (L06)\n"
				+ "   12214 feet (2.31 miles) to Overlook 2 (L07)\n"
				+ "   14105 feet (2.67 miles) to Overlook Restrooms (L08)\n"
				+ "}";
		
		assertEquals(s, r.getDistancesReport("L02"));
	}
	
	@Test
	void testGetProposedFirstAidLocations() throws FileNotFoundException {
		ReportManager r = new ReportManager(LANDMARKPATH1, TRAILPATH1);
		assertEquals("Number of intersecting trails must be greater than 0.", r.getProposedFirstAidLocations(0));
		assertEquals("Number of intersecting trails must be greater than 0.", r.getProposedFirstAidLocations(-10));
		
		assertEquals("No landmarks have at least 4 intersecting trails.", r.getProposedFirstAidLocations(4));
		
		String s1 = "Proposed Locations for First Aid Stations {\n"
				+ "   Park Entrance (L01) - 3 intersecting trails\n"
				+ "}";
		String s2 = "Proposed Locations for First Aid Stations {\n"
				+ "   Park Entrance (L01) - 3 intersecting trails\n"
				+ "   Entrance Fountain (L02) - 2 intersecting trails\n"
				+ "   Entrance Restrooms (L04) - 2 intersecting trails\n"
				+ "   Overlook 1 (L05) - 2 intersecting trails\n"
				+ "   Overlook 2 (L07) - 2 intersecting trails\n"
				+ "   Rock Formation 1 (L06) - 2 intersecting trails\n"
				+ "   Waste Station 1 (L03) - 2 intersecting trails\n"
				+ "}";
		String s3 = "Proposed Locations for First Aid Stations {\n"
				+ "   Park Entrance (L01) - 3 intersecting trails\n"
				+ "   Entrance Fountain (L02) - 2 intersecting trails\n"
				+ "   Entrance Restrooms (L04) - 2 intersecting trails\n"
				+ "   Overlook 1 (L05) - 2 intersecting trails\n"
				+ "   Overlook 2 (L07) - 2 intersecting trails\n"
				+ "   Rock Formation 1 (L06) - 2 intersecting trails\n"
				+ "   Waste Station 1 (L03) - 2 intersecting trails\n"
				+ "   Campsite 1 (L11) - 1 intersecting trails\n"
				+ "   Campsite Restrooms (L12) - 1 intersecting trails\n"
				+ "   Hidden Gardens (L10) - 1 intersecting trails\n"
				+ "   Overlook Restrooms (L08) - 1 intersecting trails\n"
				+ "   Waste Station 2 (L09) - 1 intersecting trails\n"
				+ "}";
		
		assertEquals(s1, r.getProposedFirstAidLocations(3));
		assertEquals(s2, r.getProposedFirstAidLocations(2));
		assertEquals(s3, r.getProposedFirstAidLocations(1));
	}

}
