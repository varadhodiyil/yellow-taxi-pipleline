package com.yellowtaxipipeline.model;

import com.google.gson.annotations.SerializedName;

public class WeatherData {
	public WeatherData() {
		super();
	}

	public WeatherData(String location, String datetime, int temperature, int feelsLike, int windSpeed,
			String windDirectionDegree, int humidity, int visibility, int pressure, int dewPoint) {
		super();
		this.location = location;
		this.datetime = datetime;
		this.temperature = temperature;
		this.feelsLike = feelsLike;
		this.windSpeed = windSpeed;
		this.windDirectionDegree = windDirectionDegree;
		this.humidity = humidity;
		this.visibility = visibility;
		this.pressure = pressure;
		this.dewPoint = dewPoint;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getFeelsLike() {
		return feelsLike;
	}

	public void setFeelsLike(int feelsLike) {
		this.feelsLike = feelsLike;
	}

	public int getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(int windSpeed) {
		this.windSpeed = windSpeed;
	}

	public String getWindDirectionDegree() {
		return windDirectionDegree;
	}

	public void setWindDirectionDegree(String windDirectionDegree) {
		this.windDirectionDegree = windDirectionDegree;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

	public int getPressure() {
		return pressure;
	}

	public void setPressure(int pressure) {
		this.pressure = pressure;
	}

	public int getDewPoint() {
		return dewPoint;
	}

	public void setDewPoint(int dewPoint) {
		this.dewPoint = dewPoint;
	}

	@SerializedName("location")
	private String location;
	@SerializedName("date_time")
	private String datetime;
	@SerializedName("tempC")
	private int temperature;
	@SerializedName("FeelsLikeC")
	private int feelsLike;
	@SerializedName("windspeedKmph")
	private int windSpeed;
	@SerializedName("winddirDegree")
	private String windDirectionDegree;
	@SerializedName("humidity")
	private int humidity;
	@SerializedName("visibility")
	private int visibility;
	@SerializedName("pressure")
	private int pressure;
	@SerializedName("DewPointC")
	private int dewPoint;

	@Override
	public String toString() {
		return "WeatherData [location=" + location + ", datetime=" + datetime + ", temperature=" + temperature
				+ ", feelsLike=" + feelsLike + ", windSpeed=" + windSpeed + ", windDirectionDegree="
				+ windDirectionDegree + ", humidity=" + humidity + ", visibility=" + visibility + ", pressure="
				+ pressure + ", dewPoint=" + dewPoint + "]";
	}

}
