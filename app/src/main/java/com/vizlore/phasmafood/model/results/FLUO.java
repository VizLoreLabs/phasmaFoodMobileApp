
package com.vizlore.phasmafood.model.results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FLUO {

	@SerializedName("preprocessed")
	@Expose
	private List<Preprocessed> preprocessed = null;
	@SerializedName("whiteReference")
	@Expose
	private List<WhiteReference> whiteReference = null;
	@SerializedName("darkReference")
	@Expose
	private List<DarkReference> darkReference = null;
	@SerializedName("rawData")
	@Expose
	private List<List<RawData>> rawData = null;
	@SerializedName("rawWhite")
	@Expose
	private List<List<RawWhite>> rawWhite = null;
	@SerializedName("rawDark")
	@Expose
	private List<List<RawDark>> rawDark = null;

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

	public List<List<RawData>> getRawData() {
		return rawData;
	}

	public void setRawData(List<List<RawData>> rawData) {
		this.rawData = rawData;
	}

	public List<List<RawWhite>> getRawWhite() {
		return rawWhite;
	}

	public void setRawWhite(List<List<RawWhite>> rawWhite) {
		this.rawWhite = rawWhite;
	}

	public List<List<RawDark>> getRawDark() {
		return rawDark;
	}

	public void setRawDark(List<List<RawDark>> rawDark) {
		this.rawDark = rawDark;
	}

}
