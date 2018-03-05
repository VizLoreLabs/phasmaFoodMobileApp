
package com.vizlore.phasmafood.model.results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sample {

	@SerializedName("sampleID")
	@Expose
	private String sampleID;

	@SerializedName("laboratory")
	@Expose
	private String laboratory;

	@SerializedName("UserID")
	@Expose
	private String userID;

	@SerializedName("mobileID")
	@Expose
	private String mobileID;

	@Expose
	private String deviceID;
	@SerializedName("foodType")

	@Expose
	private String foodType;
	@SerializedName("useCase")
	@Expose
	private String useCase;
	@SerializedName("temperature")
	@Expose
	private String temperature;
	@SerializedName("tempExposureHours")
	@Expose
	private String tempExposureHours;
	@SerializedName("microbiologicalValue")
	@Expose
	private String microbiologicalValue;
	@SerializedName("granularity")
	@Expose
	private String granularity;

	@SerializedName("contamination")
	@Expose
	private String contamination;

	@SerializedName("mycotoxins")
	@Expose
	private String mycotoxins;

	@SerializedName("aflatoxinName")
	@Expose
	private String aflatoxinName;

	@SerializedName("aflatoxinValue")
	@Expose
	private String aflatoxinValue;

	@SerializedName("aflatoxinUnit")
	@Expose
	private String aflatoxinUnit;

	@SerializedName("microbiologicalUnit")
	@Expose
	private String microbiologicalUnit;

	@SerializedName("VIS")
	@Expose
	private VIS vIS;
	@SerializedName("NIR")
	@Expose
	private NIR nIR;

	public String getSampleID() {
		return sampleID;
	}

	public void setSampleID(String sampleID) {
		this.sampleID = sampleID;
	}

	public String getLaboratory() {
		return laboratory;
	}

	public void setLaboratory(String laboratory) {
		this.laboratory = laboratory;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getFoodType() {
		return foodType;
	}

	public void setFoodType(String foodType) {
		this.foodType = foodType;
	}

	public String getUseCase() {
		return useCase;
	}

	public void setUseCase(String useCase) {
		this.useCase = useCase;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getTempExposureHours() {
		return tempExposureHours;
	}

	public void setTempExposureHours(String tempExposureHours) {
		this.tempExposureHours = tempExposureHours;
	}

	public String getMicrobiologicalValue() {
		return microbiologicalValue;
	}

	public void setMicrobiologicalValue(String microbiologicalValue) {
		this.microbiologicalValue = microbiologicalValue;
	}

	public String getMicrobiologicalUnit() {
		return microbiologicalUnit;
	}

	public void setMicrobiologicalUnit(String microbiologicalUnit) {
		this.microbiologicalUnit = microbiologicalUnit;
	}

	public VIS getVIS() {
		return vIS;
	}

	public void setVIS(VIS vIS) {
		this.vIS = vIS;
	}

	public NIR getNIR() {
		return nIR;
	}

	public void setNIR(NIR nIR) {
		this.nIR = nIR;
	}

	public String getGranularity() {
		return granularity;
	}

	public void setGranularity(String granularity) {
		this.granularity = granularity;
	}

	public String getContamination() {
		return contamination;
	}

	public void setContamination(String contamination) {
		this.contamination = contamination;
	}

	public String getMycotoxins() {
		return mycotoxins;
	}

	public void setMycotoxins(String mycotoxins) {
		this.mycotoxins = mycotoxins;
	}

	public String getAflatoxinName() {
		return aflatoxinName;
	}

	public void setAflatoxinName(String aflatoxinName) {
		this.aflatoxinName = aflatoxinName;
	}

	public String getAflatoxinValue() {
		return aflatoxinValue;
	}

	public void setAflatoxinValue(String aflatoxinValue) {
		this.aflatoxinValue = aflatoxinValue;
	}

	public String getAflatoxinUnit() {
		return aflatoxinUnit;
	}

	public void setAflatoxinUnit(String aflatoxinUnit) {
		this.aflatoxinUnit = aflatoxinUnit;
	}

	public String getMobileID() {
		return mobileID;
	}

	public void setMobileID(String mobileID) {
		this.mobileID = mobileID;
	}
}
