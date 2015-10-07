package com.adtheorent.UnitTesting.Testing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.adtheorent.UnitTesting.Utils.UID;

public class GenerateGUID {
	public static void generateGUIDFile() throws IOException{
		BufferedWriter bw = null; 
		try{
			File file = new File("C://temp//ClickId.csv");
			if(!file.exists()){
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsolutePath());
			bw = new BufferedWriter(fw);
		}catch(Exception e){
			e.printStackTrace();
		}
		int count = 0; 
		while (count < 10000){
			String guid = UID.generateUID();
			bw.write(guid);
			bw.newLine();
			count++;
		}
		bw.close();
		System.out.println("File has been generated [C:/temp/GUID.csv]");
	}
}
