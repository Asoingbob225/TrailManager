package edu.ncsu.csc316.trail.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.ncsu.csc316.trail.manager.ReportManager;

/**
 * This is the UI class for the TrailReport program. It prompts users on the
 * command line and carries out the proper functionality based on those prompts,
 * according to program's functional requirements.
 * 
 * @author Jimin Yu, jyu34
 *
 */
public class TrailReportUI {
	/**
	 * This is the main method of the UI class. It carries out program execution.
	 * 
	 * @param args an array of arguments to the command line
	 * @throws FileNotFoundException if the provided file-path does not exist
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// Initialize scanner
		Scanner in = new Scanner(System.in);
		// String storing landmark file path
		String landmarkFile;
		// String storing trail file path
		String trailFile;
		// Instance of file to make sure the file with the provided file path exists
		File file;
		System.out.println("Enter the path to the file containing landmark information: ");
		// Re-prompt the user for a valid file path until one is provided
		do {
			landmarkFile = in.nextLine();
			if ("quit".equals(landmarkFile)) {
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

		// Re-prompt the user for a valid file path until one is provided
		System.out.println("Enter the path to the file containing trail information: ");
		do {
			trailFile = in.nextLine();
			if ("quit".equals(trailFile)) {
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

		System.out.println(
				"Would you like a report of proposed first aid locations (enter \"first aid\") or a distance report (enter \"distance\")? ");
		String reportType = in.nextLine();

		if ("first aid".equals(reportType)) {
			System.out.println("Please input the desired minimum number of intersecting trails: ");
			int numIntersect = Integer.parseInt(in.nextLine());
			System.out.println(r.getProposedFirstAidLocations(numIntersect));
		} else if ("distance".equals(reportType)) {
			System.out.println("Please input the landmark id of the starting point: ");
			String id = in.nextLine();
			System.out.println(r.getDistancesReport(id));
		} else if ("quit".equals(reportType)) {
			in.close();
			System.exit(0);
		} else {
			System.out.println("Invalid input.");
			in.close();
			System.exit(1);
		}

		in.close();
		System.exit(0);
	}
}
