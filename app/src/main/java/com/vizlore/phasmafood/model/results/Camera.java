package com.vizlore.phasmafood.model.results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Stevan Medic
 * <p>
 * Created on Sep 2019
 */
public class Camera {

	@SerializedName("camera")
	@Expose
	private List<CameraItem> cameraItems;

	public Camera(List<CameraItem> cameraItems) {
		this.cameraItems = cameraItems;
	}

	public void setCameraItems(List<CameraItem> cameraItems) {
		this.cameraItems = cameraItems;
	}

	public List<CameraItem> getCameraItems() {
		return cameraItems;
	}
}
