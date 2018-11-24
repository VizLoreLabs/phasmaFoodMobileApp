
package com.vizlore.phasmafood.model.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisSpectrometer {

    @SerializedName("t_vis")
    @Expose
    private Integer tVis;
    @SerializedName("g_vis")
    @Expose
    private Integer gVis;
    @SerializedName("b_vis")
    @Expose
    private Integer bVis;
    @SerializedName("t_fluo")
    @Expose
    private Integer tFluo;
    @SerializedName("g_fluo")
    @Expose
    private Integer gFluo;
    @SerializedName("b_fluo")
    @Expose
    private Integer bFluo;
    @SerializedName("visLeds")
    @Expose
    private VisLeds visLeds;

    public Integer getTVis() {
        return tVis;
    }

    public void setTVis(Integer tVis) {
        this.tVis = tVis;
    }

    public Integer getGVis() {
        return gVis;
    }

    public void setGVis(Integer gVis) {
        this.gVis = gVis;
    }

    public Integer getBVis() {
        return bVis;
    }

    public void setBVis(Integer bVis) {
        this.bVis = bVis;
    }

    public Integer getTFluo() {
        return tFluo;
    }

    public void setTFluo(Integer tFluo) {
        this.tFluo = tFluo;
    }

    public Integer getGFluo() {
        return gFluo;
    }

    public void setGFluo(Integer gFluo) {
        this.gFluo = gFluo;
    }

    public Integer getBFluo() {
        return bFluo;
    }

    public void setBFluo(Integer bFluo) {
        this.bFluo = bFluo;
    }

    public VisLeds getVisLeds() {
        return visLeds;
    }

    public void setVisLeds(VisLeds visLeds) {
        this.visLeds = visLeds;
    }

}
