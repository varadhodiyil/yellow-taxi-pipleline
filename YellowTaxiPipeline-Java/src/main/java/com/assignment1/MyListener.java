package com.assignment1;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQBytesMessage;

import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

public class MyListener implements MessageListener {

	public void onMessage(Message message) {
		
		try {
			
			// Taxi zone lookup
			
//			for (TaxiZoneLookup yellowTripData : taxiZoneLookupData) {
//				System.out.println("Received: " +Thread.currentThread().getName() + yellowTripData.toString());
//						
//			}
			List<CrashData> crashData = null;
			crashData = readCrashData();
			System.out.println("Crash count" + crashData.size());
			List<TaxiZoneLookup> taxiZoneLookupData = readTaxiZoneLookup();
			System.out.println("Taxi Lookup count" + taxiZoneLookupData.size());
			
			//Reading trip details from Producer
			if (message instanceof TextMessage)
			{
				TextMessage textMessage = (TextMessage) message;
				String text = textMessage.getText();
				Gson gson = new Gson();
//				System.out.println(text);
				YellowTripData[] YellowTripDataArray = gson.fromJson(text, YellowTripData[].class);
				System.out.println(YellowTripDataArray.length);
				for (YellowTripData yellowTripData : YellowTripDataArray) {
					System.out.println("Received: " +Thread.currentThread().getName() + yellowTripData.toString());
				}
//				Datetime startTime
				
			} else {
				ActiveMQBytesMessage bytesMessage = (ActiveMQBytesMessage) message;
	            String messageText = new String(bytesMessage.getContent().data);
//				System.out.println("Received: " + messageText);
				Gson gson = new Gson();
				YellowTripData[] YellowTripDataArray = gson.fromJson(messageText, YellowTripData[].class);
				System.out.println(YellowTripDataArray.length);
//				for (YellowTripData yellowTripData : YellowTripDataArray) {
//					System.out.println("Received: " +Thread.currentThread().getName() + yellowTripData.toString());
//							
//				}
			}

		}
		catch (JMSException e)
		{
			e.getStackTrace();
		}
		
	}

	private List<TaxiZoneLookup> readTaxiZoneLookup() {
		Map<String, String> mapping = new HashMap<String, String>();
		mapping.put("LocationID", "locationID");
		mapping.put("Borough", "borough");
		mapping.put("Zone", "zone");
//		mapping.put("service_zone", "serviceZone");
		HeaderColumnNameTranslateMappingStrategy<TaxiZoneLookup> strategy = new HeaderColumnNameTranslateMappingStrategy<TaxiZoneLookup>();
		strategy.setType(TaxiZoneLookup.class);
		strategy.setColumnMapping(mapping);
		CSVReader csvReader = null;
		try {
			csvReader = new CSVReader(new FileReader("data/taxi+_zone_lookup_sample.csv"));
		} catch (FileNotFoundException e) {

			// TODO Auto-generated catch bl/ock
			e.printStackTrace();
		}

		CsvToBean<TaxiZoneLookup> csvToBean = new CsvToBean<TaxiZoneLookup>();

		// call the parse method of CsvToBean
		// pass strategy, csvReader to parse method
		@SuppressWarnings("deprecation")
		List<TaxiZoneLookup> taxiZoneLookupData = csvToBean.parse(strategy, csvReader);
		return taxiZoneLookupData;
	}
	
	private List<CrashData> readCrashData() {
		Map<String, String> mapping = new HashMap<String, String>();
		mapping.put("COLLISION_ID", "collisionId");
		mapping.put("CRASH DATE", "crashDate");
		mapping.put("CRASH TIME", "crashTime");
		mapping.put("BOROUGH", "borough");
		mapping.put("ZIP CODE", "zipCode");
		mapping.put("LATITUDE", "latitude");
		mapping.put("LONGITUDE", "longitude");
		mapping.put("ON STREET NAME", "onStreetName");
		mapping.put("CROSS STREET NAME", "crossStreetName");
		mapping.put("OFF STREET NAME", "offStreetName");
//		mapping.put("service_zone", "serviceZone");
		HeaderColumnNameTranslateMappingStrategy<CrashData> strategy = new HeaderColumnNameTranslateMappingStrategy<CrashData>();
		strategy.setType(CrashData.class);
		strategy.setColumnMapping(mapping);
		CSVReader csvReader = null;
		try {
			csvReader = new CSVReader(new FileReader("data/Motor_Vehicle_Collisions_-_Crashes.csv"));
		} catch (FileNotFoundException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CsvToBean<CrashData> csvToBean = new CsvToBean<CrashData>();

		// call the parse method of CsvToBean
		// pass strategy, csvReader to parse method
		@SuppressWarnings("deprecation")
		List<CrashData> crashData = csvToBean.parse(strategy, csvReader);
		return crashData;
	}

}
