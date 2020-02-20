package com.yellowtaxipipeline;

import com.google.gson.annotations.SerializedName;

public class TripData {
	@SerializedName("tpep_pickup_datetime")
	private String tpepPickupDatetime;
	@SerializedName("tpep_dropoff_datetime")
	private String tpepDropoffDatetime;
	@SerializedName("PULocation")
	private String pULocation;
	@SerializedName("DOLocation")
	private String dOLocation;

	public TripData(String tpepPickupDatetime, String tpepDropoffDatetime, String pULocation, String dOLocation) {
		this.tpepPickupDatetime = tpepPickupDatetime;
		this.tpepDropoffDatetime = tpepDropoffDatetime;
		this.pULocation = pULocation;
		this.dOLocation = dOLocation;
	}

	@Override
	public String toString() {
		return "TripDatawithCrash [tpepPickupDatetime=" + tpepPickupDatetime + ", tpepDropoffDatetime="
				+ tpepDropoffDatetime + ", pULocation=" + pULocation + ", dOLocation=" + dOLocation + "]";
	}

}
