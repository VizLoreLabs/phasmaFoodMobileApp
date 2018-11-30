
package com.vizlore.phasmafood.model.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisSpectrometer {

    @SerializedName("t_vis")
    @Expose
    private Integer tVis;
    @SerializedName("t_fluo")
    @Expose
    private Integer tFluo;
    @SerializedName("visLeds")
    @Expose
    private VisLeds visLeds;

    public Integer getTVis() {
        return tVis;
    }

    public void setTVis(Integer tVis) {
        this.tVis = tVis;
    }

    public Integer getTFluo() {
        return tFluo;
    }

    public void setTFluo(Integer tFluo) {
        this.tFluo = tFluo;
    }

    public VisLeds getVisLeds() {
        return visLeds;
    }

    public void setVisLeds(VisLeds visLeds) {
        this.visLeds = visLeds;
    }

}
