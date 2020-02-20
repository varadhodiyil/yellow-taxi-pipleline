package com.yellowtaxipipeline;

import com.google.gson.annotations.SerializedName;

public class YellowTripData {
	@SerializedName("VendorID")
	private String vendorID;
	@SerializedName("tpep_pickup_datetime")
	private String tpepPickupDatetime;
	@SerializedName("tpep_dropoff_datetime")
	private String tpepDropoffDatetime;

	public YellowTripData() {
	}

	@SerializedName("passenger_count")
	private String passengerCount;
	@SerializedName("trip_distance")
	private String tripDistance;
	@SerializedName("RatecodeID")
	private String RatecodeID;
	@SerializedName("store_and_fwd_flag")
	private String storandFwdFlag;
	@SerializedName("PULocationID")
	private int pULocationID;
	@SerializedName("DOLocationID")
	private int dOLocationID;

//	@SerializedName("payment_type")
//	private String paymentType;
//	@SerializedName("fare_amount")
//	private String fareAmount;
//	@SerializedName("extra")
//	private String extra;
//	@SerializedName("mta_tax")
//	private String mtaTax;
//	@SerializedName("tip_amount")
//	private String tipAmount;
//	@SerializedName("tolls_amount")
//	private String tollsAmount;
//	@SerializedName("improvement_surcharge")
//	private String improvementSurcharge;
//	@SerializedName("total_amount")
//	private String totalAmount;
	public YellowTripData(String vendorID, String tpepPickupDatetime, String tpepDropoffDatetime, String passengerCount,
			String tripDistance, String ratecodeID, String storandFwdFlag, int pULocationID, int dOLocationID) {

//	}, String paymentType, String fareAmount, String extra, String mtaTax, String tipAmount,
//			String tollsAmount, String improvementSurcharge) {
		super();
		this.vendorID = vendorID;
		this.tpepPickupDatetime = tpepPickupDatetime;
		this.tpepDropoffDatetime = tpepDropoffDatetime;
		this.passengerCount = passengerCount;
		this.tripDistance = tripDistance;
		RatecodeID = ratecodeID;
		this.storandFwdFlag = storandFwdFlag;
		this.pULocationID = pULocationID;
		this.dOLocationID = dOLocationID;
//		this.paymentType = paymentType;
//		this.fareAmount = fareAmount;
//		this.extra = extra;
//		this.mtaTax = mtaTax;
//		this.tipAmount = tipAmount;
//		this.tollsAmount = tollsAmount;
//		this.improvementSurcharge = improvementSurcharge;
	}

	@Override
	public String toString() {
		return "YellowTripData [vendorID=" + vendorID + ", tpepPickupDatetime=" + tpepPickupDatetime
				+ ", tpepDropoffDatetime=" + tpepDropoffDatetime + ", passengerCount=" + passengerCount
				+ ", tripDistance=" + tripDistance + ", RatecodeID=" + RatecodeID + ", storandFwdFlag=" + storandFwdFlag
				+ ", pULocationID=" + pULocationID + ", dOLocationID=" + dOLocationID + "]"; // ", paymentType=" +
																								// paymentType
//				+ ", fareAmount=" + fareAmount + ", extra=" + extra + ", mtaTax=" + mtaTax + ", tipAmount=" + tipAmount
//				+ ", tollsAmount=" + tollsAmount + ", improvementSurcharge=" + improvementSurcharge + "]"; //", totalAmount="
		// + totalAmount + "]";
	}

	public String getVendorID() {
		return vendorID;
	}

	public void setVendorID(String vendorID) {
		this.vendorID = vendorID;
	}

	public String getTpepPickupDatetime() {
		return tpepPickupDatetime;
	}

	public void setTpepPickupDatetime(String tpepPickupDatetime) {
		this.tpepPickupDatetime = tpepPickupDatetime;
	}

	public String getTpepDropoffDatetime() {
		return tpepDropoffDatetime;
	}

	public void setTpepDropoffDatetime(String tpepDropoffDatetime) {
		this.tpepDropoffDatetime = tpepDropoffDatetime;
	}

	public String getPassengerCount() {
		return passengerCount;
	}

	public void setPassengerCount(String passengerCount) {
		this.passengerCount = passengerCount;
	}

	public String getTripDistance() {
		return tripDistance;
	}

	public void setTripDistance(String tripDistance) {
		this.tripDistance = tripDistance;
	}

	public String getRatecodeID() {
		return RatecodeID;
	}

	public void setRatecodeID(String ratecodeID) {
		RatecodeID = ratecodeID;
	}

	public String getStorandFwdFlag() {
		return storandFwdFlag;
	}

	public void setStorandFwdFlag(String storandFwdFlag) {
		this.storandFwdFlag = storandFwdFlag;
	}

	public int getpULocationID() {
		return pULocationID;
	}

	public void setpULocationID(int pULocationID) {
		this.pULocationID = pULocationID;
	}

	public int getdOLocationID() {
		return dOLocationID;
	}

	public void setdOLocationID(int dOLocationID) {
		this.dOLocationID = dOLocationID;
	}
//	public String getPaymentType() {
//		return paymentType;
//	}
//	public void setPaymentType(String paymentType) {
//		this.paymentType = paymentType;
//	}
//	public String getFareAmount() {
//		return fareAmount;
//	}
//	public void setFareAmount(String fareAmount) {
//		this.fareAmount = fareAmount;
//	}
//	public String getExtra() {
//		return extra;
//	}
//	public void setExtra(String extra) {
//		this.extra = extra;
//	}
//	public String getMtaTax() {
//		return mtaTax;
//	}
//	public void setMtaTax(String mtaTax) {
//		this.mtaTax = mtaTax;
//	}
//	public String getTipAmount() {
//		return tipAmount;
//	}
//	public void setTipAmount(String tipAmount) {
//		this.tipAmount = tipAmount;
//	}
//	public String getTollsAmount() {
//		return tollsAmount;
//	}
//	public void setTollsAmount(String tollsAmount) {
//		this.tollsAmount = tollsAmount;
//	}
//	public String getImprovementSurcharge() {
//		return improvementSurcharge;
//	}
//	public void setImprovementSurcharge(String improvementSurcharge) {
//		this.improvementSurcharge = improvementSurcharge;
//	}
//	public String getTotalAmount() {
//		return totalAmount;
//	}
//	public void setTotalAmount(String totalAmount) {
//		this.totalAmount = totalAmount;
//	}

}
