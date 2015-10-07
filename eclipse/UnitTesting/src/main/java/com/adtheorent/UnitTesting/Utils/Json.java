package com.adtheorent.UnitTesting.Utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {
	
	public static Click getClickFromJson(String incomingJson) throws JsonParseException, JsonMappingException, IOException{
		Click newClick = new Click(); 
		ObjectMapper m = null;
		if (m == null) {
			m = new ObjectMapper();
		}

		final JsonNode rootNode = m.readValue( incomingJson, JsonNode.class );
		System.out.println("JSON: " + incomingJson);
		String adId = rootNode.path("id").textValue();
		JsonNode seatBidNode = rootNode.path("seatbid"); 
		System.out.println(seatBidNode.path("bid").toString());
		String vast = seatBidNode.path("adm").textValue();
		String nurl = seatBidNode.path("nurl").textValue();
		
		System.out.println("Nurl: " + nurl);
		
		return newClick; 
	}
}
