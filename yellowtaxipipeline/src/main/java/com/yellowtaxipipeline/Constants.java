package com.yellowtaxipipeline;

/*
 * Class for manintaining Constants in the Application
 */
public class Constants {
	//Hostname of ActiveMQ server
	public static String MQ_HOST = "tcp://localhost:61616";
	//port for ActiveMQ server
	public static int MQ_PORT = 61616;
	//Topic name for consuming Trip data 
	public static String DATASET_SRC = "dataset";
	//Topic name for Publishing cleaned Trip data
	public static String DATASET_DEST = "dataset/cleaned";
	//Topic name for consuming crash data
	public static String CRASH_SRC = "crash";
	//Topic name for Publishing cleaned crash data
	public static String CRASH_REPORT = "report/crash";
	//Taxizone Lookup data CSV name
	public static String LOOKUP_DS = "data\\taxi+_zone_lookup.csv";
	//Path for reading weather data for cities
	public static String WEATHER_DIR = "data\\weather\\";

}
