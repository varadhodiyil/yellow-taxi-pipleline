package com.yellowtaxipipeline;

import com.google.gson.annotations.SerializedName;

public class CrashData {
	@SerializedName("COLLISION_ID")
	private String collisionId;

	@SerializedName("CRASH DATE")
	private String crashDate;
	@SerializedName("CRASH TIME")
	private String crashTime;
	@SerializedName("BOROUGH")
	private String borough;
	@SerializedName("ZIP CODE")
	private String zipCode;
	@SerializedName("LATITUDE")
	private String latitude;
	@SerializedName("LONGITUDE")
	private String longitude;
	@SerializedName("ON STREET NAME")
	private String onStreetName;
	@SerializedName("CROSS STREET NAME")
	private String crossStreetName;
	@SerializedName("OFF STREET NAME")
	private String offStreetName;
	public CrashData() {
	}
	@Override
	public String toString() {
		return "CrashData [collisionId=" + collisionId + ", crashDate=" + crashDate + ", crash_time=" + crashTime
				+ ", borough=" + borough + ", zipCode=" + zipCode + ", latitude=" + latitude + ", longitude="
				+ longitude + ", onStreetName=" + onStreetName + ", crossStreetName=" + crossStreetName
				+ ", offStreetName=" + offStreetName + "]";
	}
	public CrashData(String collisionId, String crashDate, String crash_time, String borough, String zipCode,
			String latitude, String longitude, String onStreetName, String crossStreetName, String offStreetName) {
		super();
		this.collisionId = collisionId;
		this.crashDate = crashDate;
		this.crashTime = crash_time;
		this.borough = borough;
		this.zipCode = zipCode;
		this.latitude = latitude;
		this.longitude = longitude;
		this.onStreetName = onStreetName;
		this.crossStreetName = crossStreetName;
		this.offStreetName = offStreetName;
	}
	public String getCollisionId() {
		return collisionId;
	}
	public void setCollisionId(String collisionId) {
		this.collisionId = collisionId;
	}
	public String getCrashDate() {
		return crashDate;
	}
	public void setCrashDate(String crashDate) {
		this.crashDate = crashDate;
	}
	public String getCrash_time() {
		return crashTime;
	}
	public void setCrash_time(String crash_time) {
		this.crashTime = crash_time;
	}
	public String getBorough() {
		return borough;
	}
	public void setBorough(String borough) {
		this.borough = borough;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getOnStreetName() {
		return onStreetName;
	}
	public void setOnStreetName(String onStreetName) {
		this.onStreetName = onStreetName;
	}
	public String getCrossStreetName() {
		return crossStreetName;
	}
	public void setCrossStreetName(String crossStreetName) {
		this.crossStreetName = crossStreetName;
	}
	public String getOffStreetName() {
		return offStreetName;
	}
	public void setOffStreetName(String offStreetName) {
		this.offStreetName = offStreetName;
	}
}
