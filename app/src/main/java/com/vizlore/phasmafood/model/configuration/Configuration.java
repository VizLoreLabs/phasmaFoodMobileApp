
package com.vizlore.phasmafood.model.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Configuration {

    @SerializedName("NirSpectrometer")
    @Expose
    private NirSpectrometer nirSpectrometer;
    @SerializedName("VisSpectrometer")
    @Expose
    private VisSpectrometer visSpectrometer;
    @SerializedName("camera")
    @Expose
    private Camera camera;

    public NirSpectrometer getNirSpectrometer() {
        return nirSpectrometer;
    }

    public void setNirSpectrometer(NirSpectrometer nirSpectrometer) {
        this.nirSpectrometer = nirSpectrometer;
    }

    public VisSpectrometer getVisSpectrometer() {
        return visSpectrometer;
    }

    public void setVisSpectrometer(VisSpectrometer visSpectrometer) {
        this.visSpectrometer = visSpectrometer;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

}
