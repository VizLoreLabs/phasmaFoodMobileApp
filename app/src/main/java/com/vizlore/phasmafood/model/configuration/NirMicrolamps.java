
package com.vizlore.phasmafood.model.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NirMicrolamps {

    @SerializedName("V_nir")
    @Expose
    private Integer vNir;
    @SerializedName("t_nir")
    @Expose
    private Integer tNir;

    public Integer getVNir() {
        return vNir;
    }

    public void setVNir(Integer vNir) {
        this.vNir = vNir;
    }

    public Integer getTNir() {
        return tNir;
    }

    public void setTNir(Integer tNir) {
        this.tNir = tNir;
    }

}
