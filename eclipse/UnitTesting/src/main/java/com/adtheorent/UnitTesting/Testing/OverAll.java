package com.adtheorent.UnitTesting.Testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.openrtb.validator.OpenRtbInputType;
import org.openrtb.validator.OpenRtbValidator;
import org.openrtb.validator.OpenRtbValidatorFactory;
import org.openrtb.validator.OpenRtbVersion;
import org.openrtb.validator.ValidationResult;

import com.adtheorent.UnitTesting.Utils.Json;

public class OverAll {
	public static void sendEverything(String IpAddress) throws ClientProtocolException, IOException{
		String fileName = "src/main/resources/AdExchanges/";
		File fileDirectory = new File(fileName);
		File[] files = fileDirectory.listFiles();
		
		for(File file : files ){
			 ArrayList <String> output = new ArrayList<String>(); 
			 String [] AdExchangeName = file.getName().split("\\.");
			 String uri = "http://"+ IpAddress + ":9987/BidRequest/" + AdExchangeName[0];
			 HttpPost request = new HttpPost(uri);
			 System.out.println("Processing FileName [" + file.getName() + "]");
			 ArrayList <String> json = readRequestFile(file);
			 for(String currentJson : json){
				 output.add("Request: " + currentJson);
				 //System.out.println("Request: " + currentJson);
						
				 HttpClient httpClient = HttpClientBuilder.create().build(); //Use
			     StringEntity params =new StringEntity(currentJson);
				 request.addHeader("content-type", "application/json");
				 request.setEntity(params);
				 HttpResponse response = httpClient.execute(request);
				 
				 if(!response.getStatusLine().toString().contains("204")){
					 HttpEntity entity = response.getEntity();
					 String responseString = EntityUtils.toString(entity, "UTF-8");
					 responseString = responseString.replaceAll("deal_id", "dealid");
					 output.add("BidResponse: " + responseString); 
					 
					 //System.out.println("Reponse: " + responseString);
					 OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_RESPONSE, OpenRtbVersion.V2_2);
					 ValidationResult validationResult = validator.validate(responseString);
					 output.add("valid: " + validationResult.isValid() + ", result: " + validationResult.getResult());
					 Json.getClickFromJson(responseString);
					 //System.out.println("valid: " + validationResult.isValid() + ", result: " + validationResult.getResult());
					 
					 
				 }else{
					 output.add("NoBid Request: " + currentJson);
					 System.out.println("Bidder Returned back a no-bid for this file!");
				 }
			 }
			 System.out.println("\n-------------------------Report for " + AdExchangeName [0] + "--------------------------------");
			 for(String outputData : output){
				 System.out.println(outputData);
			 }
		}
	}
	
	
	private static ArrayList<String> readRequestFile(File inputFile)
			throws FileNotFoundException {
		ArrayList <String> json = new ArrayList<String>();
		Scanner scanner = new Scanner(inputFile);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			line = line.replaceAll("\"\"", "\"").replaceFirst("\"","").replace("}\"", "}");
			json.add(line);
		}
		
		scanner.close();
		return json;
	}
}
