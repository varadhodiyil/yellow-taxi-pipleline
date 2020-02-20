package com.yellowtaxipipeline;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.yellowtaxipipeline.models.WeatherData;

/**
 * Hello world!
 *
 */
public class App {
	public static HashMap<String, HashMap<String, HashMap<Integer, HashMap<String, Integer>>>> crashData = new HashMap<String, HashMap<String, HashMap<Integer, HashMap<String, Integer>>>>();
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
		cityNames = taxiZoneLookupData.stream().map(taxi -> taxi.getBorough()).distinct().collect(Collectors.toList());
		cityNames.remove(cityNames.size() - 1);
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
			for (String city : cityNames) {
				try {
					csvReader = new CSVReader(new FileReader("weather/" + city + ".csv"));

					CsvToBean<WeatherData> csvToBean = new CsvToBean<WeatherData>();
					csvToBean.setMappingStrategy(strategy);
					csvToBean.setCsvReader(csvReader);
					taxiZoneLookupData.addAll(csvToBean.parse());
				} catch (FileNotFoundException ex) {
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
