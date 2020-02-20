package com.yellowtaxipipeline;

import com.google.gson.annotations.SerializedName;

public class TaxiZoneLookup {
	private int locationID;
	private String borough;
	private String zone;
	@SerializedName("service_zone")
	private String serviceZone;

	public TaxiZoneLookup(int locationID, String borough, String zone, String serviceZone) {
		super();
		this.locationID = locationID;
		this.borough = borough;
		this.zone = zone;
		this.serviceZone = serviceZone;
	}

	@Override
	public String toString() {
		return "TaxiZoneLookup [locationID=" + locationID + ", borough=" + borough + ", zone=" + zone + ", serviceZone="
				+ serviceZone + "]";
	}

	public TaxiZoneLookup() {
		super();
	}

	public int getLocationID() {
		return locationID;
	}

	public void setLocationID(int locationID) {
		this.locationID = locationID;
	}

	public String getBorough() {
		return borough;
	}

	public void setBorough(String borough) {
		this.borough = borough;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getServiceZone() {
		return serviceZone;
	}

	public void setServiceZone(String serviceZone) {
		this.serviceZone = serviceZone;
	}

}
