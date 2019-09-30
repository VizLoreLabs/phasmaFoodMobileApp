package com.vizlore.phasmafood.model.results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Stevan Medic
 * <p>
 * Created on Sep 2019
 */
public class CameraItem {

	@SerializedName("camera")
	@Expose
	private String camera;

	public CameraItem(String camera) {
		this.camera = camera;
	}

	public String getCamera() {
		return camera;
	}

	public void setCamera(String camera) {
		this.camera = camera;
	}
}
