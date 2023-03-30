package edu.ncsu.csc316.trail.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TrailReportUI {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String landmarkFile;
		File file;
		do {
			System.out.println("Enter the path to the file containing landmark information: ");
			landmarkFile = in.nextLine();
			file = new File(landmarkFile);
			try (Scanner scan = new Scanner(new FileInputStream(landmarkFile), "UTF8")) {
				
			} catch (FileNotFoundException e) {
				System.out.println("Invalid file path, please try again: ");
				continue;
			}
			
		} while (!file.exists());
		
		

		
	}
}
