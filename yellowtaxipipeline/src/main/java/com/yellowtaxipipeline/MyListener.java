package com.yellowtaxipipeline;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.apache.activemq.command.ActiveMQBytesMessage;
import com.google.gson.Gson;

public class MyListener implements MessageListener {
	private String topic;
	Producer producer;

	public MyListener(String topic) {
		super();
		this.topic = topic;
		producer = new Producer("dataset-trip");
		App.thread(producer, false);
	}

	public void onMessage(Message message) {

		try {
			if (topic.equals("dataset")) {
				YellowTripData yellowTripData = null;
				ActiveMQBytesMessage bytesMessage = (ActiveMQBytesMessage) message;
				String messageText = new String(bytesMessage.getContent().data);
				Gson gson = new Gson();
				yellowTripData = gson.fromJson(messageText, YellowTripData.class);
//				for (YellowTripData yellowTripData : YellowTripDataArray) {

				int crashCount = 0;
				HashMap<String, Integer> crashReasons = null;
				String puHour = LocalDateTime
						.parse(yellowTripData.getTpepPickupDatetime(),
								DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
						.format(DateTimeFormatter.ofPattern("d/M/yyyy H"));
				String doHour = LocalDateTime
						.parse(yellowTripData.getTpepDropoffDatetime(),
								DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
						.format(DateTimeFormatter.ofPattern("d/M/yyyy H"));
				Boolean crashExist = false;
				final YellowTripData y = yellowTripData;
				String puLocation = App.taxiZoneLookupData.stream()
						.filter(taxi -> taxi.getLocationID() == y.getpULocationID()).findAny().get().getBorough();
				String doLocation = App.taxiZoneLookupData.stream()
						.filter(taxi -> taxi.getLocationID() == y.getdOLocationID()).findAny().get().getBorough();
				if ((App.crashData != null && App.crashData.containsKey(puHour)) || 
						(App.crashData != null && App.crashData.containsKey(doHour))) {
					HashMap<String, HashMap<Integer, HashMap<String, Integer>>> hourData = App.crashData.get(puHour);

					if (hourData != null && hourData.containsKey(puLocation.toUpperCase())) {
						crashExist = true;
						crashCount = hourData.get(puLocation.toUpperCase()).keySet().iterator().next();
						crashReasons = hourData.get(puLocation.toUpperCase())
								.get(hourData.get(puLocation.toUpperCase()).keySet().iterator().next());
					}

				}
				WeatherData weather = Consumer.readWeatherInfo(puLocation, yellowTripData.getTpepPickupDatetime());
				weather = (weather != null) ? weather : new WeatherData();
				TripDatawithCrash tripDatawithCrash = new TripDatawithCrash(yellowTripData.getTpepPickupDatetime(),
						yellowTripData.getTpepDropoffDatetime(), puLocation, doLocation, crashExist, crashCount,
						crashReasons, weather.getTemperature(), weather.getFeelsLike(), weather.getWindSpeed(),
						weather.getWindDirectionDegree(), weather.getHumidity(), weather.getVisibility(),
						weather.getPressure(), weather.getDewPoint());
//				if(crashExist)
					System.out.println(tripDatawithCrash.toString());
				producer.send(tripDatawithCrash);
			} else if (topic.equals("crash")) {
				CrashData crashData = null;
				ActiveMQBytesMessage bytesMessage = (ActiveMQBytesMessage) message;
				String messageText = new String(bytesMessage.getContent().data);
				Gson gson = new Gson();
				crashData = gson.fromJson(messageText, CrashData.class);
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
					if (App.crashData.containsKey(crashKey)) {
						HashMap<String, HashMap<Integer, HashMap<String, Integer>>> hourData = App.crashData.get(crashKey);
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
						App.crashData.put(crashKey, hourData);
					} else {

						HashMap<String, HashMap<Integer, HashMap<String, Integer>>> hourData = new HashMap<String, HashMap<Integer,HashMap<String,Integer>>>();
						HashMap<Integer, HashMap<String, Integer>> crash = new HashMap<Integer, HashMap<String,Integer>>();
						HashMap<String, Integer> reason = new HashMap<String, Integer>();
						reason.put(crashData.getContributingFactor(),1);
						crash.put(1, reason);
						hourData.put(crashData.getBorough(), crash);
						App.crashData.put(crashKey, hourData);
					}
			
//					System.out.println(App.crashData.toString());
				}
			}

//			}			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

//	private List<TaxiZoneLookup> readTaxiZoneLookup() {
//		Map<String, String> mapping = new HashMap<String, String>();
//		mapping.put("LocationID", "locationID");
//		mapping.put("Borough", "borough");
//		mapping.put("Zone", "zone");
////		mapping.put("service_zone", "serviceZone");
//		HeaderColumnNameTranslateMappingStrategy<TaxiZoneLookup> strategy = new HeaderColumnNameTranslateMappingStrategy<TaxiZoneLookup>();
//		strategy.setType(TaxiZoneLookup.class);
//		strategy.setColumnMapping(mapping);
//		CSVReader csvReader = null;
//		try {
//			csvReader = new CSVReader(new FileReader("data/taxi+_zone_lookup_sample.csv"));
//		} catch (FileNotFoundException e) {
//
//			// TODO Auto-generated catch bl/ock
//			e.printStackTrace();
//		}
//
//		CsvToBean<TaxiZoneLookup> csvToBean = new CsvToBean<TaxiZoneLookup>();
//
//		// call the parse method of CsvToBean
//		// pass strategy, csvReader to parse method
//		@SuppressWarnings("deprecation")
//		List<TaxiZoneLookup> taxiZoneLookupData = csvToBean.parse(strategy, csvReader);
//		return taxiZoneLookupData;
//	}
//	
//	private List<CrashData> readCrashData() {
//		Map<String, String> mapping = new HashMap<String, String>();
//		mapping.put("COLLISION_ID", "collisionId");
//		mapping.put("CRASH DATE", "crashDate");
//		mapping.put("CRASH_TIME", "crashTime");
//		mapping.put("BOROUGH", "borough");
//		mapping.put("ZIP CODE", "zipCode");
//		mapping.put("LATITUDE", "latitude");
//		mapping.put("LONGITUDE", "longitude");
//		mapping.put("ON STREET NAME", "onStreetName");
//		mapping.put("CROSS STREET NAME", "crossStreetName");
//		mapping.put("OFF STREET NAME", "offStreetName");
////		mapping.put("service_zone", "serviceZone");
//		HeaderColumnNameTranslateMappingStrategy<CrashData> strategy = new HeaderColumnNameTranslateMappingStrategy<CrashData>();
//		strategy.setType(CrashData.class);
//		strategy.setColumnMapping(mapping);
//		CSVReader csvReader = null;
//		try {
//			csvReader = new CSVReader(new FileReader("data/crash_sample.csv"));
//		} catch (FileNotFoundException e) {
//
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		//CsvToBean<CrashData> csvToBean = new CsvToBean<CrashData>();
//		Reader reader = null;
//		try {
//			reader = Files.newBufferedReader(Paths.get("data/crash_sample.csv"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		// call the parse method of CsvToBean
//		// pass strategy, csvReader to parse method
//		System.out.println(new Date());
//		@SuppressWarnings("deprecation")
//		List<CrashData> crashData = new ArrayList<CrashData>();
//		CsvToBean<CrashData> csvToBean = new CsvToBeanBuilder(reader)
//                .withType(CrashData.class)
//                .withIgnoreLeadingWhiteSpace(true)
//                .withMappingStrategy(strategy)
//                .build();
//
//        Iterator<CrashData> csvUserIterator = csvToBean.iterator();
//        while (csvUserIterator.hasNext()) {
//        	CrashData csvUser = csvUserIterator.next();
////            System.out.println(csvUser.toString());
//        }
//        System.out.println(new Date());
////		csvToBean.parse(strategy, csvReader);
//		return crashData;
//	}

}
