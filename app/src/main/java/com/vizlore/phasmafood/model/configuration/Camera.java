
package com.vizlore.phasmafood.model.configuration;

import com.google.gson.annotations.SerializedName;

public class Camera {

	@SerializedName("capture_image_white")
	public String captureImageWhite;

	@SerializedName("capture_image_uv")
	public String captureImageUV;

	@SerializedName("capture_image_nir")
	public String captureImageNIR;

	@SerializedName("t_cam_white")
	public Integer tCamWhite;

	@SerializedName("t_cam_uv")
	public Integer tCamUv;

	@SerializedName("t_cam_nir")
	public Integer tCamNIR;

	public Camera() {
	}

	public Camera(String captureImageWhite, String captureImageUV, String captureImageNIR, Integer tCamWhite,
				  Integer tCamUv, Integer tCamNIR) {
		this.captureImageWhite = captureImageWhite;
		this.captureImageUV = captureImageUV;
		this.captureImageNIR = captureImageNIR;
		this.tCamWhite = tCamWhite;
		this.tCamUv = tCamUv;
		this.tCamNIR = tCamNIR;
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

	public void setCaptureImageWhite(String captureImageWhite) {
		this.captureImageWhite = captureImageWhite;
	}

	public void setCaptureImageUV(String captureImageUV) {
		this.captureImageUV = captureImageUV;
	}

	public void setCaptureImageNIR(String captureImageNIR) {
		this.captureImageNIR = captureImageNIR;
	}
}
