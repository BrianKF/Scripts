package com.adtheorent.UnitTesting.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

public class Request {
	public static boolean generateRequestFile() throws IOException{
		Gzip gzip = new Gzip();
		File mainDirectory = new File ("C:\\temp\\ETL\\");
		File [] listFiles = mainDirectory.listFiles();
		
		//for(File file : listFiles){
			//String outputFile = getFileName(file.getAbsolutePath());
			//gzip.gunzipIt(file.getAbsolutePath(), outputFile); 
		processRequestFile ("C:\\Users\\Gabriel\\Desktop\\Nexage");
			
		//}
		return true;
	}
	
	public static void processRequestFile(String FilePath) throws IOException{
		//System.out.println("Reading file now from C:\\Temp..........");
		File fileDirectory = new File(FilePath);
		File [] files = fileDirectory.listFiles();
		
		ArrayList <String> list = new ArrayList<String>(); 
		
		for(File file : files){
			Scanner scanner = new Scanner(file);
			String temp []; 
			String record; 
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				record = line.replaceAll( "\"", "\"\"" );
				record = "\"" + record + "\""; 
				System.out.println(record);
				list.add(record);
			}
		}
		
		final File currentFile = new File( "C:\\temp\\request\\Nexage_New\\Nexage.csv" );
		FileUtils.touch( currentFile );

		// Write file with list of this Ad Excahnge's particular bid log message type.
		FileUtils.writeLines( currentFile, list );
		
		System.out.println("Done processing File [" + FilePath + "]");
	}
}
