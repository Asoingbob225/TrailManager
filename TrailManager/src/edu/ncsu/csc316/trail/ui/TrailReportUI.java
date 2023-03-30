package edu.ncsu.csc316.trail.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.ncsu.csc316.trail.manager.ReportManager;

public class TrailReportUI {
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(System.in);
		String landmarkFile;
		String trailFile;
		File file;
		System.out.println("Enter the path to the file containing landmark information: ");
		do {
			landmarkFile = in.nextLine();
			if (landmarkFile.equals("quit")) {
				in.close();
				System.exit(0);
			}
			file = new File(landmarkFile);
			try (Scanner scan = new Scanner(new FileInputStream(landmarkFile), "UTF8")) {
				break;
			} catch (FileNotFoundException e) {
				System.out.println("Invalid file path, please try again: ");
				continue;
			}
			
		} while (!file.exists());
		
		System.out.println("Enter the path to the file containing trail information: ");
		do {
			trailFile = in.nextLine();
			if (trailFile.equals("quit")) {
				in.close();
				System.exit(0);
			}
			file = new File(trailFile);
			try (Scanner scan = new Scanner(new FileInputStream(trailFile), "UTF8")) {
				break;
			} catch (FileNotFoundException e) {
				System.out.println("Invalid file path, please try again: ");
				continue;
			}
		} while (!file.exists());
		
		
		System.out.println("\n");
		
		ReportManager r = new ReportManager(landmarkFile, trailFile);
		
		System.out.println("Would you like a report of proposed first aid locations (enter \"first aid\") or a distance report (enter \"distance\")? ");
		String reportType = in.nextLine();

		if (reportType.equals("first aid")) {
			System.out.println("Please input the desired minimum number of intersecting trails: ");
			int numIntersect = Integer.parseInt(in.nextLine());
			System.out.println(r.getProposedFirstAidLocations(numIntersect));
		}
		else if (reportType.equals("distance")) {
			System.out.println("Please input the landmark id of the starting point: ");
			String id = in.nextLine();
			System.out.println(r.getDistancesReport(id));
		}
		else if (reportType.equals("quit")) {
			in.close();
			System.exit(0);
		}
		else {
			System.out.println("Invalid input.");
			in.close();
			System.exit(1);
		}
		
		in.close();
		System.exit(0);	
	}
}
