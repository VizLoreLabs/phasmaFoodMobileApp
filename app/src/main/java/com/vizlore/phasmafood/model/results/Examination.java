
package com.vizlore.phasmafood.model.results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Examination {

	@SerializedName("sampleID")
	@Expose
	private String sampleID;
	@SerializedName("laboratory")
	@Expose
	private String laboratory;
	@SerializedName("UserID")
	@Expose
	private String userID;
	@SerializedName("deviceID")
	@Expose
	private String deviceID;
	@SerializedName("mobileID")
	@Expose
	private String mobileID;
	@SerializedName("foodType")
	@Expose
	private String foodType;
	@SerializedName("useCase")
	@Expose
	private String useCase;
	@SerializedName("granularity")
	@Expose
	private String granularity;
	@SerializedName("contamination")
	@Expose
	private String contamination;
	@SerializedName("Mycotoxins")
	@Expose
	private String mycotoxins;
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

	public String getMobileID() {
		return mobileID;
	}

	public void setMobileID(String mobileID) {
		this.mobileID = mobileID;
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

}
