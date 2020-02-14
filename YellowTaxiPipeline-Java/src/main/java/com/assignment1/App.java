package com.assignment1;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "hello" );
        thread(new Producer(), false);
        thread(new Consumer(), false);

		List<String[]> crashData = null;
		try {
			crashData = readCrashData();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Crash count" + crashData.size());
		List<TaxiZoneLookup> taxiZoneLookupData = readTaxiZoneLookup();
		System.out.println("Taxi Lookup count" + taxiZoneLookupData.size());
    }
    public static void thread(Runnable runnable, boolean daemon) {

		Thread brokerThread = new Thread(runnable);
		brokerThread.setDaemon(daemon);
		brokerThread.start();
	}
    
    private static List<TaxiZoneLookup> readTaxiZoneLookup() {
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
	
	private static List<String[]> readCrashData() throws FileNotFoundException {
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
//		CSVReader csvReader = null;
//		try {
//			csvReader = new CSVReader(new FileReader("data/1.csv"));
//		} catch (FileNotFoundException e) {
//
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		CsvToBean<CrashData> csvToBean = new CsvToBean<CrashData>();
//
//		// call the parse method of CsvToBean
//		// pass strategy, csvReader to parse method
//		@SuppressWarnings("deprecation")
//		List<CrashData> crashData = csvToBean.parse(strategy, csvReader);
		CsvParserSettings settings = new CsvParserSettings();
		//the file used in the example uses '\n' as the line separator sequence.
		//the line separator sequence is defined here to ensure systems such as MacOS and Windows
		//are able to process this file correctly (MacOS uses '\r'; and Windows uses '\r\n').
		settings.getFormat().setLineSeparator("\n");

		// creates a CSV parser
		CsvParser parser = new CsvParser(settings);

		// parses all rows in one go.
		List<String[]> allRows = parser.parseAll(new FileReader("data/1.csv"));
		return allRows;
	}

}
