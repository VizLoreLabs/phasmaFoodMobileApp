package com.vizlore.phasmafood.model.results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Stevan Medic
 * <p>
 * Created on Sep 2019
 */
public class CameraItem {

	@SerializedName("name")
	@Expose
	private String name;

	@SerializedName("camera")
	@Expose
	private String camera;

	public CameraItem(String name, String camera) {
		this.name = name;
		this.camera = camera;
	}

	public String getCamera() {
		return camera;
	}

	public void setCamera(String camera) {
		this.camera = camera;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
