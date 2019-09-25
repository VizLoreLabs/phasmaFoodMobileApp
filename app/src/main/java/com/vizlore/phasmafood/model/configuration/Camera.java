
package com.vizlore.phasmafood.model.configuration;

import com.google.gson.annotations.SerializedName;

public class Camera {

	@SerializedName("capture_image")
	public String captureImage;

	@SerializedName("t_cam_white")
	public Integer tCamWhite;

	@SerializedName("t_cam_uv")
	public Integer tCamUv;

	@SerializedName("t_cam_nir")
	public Integer tCamNIR;

	public Camera() {
	}

	public Camera(String captureImage, Integer tCamWhite, Integer tCamUv, Integer tCamNIR) {
		this.captureImage = captureImage;
		this.tCamWhite = tCamWhite;
		this.tCamUv = tCamUv;
		this.tCamNIR = tCamNIR;
	}

	public String getCaptureImage() {
		return captureImage;
	}

	public void setCaptureImage(String captureImage) {
		this.captureImage = captureImage;
	}

	public Integer gettCamWhite() {
		return tCamWhite;
	}

	public void settCamWhite(Integer tCamWhite) {
		this.tCamWhite = tCamWhite;
	}

	public Integer gettCamUv() {
		return tCamUv;
	}

	public void settCamUv(Integer tCamUv) {
		this.tCamUv = tCamUv;
	}

	public Integer gettCamNIR() {
		return tCamNIR;
	}

	public void settCamNIR(Integer tCamNIR) {
		this.tCamNIR = tCamNIR;
	}
}
