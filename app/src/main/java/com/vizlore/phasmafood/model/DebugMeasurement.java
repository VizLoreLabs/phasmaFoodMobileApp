package com.vizlore.phasmafood.model;

/**
 * @author Stevan Medic
 * <p>
 * Created on May 2019
 */
public class DebugMeasurement {

	private String rawdata;
	private boolean status;
	private String expectedSize;
	private String receivedSize;
	private String percentage;

	public String getRawdata() {
		return rawdata;
	}

	public void setRawdata(String rawdata) {
		this.rawdata = rawdata;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getExpectedSize() {
		return expectedSize;
	}

	public void setExpectedSize(String expectedSize) {
		this.expectedSize = expectedSize;
	}

	public String getReceivedSize() {
		return receivedSize;
	}

	public void setReceivedSize(String receivedSize) {
		this.receivedSize = receivedSize;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
}
