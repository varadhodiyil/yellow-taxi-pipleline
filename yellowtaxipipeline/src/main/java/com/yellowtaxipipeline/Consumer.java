package com.yellowtaxipipeline;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Consumer implements Runnable {
	private String topic;

	public Consumer(String topic) {
		this.topic = topic;
	}

	@Override
	public void run() {
		try {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

			Connection connection = connectionFactory.createConnection();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Destination dest = session.createTopic(this.topic);

			MessageConsumer consumer = session.createConsumer(dest);

			MyListener myListener = new MyListener(this.topic);

			consumer.setMessageListener(myListener);

			connection.start();
//			List<YellowTripData> yellowTripDataArray = combineData();
//			System.out.println(yellowTripDataArray.size());
//			List<TripDatawithCrash> triDatawithCrashArray = new ArrayList<TripDatawithCrash>();
//			for (YellowTripData yellowTripData: yellowTripDataArray) {
//				int crashCount = 0;
//				HashMap<String, Integer> crashReasons = null;
//				boolean crashExist = false;
//				String puHour = LocalDateTime.parse(yellowTripData.getTpepPickupDatetime(), DateTimeFormatter.ofPattern("M/d/yyyy H:mm")).format(DateTimeFormatter.ofPattern("d/M/yyyy H"));
//				String doHour = LocalDateTime.parse(yellowTripData.getTpepDropoffDatetime(), DateTimeFormatter.ofPattern("M/d/yyyy H:mm")).format(DateTimeFormatter.ofPattern("d/M/yyyy H"));
//				
////				System.out.println(App.taxiZoneLookupData.size());
//				String puLocation = App.taxiZoneLookupData.stream().filter(taxi -> taxi.getLocationID() == yellowTripData.getpULocationID()).findAny().orElse(null).getBorough();
//				String doLocation = App.taxiZoneLookupData.stream().filter(taxi -> taxi.getLocationID() == yellowTripData.getdOLocationID()).findAny().orElse(null).getBorough();
//				
//				if ((App.crashData != null && App.crashData.containsKey(puHour) || App.crashData != null)) {
////					&&(App.crashData.containsKey(doHour))) {
//					HashMap<String, HashMap<Integer, HashMap<String, Integer>>> hourData = App.crashData.get(puHour);
////					System.out.println(puHour);
////					System.out.println(hourData.toString());
//					if (hourData != null && hourData.containsKey(puLocation.toUpperCase())) {
//						crashExist = true;
//						crashCount = hourData.get(puLocation.toUpperCase()).keySet().iterator().next();
//						crashReasons =  hourData.get(puLocation.toUpperCase()).get(hourData.get(puLocation.toUpperCase()).keySet().iterator().next());
//					}
//
//				}
//				//System.out.println(yellowTripData.getTpepPickupDatetime());
//				WeatherData weather = readWeatherInfo(puLocation, yellowTripData.getTpepPickupDatetime());
//				weather = (weather !=null) ? weather : new WeatherData();
//				triDatawithCrashArray.add(new TripDatawithCrash(yellowTripData.getTpepPickupDatetime(),
//						yellowTripData.getTpepDropoffDatetime(), puLocation, doLocation, 
//						crashExist, crashCount, crashReasons, weather.getTemperature(), weather.getFeelsLike(), 
//						weather.getWindSpeed(), weather.getWindDirectionDegree(), weather.getHumidity(), weather.getVisibility(), weather.getPressure(),
//						weather.getDewPoint()));
//				
//			}
//			for(TripDatawithCrash trip: triDatawithCrashArray) {
//				System.out.println(trip.toString());
//			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static WeatherData readWeatherInfo(String puLocation, String pickupDateTime) {
		LocalDateTime pickupDT = LocalDateTime.parse(pickupDateTime,
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		String range = String.valueOf((Integer.parseInt(pickupDT.format(DateTimeFormatter.ofPattern("H"))) / 3) * 3);
//		System.out.println(pickupDT.format(DateTimeFormatter.ofPattern("d/M/yyyy H")));
//		System.out.println(pickupDT.format(DateTimeFormatter.ofPattern("d/M/yyyy " + range)));
		WeatherData data = App.weatherDataList.stream()
				.filter(weather -> weather.getLocation().equals(puLocation)
						&& LocalDateTime.parse(weather.getDatetime(), DateTimeFormatter.ofPattern("M/d/yyyy H:mm")) // "yyyy-MM-dd
																													// HH:mm:ss"))
								.format(DateTimeFormatter.ofPattern("d/M/yyyy H"))
								.equals(pickupDT.format(DateTimeFormatter.ofPattern("d/M/yyyy")) + " " + range))
				.findAny().orElse(null);
//		System.out.println(data.toString());

		return data;
	}

//	private List<YellowTripData> combineData() {
//		Map<String, String> mapping = new HashMap<String, String>();
//		mapping.put("VendorID", "vendorID");
//		mapping.put("tpep_pickup_datetime", "tpepPickupDatetime");
//		mapping.put("tpep_dropoff_datetime", "tpepDropoffDatetime");
//		mapping.put("passenger_count", "passenger_count");
//		mapping.put("trip_distance", "tripDistance");
//		mapping.put("RatecodeID", "RatecodeID");
//		mapping.put("store_and_fwd_flag", "store_and_fwd_flag");
//		mapping.put("PULocationID", "pULocationID");
//		mapping.put("DOLocationID", "dOLocationID");
//		mapping.put("payment_type", "payment_type");
//		mapping.put("fare_amount", "fare_amount");
//		mapping.put("extra", "extra");
//		mapping.put("mta_tax", "mta_tax");
//		mapping.put("tip_amount", "tip_amount");
//		mapping.put("tolls_amount", "tolls_amount");
//		mapping.put("improvement_surcharge", "improvement_surcharge");
//		mapping.put("total_amount", "total_amount");
//		HeaderColumnNameTranslateMappingStrategy<YellowTripData> strategy = new HeaderColumnNameTranslateMappingStrategy<YellowTripData>();
//		strategy.setType(YellowTripData.class);
//		strategy.setColumnMapping(mapping);
////		strategy.
//		CSVReader csvReader = null;
//		try {
//			csvReader = new CSVReader(new FileReader("data/yellow_tripdata_2018-01_sample.csv"));
//		} catch (Exception e) {
//
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		CsvToBean<YellowTripData> csvToBean = new CsvToBean<YellowTripData>();
//
//		// call the parse method of CsvToBean
//		// pass strategy, csvReader to parse method
//		@SuppressWarnings({ "deprecation" })
//		List<YellowTripData> list = csvToBean.parse(strategy, csvReader);
//		System.out.println("Trip data" + list.size());
//		return list;
//	}

}
