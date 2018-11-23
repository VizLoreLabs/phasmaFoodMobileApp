
package com.vizlore.phasmafood.model.results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VIS {

	@SerializedName("preprocessed")
	@Expose
	private List<MeasuredSample> preprocessed = null;
	@SerializedName("whiteReference")
	@Expose
	private List<MeasuredSample> whiteReference = null;
	@SerializedName("darkReference")
	@Expose
	private List<MeasuredSample> darkReference = null;
	@SerializedName("rawData")
	@Expose
	private List<List<MeasuredSample>> rawData = null;
	@SerializedName("rawWhite")
	@Expose
	private List<List<MeasuredSample>> rawWhite = null;
	@SerializedName("rawDark")
	@Expose
	private List<List<MeasuredSample>> rawDark = null;

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

	public List<List<MeasuredSample>> getRawData() {
		return rawData;
	}

	public void setRawData(List<List<MeasuredSample>> rawData) {
		this.rawData = rawData;
	}

	public List<List<MeasuredSample>> getRawWhite() {
		return rawWhite;
	}

	public void setRawWhite(List<List<MeasuredSample>> rawWhite) {
		this.rawWhite = rawWhite;
	}

	public List<List<MeasuredSample>> getRawDark() {
		return rawDark;
	}

	public void setRawDark(List<List<MeasuredSample>> rawDark) {
		this.rawDark = rawDark;
	}

}

