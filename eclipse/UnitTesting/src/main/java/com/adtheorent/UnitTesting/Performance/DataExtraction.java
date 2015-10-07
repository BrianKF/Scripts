package com.adtheorent.UnitTesting.Performance;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.adtheorent.AdThCommon_Dex.concepts.AdExchange;
import com.adtheorent.AdThCommon_Dex.concepts.BidLogMessageType;
import com.adtheorent.UnitTesting.Utils.DEX.HoldData;


public class DataExtraction {
	
	private static Queue<HoldData> bidLogMessageQueue = new ConcurrentLinkedQueue();
	private static Map<AdExchange, Map<BidLogMessageType, List<String>>> fullBidLogMessageMap;
	
	public static void regexMapCreationTesting() throws FileNotFoundException{
		readRequestFile(); 
		
		initiFullBidLogRequestMap(); 
		
		long startTimeNew = System.currentTimeMillis(); 
		newBuildFullBidLogRequestMap(); 
		long endTimeNew = System.currentTimeMillis(); 
		
		readRequestFile();
		
		initiFullBidLogRequestMap(); 
		long startTimeCurrent = System.currentTimeMillis(); 
		currentBuildFullBidLogRequestMap(); 
		long endTimeCurrent = System.currentTimeMillis();
		
		System.out.println("Current Process: [" + (endTimeCurrent - startTimeCurrent) + "] New Process: [" + (endTimeNew - startTimeNew) + "]" );
		
	}
	
	//PART OF TESTING FOR TIMESTAMP
	public static String regexDateTesting() throws ParseException{
		int count =0; 
		ArrayList <String> dates = new ArrayList<String>();
		ArrayList <String> records = new ArrayList<String>();
		while (count < 1000){
			try {
				String date = getRandomDate();
				dates.add(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count++;
		}
		
		String key = "Bid|Rubicon|9094|33253|2015_07_06_15_34|0|ip-10-0-0-212.ec2.internal|";
		for(String temp : dates){
			records.add("MoPub,08d95a8f-7c71-4635-b2a9-46306ad9327a,0e94c130-1f51-11e5-84c5-12e3f67c4a3f,"+temp+",320,49,4,SITE,Zynga,eb99c87f16d44af380cca10c8691f91a,Cool/games.com,Cool/'games.com,,,IAB1|IAB9|IAB9-30|entertainment|games,,152.10.0.66,USA,517,NC,BOONE,28608,MCNC,Mozilla/5.0 (iPhone; CPU iPhone OS 8_1 like Mac OS X) Apple/WebKit/600.1.4 (KHTML| like Gecko) Mobile/12B411,en,AMERICA/NEW_YORK,iOS,Apple,iphone 5s (gsm),8.1,0.0,0.0,,1,,,IAB7-39|IAB9-7|IAB9-9|IAB9-25|IAB25|IAB25-2|IAB26|IAB3-7,audible.com|bigfish.com|bigfishgames.com|boombeach.com|candycrushsodasaga|clashofclans.com|classesusa.com|cm.best-thing.eu|doubledown|doubledowncasino.com|doubledown.com|doubledowninteractive.com|etermax.com|funstage.com|gsn|king.com|meethue.com|netflix.com|pg.com|philips.com|playtika|playtika.com|pokerstars.com|sprint.com|storm8.com|supercell.com|supercell.net|tapslots.com|triviacrack|uptodatecontent.net|wsop.com,agltb3B1Yi1pbmNyDAsSA0FwcBjC6LMUDA,1,ce64c870119c4b13b72a3dbaa3b4d008,,,,z_impression_id:539D2F6E-E577-4848-8D92-7823372B6626|z_slot_name:MOB_WWF2_BAN|z_min_version:4.1.5,0,0,,3315231,,B0081844-2A2E-4BCA-9295-29B39DA7F087,3|5,1.18,QA_Application_Health_Check,Test_Rubicon_Custom Markup,Banner,0,10.0,0,0,3315231,0.3170369561513702,0,0,0.0,0,,0,,,,0.0,,I,0,0,0.0,2,9094,33253,,,,idfa,mopub,Banner,0,,,0,,,,0,0,BUSINESS,0.0,0,1|2|3|8|9|10|13|14|6,");
		}
		
		long startTimeCurrent = System.currentTimeMillis();
		for(String temp: records){
			currentDateProcess(temp,key);
		}
		long endTimeCurrent = System.currentTimeMillis(); 
		
		
		
		long startTimeNew = System.currentTimeMillis();
		for(String temp: records){
			newDateProcess(temp,key);
		}
		long endTimeNew = System.currentTimeMillis(); 
		System.out.println("Current Process: [" + (endTimeCurrent - startTimeCurrent) + "] New Process: [" + (endTimeNew - startTimeNew) + "]" );
		return "done";
	}
	
	private static String getRandomDate() throws ParseException{
		final DecimalFormat mFormat = new DecimalFormat("00");
		GregorianCalendar gc = new GregorianCalendar();
		int year = randBetween(1900, 2015);
		gc.set(gc.YEAR, year);
		int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
		gc.set(gc.DAY_OF_YEAR, dayOfYear);
		int hourOfDay = randBetween(1, gc.getActualMaximum(gc.HOUR_OF_DAY));
		gc.set(gc.HOUR_OF_DAY, hourOfDay);
		int minOfHour = randBetween(1,  gc.getActualMaximum(gc.MINUTE));
		gc.set(gc.MINUTE, minOfHour);
		int second = randBetween(1,  gc.getActualMaximum(gc.SECOND));
		gc.set(gc.SECOND, second);
		int mills = randBetween(1,  gc.getActualMaximum(gc.MILLISECOND));
		gc.set(gc.MILLISECOND, mills);
		String newDate = gc.get(gc.YEAR) + "-" + mFormat.format(gc.get(gc.MONTH)) + "-" + mFormat.format(gc.get(gc.DAY_OF_MONTH)) + " " + mFormat.format(gc.get(gc.HOUR_OF_DAY))+":" 
				+ mFormat.format(gc.get(gc.MINUTE))+":"+mFormat.format(gc.get(gc.SECOND))+":"+ mFormat.format(gc.get(gc.MILLISECOND));
		return newDate;
		
		

	}

	public static int randBetween(int start, int end) {
		return start + (int)Math.round(Math.random() * (end - start));
	}
	
	private static String newDateProcess( final String rawBidLogMessage, final String shardPartitionKey ) throws ParseException {
		final String[] dateSplit = rawBidLogMessage.split( "\\," );
		String bidLogMessageDate;

		if (shardPartitionKey.contains( "Imp" )) {
			bidLogMessageDate = dateSplit[ 13 ];
		}else if(shardPartitionKey.contains( "Click" )){
			bidLogMessageDate = dateSplit[0];
		}else if (shardPartitionKey.contains( "Bid" )) {
			bidLogMessageDate = dateSplit[ 3 ];
		} else if (shardPartitionKey.contains( "Win" )) {
			bidLogMessageDate = dateSplit[ 2 ];
		} else {
			throw new IllegalStateException( "IllegalStateException: shard partition key [" + shardPartitionKey
				+ "] does not contain a valid Bid Log Message Type." );
		}
		//2015_04_07_19_39
		String date = bidLogMessageDate.replaceAll("-", "_").replaceFirst(" ", "_").replaceFirst(":", "_");
		date = date.substring(0, date.indexOf(":"));
		return date;
	}
	
	private static String currentDateProcess( final String rawBidLogMessage, final String shardPartitionKey ) {
		final String[] dateSplit = rawBidLogMessage.split( "\\," );
		String dateArray[];
		String bidLogMessageDate;

		if (shardPartitionKey.contains( "Imp" )) {
			bidLogMessageDate = dateSplit[ 13 ];
		} else if(shardPartitionKey.contains( "Click" )){
			bidLogMessageDate = dateSplit[0];
		}
		else if (shardPartitionKey.contains( "Bid" )) {
			bidLogMessageDate = dateSplit[ 3 ];
		} else if (shardPartitionKey.contains( "Win" )) {
			bidLogMessageDate = dateSplit[ 2 ];
		} else {
			throw new IllegalStateException( "IllegalStateException: shard partition key [" + shardPartitionKey
				+ "] does not contain a valid Bid Log Message Type." );
		}
		// LOG.warn( "Incoming Date: " + bidLogMessageDate );
		if (bidLogMessageDate.contains( "-" )) {
			dateArray = bidLogMessageDate.split( "-" );
		} else {
			dateArray = bidLogMessageDate.split( "\\/" );
		}
		final StringBuilder formattedDateBuilder = new StringBuilder();
		formattedDateBuilder.append( dateArray[ 0 ] ).append( "_" ).append( dateArray[ 1 ] ).append( "_" );
		dateArray = dateArray[ 2 ].split( " " );
		formattedDateBuilder.append( dateArray[ 0 ] );

		final int position = bidLogMessageDate.indexOf( " " );
		final String timePortion = bidLogMessageDate.substring( position, bidLogMessageDate.length() ).trim();
		dateArray = timePortion.split( "\\:" );
		formattedDateBuilder.append( "_" ).append( dateArray[ 0 ] ).append( "_" ).append( dateArray[ 1 ] );

		// LOG.warn( "Final Date: " + formattedDateBuilder.toString() );
		return formattedDateBuilder.toString().intern();
	}
	//END METHODS FOR TESTING TIMESTAMP
	
	//PART OF TESTING MAP CREATION 
	private static void newBuildFullBidLogRequestMap() {
		HoldData currentBidLogMessage = bidLogMessageQueue.poll();
		final Pattern p = Pattern.compile( "(.*?)\\|" );
		while ( currentBidLogMessage != null ) {
			String bidLogMessageTypeString = "", adExchangeString = "";

			// Key structure: BidLogMessageType | AdExchange | CampaignId | CreativeId | KinesisShardId | date |
			final Matcher m = p.matcher( currentBidLogMessage.getKey() );

			int count = 0;
			while ( m.find() && count != 2 ) {
				count++;
				if (count == 1) {
					bidLogMessageTypeString = m.group( 1 ).trim().toUpperCase();
				} else if (count == 2) {
					adExchangeString = m.group( 1 ).trim().toUpperCase();
				}
			}

			if (StringUtils.isBlank( bidLogMessageTypeString )) {
				System.err.println( "Error: current record's Bid Log Message Type is blank, skipping" );
			} else if (StringUtils.isBlank( adExchangeString )) {
				System.err.println( "Error: current record's Ad Exchange is blank, skipping" );
			} else {
				// Add this record into the appropriate place in the compound map.
				fullBidLogMessageMap.get( AdExchange.valueOf( adExchangeString ) ).get(
					BidLogMessageType.valueOf( bidLogMessageTypeString ) ).add(
						currentBidLogMessage.getRecord() );
			}
			currentBidLogMessage = bidLogMessageQueue.poll();
		}
	}
	
	private static void initiFullBidLogRequestMap() {
		fullBidLogMessageMap = new HashMap();

		for ( final AdExchange currentAdExchange : AdExchange.values() ) {
			final Map<BidLogMessageType, List<String>> currentBidLogMessageTypeMap = new HashMap();
			fullBidLogMessageMap.put( currentAdExchange, currentBidLogMessageTypeMap );

			for ( final BidLogMessageType currentBidLogMessageType : BidLogMessageType.values() ) {
				currentBidLogMessageTypeMap.put( currentBidLogMessageType, new ArrayList<String>() );
			}
		}
	}
	
	private static void readRequestFile() throws FileNotFoundException {
		String key = "Bid|Rubicon|9094|33253|2015_07_06_15_34|0|ip-10-0-0-212.ec2.internal|";
		File file = new File ("C:\\temp\\BidLogData.csv");
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			HoldData temp = new HoldData(key, line); 
			bidLogMessageQueue.add(temp);
		}
		scanner.close();
	}
	
	private static void currentBuildFullBidLogRequestMap() {
		HoldData currentBidLogMessage = bidLogMessageQueue.poll();
		while ( currentBidLogMessage != null ) {
			final String key = currentBidLogMessage.getKey();
			final String rawBidLogMessage = currentBidLogMessage.getRecord();

			// Key structure: BidLogMessageType | AdExchange | CampaignId | CreativeId | KinesisShardId | date |
			final String[] splitKey = key.split( "\\|" );

			final String bidLogMessageTypeString = splitKey[ 0 ].trim().toUpperCase();
			final String adExchangeString = splitKey[ 1 ].trim().toUpperCase();

			if (StringUtils.isBlank( bidLogMessageTypeString )) {
				System.err.println( "Error: current record's Bid Log Message Type is blank, skipping" );
			} else if (StringUtils.isBlank( adExchangeString )) {
				System.err.println( "Error: current record's Ad Excahnge is blank, skipping" );
			} else {
				final BidLogMessageType bidLogMessageTypeEnum = BidLogMessageType.valueOf( bidLogMessageTypeString );
				final AdExchange adExchangeEnum = AdExchange.valueOf( adExchangeString );

				// Add this record into the appropriate place in the compound map.
				fullBidLogMessageMap.get( adExchangeEnum ).get( bidLogMessageTypeEnum ).add( rawBidLogMessage );
			}
			currentBidLogMessage = bidLogMessageQueue.poll();
		}
	}
	
	
	//END METHODS FOR TESTING MAP CREATION

}
