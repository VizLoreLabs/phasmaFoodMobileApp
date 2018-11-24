
package com.vizlore.phasmafood.model.configuration;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Camera {

	@SerializedName("t_cam")
	public abstract Integer tCam();

	@SerializedName("vw_cam")
	public abstract Integer vwCam();

	public static Builder builder() {
		return new AutoValue_Camera.Builder();
	}

	@AutoValue.Builder
	public abstract static class Builder {
		public abstract Builder setTCam(Integer tCam);

		public abstract Builder setVwCam(Integer vwCam);

		public abstract Camera build();
	}

	public static TypeAdapter<Camera> typeAdapter(Gson gson) {
		return new AutoValue_Camera.GsonTypeAdapter(gson);
	}
}
