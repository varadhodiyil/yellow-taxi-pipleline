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
	@SerializedName("CONTRIBUTING FACTOR VEHICLE 1")
	private String contributingFactor;

	public CrashData() {
	}

	@Override
	public String toString() {
		return "CrashData [collisionId=" + collisionId + ", crashDate=" + crashDate + ", crashTime=" + crashTime
				+ ", borough=" + borough + ", zipCode=" + zipCode + ", latitude=" + latitude + ", longitude="
				+ longitude + ", contributingFactor=" + contributingFactor + "]";
	}

	public CrashData(String collisionId, String crashDate, String crashTime, String borough, String zipCode,
			String latitude, String longitude, String contributingFactor) {
		super();
		this.collisionId = collisionId;
		this.crashDate = crashDate;
		this.crashTime = crashTime;
		this.borough = borough;
		this.zipCode = zipCode;
		this.latitude = latitude;
		this.longitude = longitude;
		this.contributingFactor = contributingFactor;
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

	public String getCrashTime() {
		return crashTime;
	}

	public void setCrashTime(String crashTime) {
		this.crashTime = crashTime;
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

	public String getContributingFactor() {
		return contributingFactor;
	}

	public void setContributingFactor(String contributingFactor) {
		this.contributingFactor = contributingFactor;
	}
}
