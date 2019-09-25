
package com.vizlore.phasmafood.model.configuration;

import com.google.gson.annotations.SerializedName;

public class NirSpectrometer {

	@SerializedName("single")
	public String single;

	@SerializedName("av_NIR")
	public Integer avNIRm;

	public NirSpectrometer(String single, Integer avNIRm) {
		this.single = single;
		this.avNIRm = avNIRm;
	}

	public NirSpectrometer() {
	}

	public String getSingle() {
		return single;
	}

	public void setSingle(String single) {
		this.single = single;
	}

	public Integer getAvNIRm() {
		return avNIRm;
	}

	public void setAvNIRm(Integer avNIRm) {
		this.avNIRm = avNIRm;
	}
}
