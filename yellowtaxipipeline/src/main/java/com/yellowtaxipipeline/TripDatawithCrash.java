package com.yellowtaxipipeline;

import java.util.HashMap;

import com.google.gson.annotations.SerializedName;

public class TripDatawithCrash {
	@SerializedName("tpep_pickup_datetime")
	private String tpepPickupDatetime;
	@SerializedName("tpep_dropoff_datetime")
	private String tpepDropoffDatetime;
	@SerializedName("PULocation")
	private String pULocation;
	@SerializedName("DOLocation")
	private String dOLocation;
	@SerializedName("crash_exist")
	private boolean crashExist;
	@SerializedName("crash_count")
	private int crashCount;
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
	@SerializedName("crash_reasons")
	private HashMap<String, Integer> crashReason;
	public TripDatawithCrash(String tpepPickupDatetime, String tpepDropoffDatetime, String pULocation,
			String dOLocation, boolean crashExist, int crashCount, HashMap<String, Integer> crashReason,
			int temperature, int feelsLike, int windSpeed,
			String windDirectionDegree, int humidity, int visibility, int pressure, int dewPoint) {
		super();
		this.tpepPickupDatetime = tpepPickupDatetime;
		this.tpepDropoffDatetime = tpepDropoffDatetime;
		this.pULocation = pULocation;
		this.dOLocation = dOLocation;
		this.crashExist = crashExist;
		this.crashCount = crashCount;

		this.crashReason = crashReason;
		this.temperature = temperature;
		this.feelsLike = feelsLike;
		this.windSpeed = windSpeed;
		this.windDirectionDegree = windDirectionDegree;
		this.humidity = humidity;
		this.visibility = visibility;
		this.pressure = pressure;
		this.dewPoint = dewPoint;
	}
	@Override
	public String toString() {
		return "TripDatawithCrash [tpepPickupDatetime=" + tpepPickupDatetime + ", tpepDropoffDatetime="
				+ tpepDropoffDatetime + ", pULocation=" + pULocation + ", dOLocation=" + dOLocation + ", crashExist="
				+ crashExist + ", crashCount=" + crashCount + ", crashReason=" + crashReason + ", temperature=" + temperature + ", feelsLike="
				+ feelsLike + ", windSpeed=" + windSpeed + ", windDirectionDegree=" + windDirectionDegree
				+ ", humidity=" + humidity + ", visibility=" + visibility + ", pressure=" + pressure + ", dewPoint="
				+ dewPoint  + "]";
	}
	
	
	
	
}
