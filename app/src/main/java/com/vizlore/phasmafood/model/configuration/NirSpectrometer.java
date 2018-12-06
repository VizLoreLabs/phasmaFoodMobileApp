
package com.vizlore.phasmafood.model.configuration;

import com.google.gson.annotations.SerializedName;

public class NirSpectrometer {

	@SerializedName("single")
	public String single;

	@SerializedName("av_NIR")
	public Integer avNIRm;

	@SerializedName("NirMicrolamps")
	public NirMicrolamps nirMicrolamps;

	public NirSpectrometer(String single, Integer avNIRm, NirMicrolamps nirMicrolamps) {
		this.single = single;
		this.avNIRm = avNIRm;
		this.nirMicrolamps = nirMicrolamps;
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

	public NirMicrolamps getNirMicrolamps() {
		return nirMicrolamps;
	}

	public void setNirMicrolamps(NirMicrolamps nirMicrolamps) {
		this.nirMicrolamps = nirMicrolamps;
	}
}
