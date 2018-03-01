
package com.vizlore.phasmafood.model.results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NIR {

    @SerializedName("averageAbsorbance")
    @Expose
    private List<AverageAbsorbance> averageAbsorbance = null;

    public List<AverageAbsorbance> getAverageAbsorbance() {
        return averageAbsorbance;
    }

    public void setAverageAbsorbance(List<AverageAbsorbance> averageAbsorbance) {
        this.averageAbsorbance = averageAbsorbance;
    }

}
