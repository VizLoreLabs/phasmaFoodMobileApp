package com.vizlore.phasmafood.model.results;

/**
 * @author Stevan Medic
 * <p>
 * Created on Nov 2018
 */
public class MeasuredSample {

	private String wave;
	private String measurement;

	public MeasuredSample(String wave, String measurement) {
		this.wave = wave;
		this.measurement = measurement;
	}

	public MeasuredSample() {
	}

	public String getWave() {
		return wave;
	}

	public void setWave(String wave) {
		this.wave = wave;
	}

	public String getMeasurement() {
		return measurement;
	}

	public void setMeasurement(String measurement) {
		this.measurement = measurement;
	}

	@Override
	public String toString() {
		return "MeasuredSample {" +
			"wave='" + wave + '\'' +
			", measurement='" + measurement + '\'' +
			'}';
	}
}
