
package com.vizlore.phasmafood.model.results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VIS {

	@SerializedName("preprocessed")
	@Expose
	private List<Preprocessed> preprocessed = null;
	@SerializedName("whiteReference")
	@Expose
	private List<WhiteReference> whiteReference = null;
	@SerializedName("darkReference")
	@Expose
	private List<DarkReference> darkReference = null;

	public List<Preprocessed> getPreprocessed() {
		return preprocessed;
	}

	public void setPreprocessed(List<Preprocessed> preprocessed) {
		this.preprocessed = preprocessed;
	}

	public List<WhiteReference> getWhiteReference() {
		return whiteReference;
	}

	public void setWhiteReference(List<WhiteReference> whiteReference) {
		this.whiteReference = whiteReference;
	}

	public List<DarkReference> getDarkReference() {
		return darkReference;
	}

	public void setDarkReference(List<DarkReference> darkReference) {
		this.darkReference = darkReference;
	}

}
