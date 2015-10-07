package com.adtheorent.UnitTesting.Testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Scanner;

import org.apache.http.client.ClientProtocolException;

import com.adtheorent.UnitTesting.Performance.DataExtraction;
import com.adtheorent.UnitTesting.Utils.Aws;
import com.adtheorent.UnitTesting.Utils.Request;
import com.adtheorent.UnitTesting.Utils.Platform.*;

/**1
 *1
 * Hello world!
 *
 */
public class UnitTesting 
{
    public static void main( String[] args ) throws ClientProtocolException, URISyntaxException, IOException, ParseException
    {
    	int clickCount = 0, completeCount =0; 
    	Scanner keyboard = new Scanner(System.in);
    	System.out.println("Please enter what you would like to do:");
    	System.out.println("1. Send Requests to UAT");
    	System.out.println("2. Send Clicks to UAT");
    	System.out.println("3. Send Imps to UAT");
    	System.out.println("4. Send Wins to UAT");
    	
    	String userInput = keyboard.next();
    	
    	//Case 1 Send Requests to UAT
    	if(userInput.equals("1")){
    		System.out.println("Please Enter the IpAddress of the Bidder you would like to use: ");
    		String IpAddress = "10.0.0.89";//keyboard.next();
    		OverAll.sendEverything(IpAddress);
    		//Winloss.sendBids(IpAddress);
    	}
    	//Case 2 Sending Clicks to UAT  
    	if(userInput.equals("2")){
    		System.out.println("Please Enter How Many Clicks you want to send:");
    		String numOfClicks = keyboard.next();
    		Winloss.sendClicks(numOfClicks);
    	}
    	//Case 3 Sending Bids to UAT
//    	if(userInput.equals("3")){
//    		System.out.println("Please enter the AdExchange you would like to test:");
//    		String AdExchange = keyboard.next();
//    		Winloss.sendBids(AdExchange);
//    	}
    	//Case 4 Generate GUID File For Clicks, Wins, and Impression Testing
    	if(userInput.equals("4")){
    		GenerateGUID.generateGUIDFile();
    	}
    	//Case 4 Sending Wins to UAT 
    	
    	//Case 5 Sending Impression to UAT 
    	
    	//Case 6 Running ETL Process Test 
    	if(userInput.equals("6")){
    		ETL.processZippedFile();
    	}
    	
    	//Case 7 Performance Testing
    	if(userInput.equals("7")){
    		System.out.println("Please enter what product you want to test: ");
    		System.out.println("1. Date Format Testing");
    		System.out.println("2. Map Creation Testing");
    		String productNumber = keyboard.next();
    		int count = 0; 
    		if(productNumber.equals("1")){
	    		while(count <50){
	    			DataExtraction.regexDateTesting();
	    			count++; 
	    		}
    		}
    		else{
	    		while (count < 50){
	    			DataExtraction.regexMapCreationTesting();
	    			count++;
	    		}
    		}
    	}
    	
    	//Case 8 Generating DE File
    	if(userInput.equals("8")){
    		System.out.println("Starting DE FILE Merge Process");
    		Aws.s3Login();
    		DEFileMerger temp = new DEFileMerger(Aws.s3Client, new com.adtheorent.UnitTesting.Utils.Platform.MergeFiles()); 
    		temp.runNow();
    	}
    	
    	//Case 9 Generating Request File
    	if(userInput.equals("9")){
    		System.out.println("Starting Request File Generation Process");
    		Request.generateRequestFile();
    	}
    	
    }
}
