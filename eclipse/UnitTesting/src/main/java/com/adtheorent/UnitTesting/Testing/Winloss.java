package com.adtheorent.UnitTesting.Testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.openrtb.validator.OpenRtbInputType;
import org.openrtb.validator.OpenRtbValidator;
import org.openrtb.validator.OpenRtbValidatorFactory;
import org.openrtb.validator.OpenRtbVersion;
import org.openrtb.validator.ValidationResult;

import com.adtheorent.UnitTesting.Utils.AdExchange;
import com.adtheorent.UnitTesting.Utils.OpenRTBValidator;
import com.adtheorent.UnitTesting.Utils.UID;

public class Winloss {
	private static int clickCount = 0, completeCount = 0; 
	
	public static void sendClicks(String numOfClicks) throws URISyntaxException, ClientProtocolException, IOException{
		int count =0; 
		while(count <= Integer.valueOf(numOfClicks)){
			String adId = UID.generateUID();
			HttpClient httpClient = HttpClientBuilder.create().build(); //Use
			URI uriClick = getURI("click", adId);
			URI uriComplete = getURI("complete", adId);
			HttpGet request = new HttpGet(uriClick);
			HttpResponse response = httpClient.execute(request);
			System.out.println("Click: " + response.getStatusLine().toString());
			
			request = new HttpGet(uriComplete);
			response = httpClient.execute(request);
			
			System.out.println("Complete: " + response.getStatusLine().toString());
			count++;
		}
	}

	public static void wordCountClickFile() throws FileNotFoundException{
		//Case 1 Counting the number of entries in CSV file
		System.out.println("Reading file now from C:\\Temp..........");
		File fileDirectory = new File("C:\\temp");
		File [] files = fileDirectory.listFiles();
		
		for(File file : files){
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if(line.contains("click")){
					clickCount++; 
				}else if(line.contains("complete")){
					completeCount++;
				}
			}
		}
		
		System.out.println("ClickCount: " + clickCount);
		System.out.println("CompleteCount: "+ completeCount);
	}
	
	private static URI getURI(String key, String adId) throws URISyntaxException{
		String action = "", redir = "";
		if(key.equals("click")){
			action = "click";
			redir = "http://www.adtheorent.com";
		}else if(key.equals("complete")){
			action = "complete";
			redir = "complete";
		}
		
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http")
				.setHost("uatclicks.adtheorent.com")
				.setPath("/Clicks")
				.setParameter("adExchange", "MoPub")
				.setParameter("action", action)
				.setParameter("adId", adId)
				.setParameter("assetId", "10921")
				.setParameter("cId", "9103")
				.setParameter("crId", "33286")
				.setParameter("adThdId",
						"5F17DFFB-B8D5-488E-9CB3-1B6ADA060FBD")
				.setParameter("redir",redir);
		URI uri = builder.build();
		
		return uri;
	}

	//Sending Bids
	public static void sendBids(String IpAddress) throws ClientProtocolException, IOException{
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
	
	//Sending Wins
	
	//Sending Imps 
}
