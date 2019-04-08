
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

	@Nullable
	private Long timestamp;

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

	@SerializedName("package")
	@Expose
	@Nullable
	private String packageParam;

	//use case 2 specific
	@SerializedName("temperature")
	@Expose
	@Nullable
	private String temperature;

	@SerializedName("tempExposureHours")
	@Expose
	@Nullable
	private String tempExposureHours;

	@SerializedName("microbioSampleId")
	@Expose
	@Nullable
	private String microbioSampleId;

	@SerializedName("microbiologicalValue")
	@Expose
	@Nullable
	private String microbiologicalValue;

	@SerializedName("microbiologicalUnit")
	@Expose
	@Nullable
	private String microbiologicalUnit;

	//use case 3 specific

	@SerializedName("otherSpecies")
	@Expose
	@Nullable
	private String otherSpecies;

	@SerializedName("foodSubtype")
	@Expose
	@Nullable
	private String foodSubtype;

	@SerializedName("adulterationSampleId")
	@Expose
	@Nullable
	private String adulterationSampleId;

	@SerializedName("alcoholLabel")
	@Expose
	@Nullable
	private String alcoholLabel;

	@SerializedName("authentic")
	@Expose
	@Nullable
	private String authentic;

	@SerializedName("puritySMP")
	@Expose
	@Nullable
	private String puritySMP;

	@SerializedName("lowValueFiller")
	@Expose
	@Nullable
	private String lowValueFiller;

	@SerializedName("nitrogenEnhancer")
	@Expose
	@Nullable
	private String nitrogenEnhancer;

	@SerializedName("hazardOneName")
	@Expose
	@Nullable
	private String hazardOneName;

	@SerializedName("hazardOnePct")
	@Expose
	@Nullable
	private String hazardOnePct;

	@SerializedName("hazardTwoName")
	@Expose
	@Nullable
	private String hazardTwoName;

	@SerializedName("hazardTwoPct")
	@Expose
	@Nullable
	private String hazardTwoPct;

	@SerializedName("dilutedPct")
	@Expose
	@Nullable
	private String dilutedPct;

	@SerializedName("lightsOnDuration")
	@Expose
	@Nullable
	private String lightsOnDuration;

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
	public String getPackageParam() {
		return packageParam;
	}

	public void setPackageParam(@Nullable String packageParam) {
		this.packageParam = packageParam;
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

	public void setMicrobioSampleId(@Nullable String microbioSampleId) {
		this.microbioSampleId = microbioSampleId;
	}

	@Nullable
	public String getMicrobioSampleId() {
		return microbioSampleId;
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

	@Nullable
	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(@Nullable Long timestamp) {
		this.timestamp = timestamp;
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

	@Nullable
	public String getMobileID() {
		return mobileID;
	}

	@Nullable
	public String getOtherSpecies() {
		return otherSpecies;
	}

	public void setOtherSpecies(@Nullable String otherSpecies) {
		this.otherSpecies = otherSpecies;
	}

	@Nullable
	public String getFoodSubtype() {
		return foodSubtype;
	}

	public void setFoodSubtype(@Nullable String foodSubtype) {
		this.foodSubtype = foodSubtype;
	}

	@Nullable
	public String getAdulterationSampleId() {
		return adulterationSampleId;
	}

	public void setAdulterationSampleId(@Nullable String adulterationSampleId) {
		this.adulterationSampleId = adulterationSampleId;
	}

	@Nullable
	public String getAlcoholLabel() {
		return alcoholLabel;
	}

	public void setAlcoholLabel(@Nullable String alcoholLabel) {
		this.alcoholLabel = alcoholLabel;
	}

	@Nullable
	public String getAuthentic() {
		return authentic;
	}

	public void setAuthentic(@Nullable String authentic) {
		this.authentic = authentic;
	}

	@Nullable
	public String getPuritySMP() {
		return puritySMP;
	}

	public void setPuritySMP(@Nullable String puritySMP) {
		this.puritySMP = puritySMP;
	}

	@Nullable
	public String getLowValueFiller() {
		return lowValueFiller;
	}

	public void setLowValueFiller(@Nullable String lowValueFiller) {
		this.lowValueFiller = lowValueFiller;
	}

	@Nullable
	public String getNitrogenEnhancer() {
		return nitrogenEnhancer;
	}

	public void setNitrogenEnhancer(@Nullable String nitrogenEnhancer) {
		this.nitrogenEnhancer = nitrogenEnhancer;
	}

	@Nullable
	public String getHazardOneName() {
		return hazardOneName;
	}

	public void setHazardOneName(@Nullable String hazardOneName) {
		this.hazardOneName = hazardOneName;
	}

	@Nullable
	public String getHazardOnePct() {
		return hazardOnePct;
	}

	public void setHazardOnePct(@Nullable String hazardOnePct) {
		this.hazardOnePct = hazardOnePct;
	}

	@Nullable
	public String getHazardTwoName() {
		return hazardTwoName;
	}

	public void setHazardTwoName(@Nullable String hazardTwoName) {
		this.hazardTwoName = hazardTwoName;
	}

	@Nullable
	public String getHazardTwoPct() {
		return hazardTwoPct;
	}

	public void setHazardTwoPct(@Nullable String hazardTwoPct) {
		this.hazardTwoPct = hazardTwoPct;
	}

	@Nullable
	public String getDilutedPct() {
		return dilutedPct;
	}

	public void setDilutedPct(@Nullable String dilutedPct) {
		this.dilutedPct = dilutedPct;
	}

	@Nullable
	public String getLightsOnDuration() {
		return lightsOnDuration;
	}

	public void setLightsOnDuration(@Nullable String lightsOnDuration) {
		this.lightsOnDuration = lightsOnDuration;
	}

	public VIS getvIS() {
		return vIS;
	}

	public void setvIS(VIS vIS) {
		this.vIS = vIS;
	}

	public NIR getnIR() {
		return nIR;
	}

	public void setnIR(NIR nIR) {
		this.nIR = nIR;
	}

	public FLUO getfLUO() {
		return fLUO;
	}

	public void setfLUO(FLUO fLUO) {
		this.fLUO = fLUO;
	}

	@Nullable
	public Configuration getConfiguration() {
		return configuration;
	}

	@Override
	public String toString() {
		return "Sample{" +
			"sampleID='" + sampleID + '\'' +
			", laboratory='" + laboratory + '\'' +
			", userID='" + userID + '\'' +
			", deviceID='" + deviceID + '\'' +
			", foodType='" + foodType + '\'' +
			", useCase='" + useCase + '\'' +
			", mobileID='" + mobileID + '\'' +
			", timestamp=" + timestamp +
			", granularity='" + granularity + '\'' +
			", contamination='" + contamination + '\'' +
			", mycotoxins='" + mycotoxins + '\'' +
			", aflatoxinName='" + aflatoxinName + '\'' +
			", aflatoxinValue='" + aflatoxinValue + '\'' +
			", aflatoxinUnit='" + aflatoxinUnit + '\'' +
			", packageParam='" + packageParam + '\'' +
			", temperature='" + temperature + '\'' +
			", tempExposureHours='" + tempExposureHours + '\'' +
			", microbioSampleId='" + microbioSampleId + '\'' +
			", microbiologicalValue='" + microbiologicalValue + '\'' +
			", microbiologicalUnit='" + microbiologicalUnit + '\'' +
			", otherSpecies='" + otherSpecies + '\'' +
			", foodSubtype='" + foodSubtype + '\'' +
			", adulterationSampleId='" + adulterationSampleId + '\'' +
			", alcoholLabel='" + alcoholLabel + '\'' +
			", authentic='" + authentic + '\'' +
			", puritySMP='" + puritySMP + '\'' +
			", lowValueFiller='" + lowValueFiller + '\'' +
			", nitrogenEnhancer='" + nitrogenEnhancer + '\'' +
			", hazardOneName='" + hazardOneName + '\'' +
			", hazardOnePct='" + hazardOnePct + '\'' +
			", hazardTwoName='" + hazardTwoName + '\'' +
			", hazardTwoPct='" + hazardTwoPct + '\'' +
			", dilutedPct='" + dilutedPct + '\'' +
			'}';
	}
}
