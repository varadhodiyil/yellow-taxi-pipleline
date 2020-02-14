package com.assignment1;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

public class Producer implements Runnable {

	public void run() {

		try {

			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.1.12:61616");

			Connection connection = connectionFactory.createConnection();
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// Administrative object
			Destination dest = session.createTopic("dataset");

			MessageProducer producer = session.createProducer(dest);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
			TextMessage message = session.createTextMessage("Consumer");
			Map<String, String> mapping = new HashMap<String, String>();
			mapping.put("LocationID", "locationID");
			mapping.put("Borough", "borough");
			mapping.put("Zone", "zone");
			mapping.put("service_zone", "serviceZone");
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
			List<TaxiZoneLookup> list = csvToBean.parse(strategy, csvReader);

			// print details of Bean object
			Gson gson = new Gson();
			String sampleText = gson.toJson(list);
			message = session.createTextMessage(sampleText);
			System.out.println("sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
			producer.send(message);
//			for (TaxiZoneLookup data : list) {
//				String sampleText = gson.toJson(data);
//				message = session.createTextMessage(sampleText);
//				System.out.println("sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
//				producer.send(message);
//			}			
//			Map<String, String> mapping = new HashMap<String, String>();
//			mapping.put("VendorID", "vendorID");
//			mapping.put("tpep_pickup_datetime", "tpep_pickup_datetime");
//			mapping.put("tpep_dropoff_datetime", "tpep_dropoff_datetime");
//			mapping.put("passenger_count", "passenger_count");
//			mapping.put("trip_distance", "trip_distance");
//			mapping.put("RatecodeID", "RatecodeID");
//			mapping.put("store_and_fwd_flag", "store_and_fwd_flag");
//			mapping.put("PULocationID", "PULocationID");
//			mapping.put("DOLocationID", "DOLocationID");
//			mapping.put("payment_type", "payment_type");
//			mapping.put("fare_amount", "fare_amount");
//			mapping.put("extra", "extra");
//			mapping.put("mta_tax", "mta_tax");
//			mapping.put("tip_amount", "tip_amount");
//			mapping.put("tolls_amount", "tolls_amount");
//			mapping.put("improvement_surcharge", "improvement_surcharge");
//			mapping.put("total_amount", "total_amount");
//			HeaderColumnNameTranslateMappingStrategy<Yellow_TripData> strategy = new HeaderColumnNameTranslateMappingStrategy<Yellow_TripData>();
//			strategy.setType(Yellow_TripData.class);
//			strategy.setColumnMapping(mapping);
////			strategy.
//			CSVReader csvReader = null;
//			try {
//				csvReader = new CSVReader(new FileReader("yellow_tripdata_2018-01_sample.csv"));
//			} catch (Exception e) {
//
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			CsvToBean<Yellow_TripData> csvToBean = new CsvToBean<Yellow_TripData>();
//			
//			// call the parse method of CsvToBean
//			// pass strategy, csvReader to parse method
//			@SuppressWarnings({ "deprecation"})
//			List<Yellow_TripData> list = csvToBean.parse(strategy, csvReader);
//
//			// print details of Bean object
//			Gson gson = new Gson();
//			for (Yellow_TripData data : list) {
//				String sampleText = gson.toJson(data);
//				message = session.createTextMessage(sampleText);
//				System.out.println("sent message: " + message.hashCode() + " : " + Thread.currentThread().getName() + ":" + data.getVendorID());
//				producer.send(message);
//			}
			connection.close();

		} catch (JMSException e) {
			System.out.println("caught " + e);
			e.printStackTrace();
		}
	}
}
