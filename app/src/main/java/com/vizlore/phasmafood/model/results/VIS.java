
package com.vizlore.phasmafood.model.results;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VIS {

    @SerializedName("Preprocessed")
    @Expose
    private List<Preprocessed> preprocessed = null;

    public List<Preprocessed> getPreprocessed() {
        return preprocessed;
    }

    public void setPreprocessed(List<Preprocessed> preprocessed) {
        this.preprocessed = preprocessed;
    }

}
