
package com.vizlore.phasmafood.model.results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AverageAbsorbance {

    @SerializedName("wave")
    @Expose
    private String wave;
    @SerializedName("measurement")
    @Expose
    private String measurement;

    public String getWave() {
        return wave;
    }

    public void setWave(String wave) {
        this.wave = wave;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

}
