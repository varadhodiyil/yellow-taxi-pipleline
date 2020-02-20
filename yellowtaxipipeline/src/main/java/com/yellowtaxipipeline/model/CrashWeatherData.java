package com.yellowtaxipipeline.model;

import java.util.HashMap;

public class CrashWeatherData {
	private String crashDateTime;
	private HashMap<String, HashMap<Integer, HashMap<String, Integer>>> crashDetails;
	private HashMap<String, WeatherData> weatherDetails;

	public HashMap<String, WeatherData> getWeatherDetails() {
		return weatherDetails;
	}

	public void setWeatherDetails(HashMap<String, WeatherData> weatherDetails) {
		this.weatherDetails = weatherDetails;
	}

	public String getCrashDateTime() {
		return crashDateTime;
	}

	public void setCrashDateTime(String crashDateTime) {
		this.crashDateTime = crashDateTime;
	}

	public HashMap<String, HashMap<Integer, HashMap<String, Integer>>> getCrashDetails() {
		return crashDetails;
	}

	@Override
	public String toString() {
		return "CrashDataUpdated [crashDateTime=" + crashDateTime + ", crashDetails=" + crashDetails
				+ ", weatherDetails=" + weatherDetails + "]";
	}

	public void setCrashDetails(HashMap<String, HashMap<Integer, HashMap<String, Integer>>> crashDetails) {
		this.crashDetails = crashDetails;
	}

	public CrashWeatherData(String crashDateTime,
			HashMap<String, HashMap<Integer, HashMap<String, Integer>>> crashDetails,
			HashMap<String, WeatherData> weatherDetails) {
		this.crashDateTime = crashDateTime;
		this.crashDetails = crashDetails;
		this.weatherDetails = weatherDetails;
	}

}
