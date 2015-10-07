package com.adtheorent.UnitTesting.Utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class Gzip {
	public void gunzipIt(String inputFile, String outputFile){
		 
	     byte[] buffer = new byte[1024];
	 
	     try{
	 
	    	 GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(inputFile));
	 
	    	 FileOutputStream out = new FileOutputStream(outputFile);
	 
	        int len;
	        while ((len = gzis.read(buffer)) > 0) {
	        	out.write(buffer, 0, len);
	        }
	 
	        gzis.close();
	    	out.close();
	 
	    	System.out.println("Done");
	 
	    }catch(IOException ex){
	       ex.printStackTrace();   
	    }
	} 
}
