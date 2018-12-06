
package com.vizlore.phasmafood.model.configuration;

import com.google.gson.annotations.SerializedName;

public class Camera {

	@SerializedName("t_cam")
	public Integer tCam;

	@SerializedName("vw_cam")
	public Integer vwCam;

	public Camera() {
	}

	public Camera(Integer tCam, Integer vwCam) {
		this.tCam = tCam;
		this.vwCam = vwCam;
	}

	public Integer gettCam() {
		return tCam;
	}

	public void settCam(Integer tCam) {
		this.tCam = tCam;
	}

	public Integer getVwCam() {
		return vwCam;
	}

	public void setVwCam(Integer vwCam) {
		this.vwCam = vwCam;
	}
}
