
package com.vizlore.phasmafood.model.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisLeds {

    @SerializedName("Vw_vis")
    @Expose
    private Integer vwVis;
    @SerializedName("V_UV")
    @Expose
    private Integer vUV;

    public Integer getVwVis() {
        return vwVis;
    }

    public void setVwVis(Integer vwVis) {
        this.vwVis = vwVis;
    }

    public Integer getVUV() {
        return vUV;
    }

    public void setVUV(Integer vUV) {
        this.vUV = vUV;
    }

}
