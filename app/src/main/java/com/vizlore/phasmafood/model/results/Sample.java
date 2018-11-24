
package com.vizlore.phasmafood.model.results;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vizlore.phasmafood.model.configuration.Configuration;

public class Sample {

	@SerializedName("sampleID")
	@Expose
	private String sampleID;
	@SerializedName("laboratory")
	@Expose
	private String laboratory;
	@SerializedName("userID")
	@Expose
	private String userID;
	@SerializedName("deviceID")
	@Expose
	private String deviceID;
	@SerializedName("foodType")
	@Expose
	private String foodType;
	@SerializedName("useCase")
	@Expose
	private String useCase;

	@Nullable
	private String mobileID;

	// use case 1 specific // TODO: 9/4/18 reconsider
	@Expose
	@SerializedName("granularity")
	@Nullable
	private String granularity;

	@SerializedName("contamination")
	@Expose
	@Nullable
	private String contamination;

	@SerializedName("mycotoxins")
	@Expose
	@Nullable
	private String mycotoxins;

	@SerializedName("aflatoxinName")
	@Expose
	@Nullable
	private String aflatoxinName;

	@SerializedName("aflatoxinValue")
	@Expose
	@Nullable
	private String aflatoxinValue;

	@SerializedName("aflatoxinUnit")
	@Expose
	@Nullable
	private String aflatoxinUnit;

	//use case 2 specific
	@SerializedName("temperature")
	@Expose
	@Nullable
	private String temperature;

	@SerializedName("tempExposureHours")
	@Expose
	@Nullable
	private String tempExposureHours;

	@SerializedName("microbiologicalValue")
	@Expose
	@Nullable
	private String microbiologicalValue;

	@SerializedName("microbiologicalUnit")
	@Expose
	@Nullable
	private String microbiologicalUnit;

	@SerializedName("VIS")
	@Expose
	private VIS vIS;
	@SerializedName("NIR")
	@Expose
	private NIR nIR;
	@SerializedName("FLUO")
	@Expose
	private FLUO fLUO;

	@SerializedName("configuration")
	@Nullable
	private Configuration configuration;

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

	@Nullable
	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(@Nullable String temperature) {
		this.temperature = temperature;
	}

	@Nullable
	public String getTempExposureHours() {
		return tempExposureHours;
	}

	public void setTempExposureHours(@Nullable String tempExposureHours) {
		this.tempExposureHours = tempExposureHours;
	}

	@Nullable
	public String getMicrobiologicalValue() {
		return microbiologicalValue;
	}

	public void setMicrobiologicalValue(@Nullable String microbiologicalValue) {
		this.microbiologicalValue = microbiologicalValue;
	}

	@Nullable
	public String getMicrobiologicalUnit() {
		return microbiologicalUnit;
	}

	public void setMicrobiologicalUnit(@Nullable String microbiologicalUnit) {
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

	public FLUO getFLUO() {
		return fLUO;
	}

	public void setFLUO(FLUO fLUO) {
		this.fLUO = fLUO;
	}

	public void setMobileID(String mobileID) {
		this.mobileID = mobileID;
	}

	public void setConfiguration(@NonNull Configuration configuration) {
		this.configuration = configuration;
	}
}
