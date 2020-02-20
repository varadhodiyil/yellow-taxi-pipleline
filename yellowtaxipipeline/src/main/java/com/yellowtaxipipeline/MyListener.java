package com.yellowtaxipipeline;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Set;

import javax.jms.Message;
import javax.jms.MessageListener;
import org.apache.activemq.command.ActiveMQBytesMessage;
import com.google.gson.Gson;
import com.yellowtaxipipeline.model.CrashData;
import com.yellowtaxipipeline.model.CrashWeatherData;
import com.yellowtaxipipeline.model.TaxiZoneLookup;
import com.yellowtaxipipeline.model.TripData;
import com.yellowtaxipipeline.model.WeatherData;
import com.yellowtaxipipeline.model.YellowTripData;

public class MyListener implements MessageListener {
	private String topic;
	Producer<TripData> producerTripData;
	Producer<CrashWeatherData> producerCrash;
	CrashWeatherData crashWeatherData;

	public MyListener(String topic) {
		super();
		this.topic = topic;
		producerTripData = new Producer<TripData>(Constants.DATASET_DEST);
		App.thread(producerTripData, false);
		producerCrash = new Producer<CrashWeatherData>(Constants.CRASH_REPORT);
		App.thread(producerCrash, false);
	}

	@Override
	public void onMessage(Message message) {

		try {
			if (topic.equals(Constants.DATASET_SRC)) {
				ActiveMQBytesMessage bytesMessage = (ActiveMQBytesMessage) message;
				String messageText = new String(bytesMessage.getContent().data);
				Gson gson = new Gson();
				YellowTripData yellowTripData = gson.fromJson(messageText, YellowTripData.class);
				TaxiZoneLookup puLocationLookup = App.taxiZoneLookupData.stream()
						.filter(taxi -> taxi.getLocationID() == yellowTripData.getpULocationID()).findAny()
						.orElse(null);
				String puLocation = puLocationLookup == null ? "" : puLocationLookup.getBorough();
				TaxiZoneLookup doLocationLookup = App.taxiZoneLookupData.stream()
						.filter(taxi -> taxi.getLocationID() == yellowTripData.getdOLocationID()).findAny()
						.orElse(null);
				String doLocation = doLocationLookup == null ? "" : doLocationLookup.getBorough();
				System.out.println(new TripData(yellowTripData.getTpepPickupDatetime(),
						yellowTripData.getTpepDropoffDatetime(), puLocation, doLocation).toString());
				producerTripData.send(new TripData(yellowTripData.getTpepPickupDatetime(),
						yellowTripData.getTpepDropoffDatetime(), puLocation, doLocation));
			} else if (topic.equals(Constants.CRASH_SRC)) {
				CrashData crashData = null;
				ActiveMQBytesMessage bytesMessage = (ActiveMQBytesMessage) message;
				String messageText = new String(bytesMessage.getContent().data);
				if (messageText != null && !messageText.isEmpty()) {
					Gson gson = new Gson();
					crashData = gson.fromJson(messageText, CrashData.class);
					if (crashData != null) {
				System.out.println(crashData.toString());
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
						DateTimeFormatter crashFormatter = DateTimeFormatter.ofPattern("M/d/yyyy H");
						System.out.println(crashData.getCrashDate());
						LocalDateTime dateLimit = LocalDateTime.parse(crashData.getCrash_date(), formatter);
						// <hour, <zone, count>
						if (dateLimit.isAfter(
								LocalDateTime.parse("31/12/2017 0:00", DateTimeFormatter.ofPattern("d/M/yyyy H:mm")))
								&& dateLimit.isBefore(LocalDateTime.parse("1/2/2018 0:00",
										DateTimeFormatter.ofPattern("d/M/yyyy H:mm")))) {

							// String hour = String.valueOf(LocalTime.parse(crashData.getCrashTime(),
							// DateTimeFormatter.ofPattern("H:mm")).getHour());
							String crashKey = dateLimit.format(crashFormatter);
							HashMap<String, HashMap<Integer, HashMap<String, Integer>>> hourData = new HashMap<String, HashMap<Integer, HashMap<String, Integer>>>();
							if (crashWeatherData != null && crashWeatherData.getCrashDateTime().equals(crashKey)) {
								hourData = crashWeatherData.getCrashDetails();
								if (hourData.containsKey(crashData.getBorough())) {
									HashMap<Integer, HashMap<String, Integer>> crash = hourData
											.get(crashData.getBorough());
									int count = crash.keySet().iterator().next() + 1;
									HashMap<String, Integer> reason = crash.get(crash.keySet().iterator().next());
									if (reason.containsKey(crashData.getContributingFactor())) {
										reason.put(crashData.getContributingFactor(),
												reason.get(crashData.getContributingFactor()) + 1);
									} else
										reason.put(crashData.getContributingFactor(), 1);
									crash = new HashMap<Integer, HashMap<String, Integer>>();
									crash.put(count, reason);
									hourData.put(crashData.getBorough(), crash);
								} else {
									HashMap<Integer, HashMap<String, Integer>> crash = new HashMap<Integer, HashMap<String, Integer>>();
									HashMap<String, Integer> crashReason = new HashMap<String, Integer>();
									crashReason.put(crashData.getContributingFactor(), 1);
									crash.put(1, crashReason);
									hourData.put(crashData.getBorough(), crash);
								}
							} else {
								HashMap<Integer, HashMap<String, Integer>> crash = new HashMap<Integer, HashMap<String, Integer>>();
								HashMap<String, Integer> reason = new HashMap<String, Integer>();
								reason.put(crashData.getContributingFactor(), 1);
								crash.put(1, reason);
								hourData.put(crashData.getBorough(), crash);
							}
							// App.crashData.put(crashKey, hourData);
							HashMap<String, WeatherData> weatherData = null;
							if (crashWeatherData != null && LocalDateTime.parse(crashKey, crashFormatter).isAfter(
									LocalDateTime.parse(crashWeatherData.getCrashDateTime(), crashFormatter))) {
//								crashWeatherData.setWeatherDetails(readWeatherInfo(crashKey));
								crashWeatherData.setWeatherDetails(readWeatherInfo(crashKey, crashWeatherData.getCrashDetails().keySet()));
								System.out.println("Enters" + crashWeatherData.toString());
								producerCrash.send(crashWeatherData);
							}

							crashWeatherData = new CrashWeatherData(crashKey, hourData, weatherData);
//					System.out.println(App.crashData.toString());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static HashMap<String, WeatherData> readWeatherInfo(String pickupDateTime , Set<String> cityNames) {
		HashMap<String, WeatherData> weatherData = new HashMap<String, WeatherData>();
		LocalDateTime pickupDT = LocalDateTime.parse(pickupDateTime, DateTimeFormatter.ofPattern("M/d/yyyy H"));
		String range = String.valueOf((Integer.parseInt(pickupDT.format(DateTimeFormatter.ofPattern("H"))) / 3) * 3);
		for (String city : cityNames) {
			WeatherData weather = App.weatherDataList.stream()
					.filter(w -> w.getLocation() != null && w.getLocation().equalsIgnoreCase(city)
							&& LocalDateTime.parse(w.getDatetime(), DateTimeFormatter.ofPattern("M/d/yyyy H:mm")) // "yyyy-MM-dd
																													// HH:mm:ss"))
									.format(DateTimeFormatter.ofPattern("d/M/yyyy H"))
									.equals(pickupDT.format(DateTimeFormatter.ofPattern("d/M/yyyy")) + " " + range))
					.findAny().orElse(null);
			if (weather != null)
				weatherData.put(city, weather);
		}
		return weatherData;
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
