
package com.vizlore.phasmafood.model.results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NIR {

	@SerializedName("preprocessed")
	@Expose
	private List<MeasuredSample> preprocessed = null;
	@SerializedName("whiteReference")
	@Expose
	private List<MeasuredSample> whiteReference = null;
	@SerializedName("darkReference")
	@Expose
	private List<MeasuredSample> darkReference = null;

	public List<MeasuredSample> getPreprocessed() {
		return preprocessed;
	}

	public void setPreprocessed(List<MeasuredSample> preprocessed) {
		this.preprocessed = preprocessed;
	}

	public List<MeasuredSample> getWhiteReference() {
		return whiteReference;
	}

	public void setWhiteReference(List<MeasuredSample> whiteReference) {
		this.whiteReference = whiteReference;
	}

	public List<MeasuredSample> getDarkReference() {
		return darkReference;
	}

	public void setDarkReference(List<MeasuredSample> darkReference) {
		this.darkReference = darkReference;
	}

}
