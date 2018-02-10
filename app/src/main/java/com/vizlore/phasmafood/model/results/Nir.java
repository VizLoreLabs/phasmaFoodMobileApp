
package com.vizlore.phasmafood.model.results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Nir {

    @SerializedName("wavelength")
    @Expose
    private Integer wavelength;
    @SerializedName("value")
    @Expose
    private Integer value;

    public Integer getWavelength() {
        return wavelength;
    }

    public void setWavelength(Integer wavelength) {
        this.wavelength = wavelength;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

}
