
package com.vizlore.phasmafood.model.results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Examination {

    @SerializedName("sampleID")
    @Expose
    private String sampleId;
    @SerializedName("laboratory")
    @Expose
    private String laboratory;
    @SerializedName("UserID")
    @Expose
    private String userId;
    @SerializedName("deviceID")
    @Expose
    private String deviceId;
    @SerializedName("foodType")
    @Expose
    private String foodType;
    @SerializedName("useCase")
    @Expose
    private String useCase;
    @SerializedName("temperature")
    @Expose
    private Double temperature;
    @SerializedName("temp_profile")
    @Expose
    private String tempProfile;
    @SerializedName("temp_exp_hours")
    @Expose
    private Integer tempExpHours;
    @SerializedName("package_type")
    @Expose
    private String packageType;
    @SerializedName("sample_status")
    @Expose
    private String sampleStatus;
    @SerializedName("measurement_freq")
    @Expose
    private Integer measurementFreq;
    @SerializedName("tvc")
    @Expose
    private Integer tvc;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("VIS")
    @Expose
    private List<UvVi> uvVis = null;
    @SerializedName("NIR")
    @Expose
    private List<Nir> nir = null;

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public String getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(String laboratory) {
        this.laboratory = laboratory;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getTempProfile() {
        return tempProfile;
    }

    public void setTempProfile(String tempProfile) {
        this.tempProfile = tempProfile;
    }

    public Integer getTempExpHours() {
        return tempExpHours;
    }

    public void setTempExpHours(Integer tempExpHours) {
        this.tempExpHours = tempExpHours;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getSampleStatus() {
        return sampleStatus;
    }

    public void setSampleStatus(String sampleStatus) {
        this.sampleStatus = sampleStatus;
    }

    public Integer getMeasurementFreq() {
        return measurementFreq;
    }

    public void setMeasurementFreq(Integer measurementFreq) {
        this.measurementFreq = measurementFreq;
    }

    public Integer getTvc() {
        return tvc;
    }

    public void setTvc(Integer tvc) {
        this.tvc = tvc;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<UvVi> getUvVis() {
        return uvVis;
    }

    public void setUvVis(List<UvVi> uvVis) {
        this.uvVis = uvVis;
    }

    public List<Nir> getNir() {
        return nir;
    }

    public void setNir(List<Nir> nir) {
        this.nir = nir;
    }

}
