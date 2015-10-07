package com.adtheorent.UnitTesting.Testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.adtheorent.UnitTesting.Utils.Gzip;

public class ETL {
	public static boolean processZippedFile() throws IOException{
		Gzip gzip = new Gzip();
		File mainDirectory = new File ("C:\\temp\\ETL\\");
		File [] listFiles = mainDirectory.listFiles();
		
		for(File file : listFiles){
			String outputFile = getFileName(file.getAbsolutePath());
			gzip.gunzipIt(file.getAbsolutePath(), outputFile); 
			generateRequestFile (outputFile);
		}
		return true;
	}
	
	public static String getFileName(String inputFileName){
		final Pattern p = Pattern.compile( "(.*?)\\." );
		final Matcher m = p.matcher( inputFileName );
		String output = "";
		int count =0; 
		
		while(m.find()){
			count++; 
			//System.out.println(m.group(1));
			output = output + m.group(1);
			if(count==1){
				output = output + ".";
			}
		}
		
		return output; 
	}
	
	public static void generateRequestFile(String FilePath) throws IOException{
		//System.out.println("Reading file now from C:\\Temp..........");
		File fileDirectory = new File(FilePath);
		File [] files = fileDirectory.listFiles();
		
		ArrayList <String> list = new ArrayList<String>(); 
		
		Scanner scanner = new Scanner(fileDirectory);
		String temp []; 
		String record; 
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			temp = line.split(",");
			record = temp[2];
			if(list.contains(record)){
				System.out.println("Dup! [" + record + "]");
			}else{
				list.add(record);
			}
		}
		
		System.out.println("Done processing File [" + FilePath + "]");
	}
}
