package com.yellowtaxipipeline;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import java.time.*;
import java.time.format.*;

/**
 * Hello world!
 *
 */
public class App {
	public static HashMap<String, HashMap<String, HashMap<Integer, HashMap<String, Integer>>>> crashData = new HashMap<String, HashMap<String,HashMap<Integer,HashMap<String,Integer>>>>();
	public static List<TaxiZoneLookup> taxiZoneLookupData = null;
	public static List<WeatherData> weatherDataList = null;
	public static List<String> cityNames = null;
	public static void main(String[] args) {
//		System.out.println(new Date());
//		crashData = readCrashData();
//		System.out.println(new Date());
//		System.out.println("Crash count" + crashData);
//		
		taxiZoneLookupData = readTaxiZoneLookup();
		System.out.println("Taxi Lookup count" + taxiZoneLookupData.size());
		cityNames = taxiZoneLookupData.stream().map(taxi -> taxi.getBorough()).distinct()
				.collect(Collectors.toList());
		cityNames.remove(cityNames.size()-1);
		weatherDataList = readWeatherData(cityNames);

//		System.out.println("WeatherData" + weatherDataList.toString());
//		thread(new Producer(), false);

		thread(new Consumer("crash"), false);
		thread(new Consumer("dataset"), false);
	}

	public static void thread(Runnable runnable, boolean daemon) {

		Thread brokerThread = new Thread(runnable);
		brokerThread.setDaemon(daemon);
		brokerThread.start();
	}

	private static List<TaxiZoneLookup> readTaxiZoneLookup() {
		List<TaxiZoneLookup> taxiZoneLookupData = new ArrayList<TaxiZoneLookup>();
		try {
			Map<String, String> mapping = new HashMap<String, String>();
			mapping.put("LocationID", "locationID");
			mapping.put("Borough", "borough");
			mapping.put("Zone", "zone");
			mapping.put("service_zone", "serviceZone");
			// HeaderColumnNameTranslateMappingStrategy<TaxiZoneLookup> strategy = new
			// HeaderColumnNameTranslateMappingStrategy<TaxiZoneLookup>();
			HeaderColumnNameTranslateMappingStrategy<TaxiZoneLookup> strategy = new HeaderColumnNameTranslateMappingStrategy<TaxiZoneLookup>();
			strategy.setType(TaxiZoneLookup.class);
			strategy.setColumnMapping(mapping);
			CSVReader csvReader = null;

			csvReader = new CSVReader(new FileReader("data/taxi+_zone_lookup.csv"));

			CsvToBean<TaxiZoneLookup> csvToBean = new CsvToBean<TaxiZoneLookup>();
			csvToBean.setMappingStrategy(strategy);
			csvToBean.setCsvReader(csvReader);
			taxiZoneLookupData = csvToBean.parse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taxiZoneLookupData;
	}

	private static HashMap<String, HashMap<String, HashMap<Integer, HashMap<String, Integer>>>> readCrashData() {
		HashMap<String, HashMap<String, HashMap<Integer, HashMap<String, Integer>>>> crashDetails = new HashMap<String, HashMap<String, HashMap<Integer, HashMap<String, Integer>>>>();
		try {

			Map<String, String> mapping = new HashMap<String, String>();
			mapping.put("COLLISION_ID", "collisionId");
			mapping.put("CRASH DATE", "crashDate");
			mapping.put("CRASH TIME", "crashTime");
			mapping.put("BOROUGH", "borough");
			mapping.put("ZIP CODE", "zipCode");
			mapping.put("LATITUDE", "latitude");
			mapping.put("LONGITUDE", "longitude");
			mapping.put("CONTRIBUTING FACTOR VEHICLE 1", "contributingFactor");
			HeaderColumnNameTranslateMappingStrategy<CrashData> strategy = new HeaderColumnNameTranslateMappingStrategy<CrashData>();
			strategy.setType(CrashData.class);
			strategy.setColumnMapping(mapping);
			CSVReader csvReader = null;

			csvReader = new CSVReader(new FileReader("data/1.csv"));

			CsvToBean<CrashData> csvToBean = new CsvToBean<CrashData>();
			csvToBean.setMappingStrategy(strategy);
			csvToBean.setCsvReader(csvReader);
			Iterator<CrashData> csvUserIterator = csvToBean.iterator();
			CrashData crashData = null;

			while (csvUserIterator.hasNext()) {
				crashData = csvUserIterator.next();
//				System.out.println(crashData.toString());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy H:mm");
//				System.out.println(crashData.getCrashDate() + " " + crashData.getCrashTime());
				LocalDateTime dateLimit = LocalDateTime.parse(crashData.getCrashDate() + " " + crashData.getCrashTime(),
						formatter);
				// <hour, <zone, count>
				if (dateLimit
						.isAfter(LocalDateTime.parse("31/12/2017 0:00", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")))
						&& dateLimit.isBefore(
								LocalDateTime.parse("1/2/2018 0:00", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")))) {
					// String hour = String.valueOf(LocalTime.parse(crashData.getCrashTime(),
					// DateTimeFormatter.ofPattern("H:mm")).getHour());
					String crashKey = dateLimit.format(DateTimeFormatter.ofPattern("d/M/yyyy H"));
					if (crashDetails.containsKey(crashKey)) {
						HashMap<String, HashMap<Integer, HashMap<String, Integer>>> hourData = crashDetails.get(crashKey);
						if (hourData.containsKey(crashData.getBorough())) {
							HashMap<Integer, HashMap<String, Integer>> crash = hourData.get(crashData.getBorough());
							int count =crash.keySet().iterator().next() + 1;
							HashMap<String, Integer> reason = crash.get(crash.keySet().iterator().next());
							if (reason.containsKey(crashData.getContributingFactor())){
								reason.put(crashData.getContributingFactor(), reason.get(crashData.getContributingFactor())+1);
							}
							else
								reason.put(crashData.getContributingFactor(), 1);
							crash = new HashMap<Integer, HashMap<String,Integer>>();
							crash.put(count, reason);
							hourData.put(crashData.getBorough(), crash);
						} else {
							HashMap<Integer, HashMap<String, Integer>> crash = new HashMap<Integer, HashMap<String,Integer>>();
							HashMap<String, Integer> crashReason = new HashMap<String, Integer>();
							crashReason.put(crashData.getContributingFactor(), 1);
							crash.put(1, crashReason);
							hourData.put(crashData.getBorough(),crash);
						}
						crashDetails.put(crashKey, hourData);
					} else {

						HashMap<String, HashMap<Integer, HashMap<String, Integer>>> hourData = new HashMap<String, HashMap<Integer,HashMap<String,Integer>>>();
						HashMap<Integer, HashMap<String, Integer>> crash = new HashMap<Integer, HashMap<String,Integer>>();
						HashMap<String, Integer> reason = new HashMap<String, Integer>();
						reason.put(crashData.getContributingFactor(),1);
						crash.put(1, reason);
						hourData.put(crashData.getBorough(), crash);
						crashDetails.put(crashKey, hourData);
					}
				}
			}
			System.out.println(crashDetails.toString());

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return crashDetails;
	}

	private static List<WeatherData> readWeatherData(List<String> cityNames) {
		List<WeatherData> taxiZoneLookupData = new ArrayList<WeatherData>();
		try {
			Map<String, String> mapping = new HashMap<String, String>();
			mapping.put("location", "location");
			mapping.put("date_time", "datetime");
			mapping.put("tempC", "temperature");
			mapping.put("FeelsLikeC", "feelsLike");
			mapping.put("windspeedKmph", "windSpeed");
			mapping.put("winddirDegree", "windDirectionDegree");
			mapping.put("humidity", "humidity");
			mapping.put("visibility", "visibility");
			mapping.put("pressure", "pressure");
			mapping.put("DewPointC", "dewPoint");
			HeaderColumnNameTranslateMappingStrategy<WeatherData> strategy = new HeaderColumnNameTranslateMappingStrategy<WeatherData>();
			strategy.setType(WeatherData.class);
			strategy.setColumnMapping(mapping);
			CSVReader csvReader = null;
			for(String city: cityNames) {
				try {
				csvReader = new CSVReader(new FileReader("weather/" + city + ".csv"));

				CsvToBean<WeatherData> csvToBean = new CsvToBean<WeatherData>();
				csvToBean.setMappingStrategy(strategy);
				csvToBean.setCsvReader(csvReader);
				taxiZoneLookupData.addAll(csvToBean.parse());
				}
				catch(FileNotFoundException ex) {
					ex.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error");
		}
		return taxiZoneLookupData;
	}

}
