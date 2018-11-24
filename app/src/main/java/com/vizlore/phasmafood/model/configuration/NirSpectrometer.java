
package com.vizlore.phasmafood.model.configuration;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class NirSpectrometer {

	@SerializedName("single")
	public abstract String single();

	@SerializedName("av_NIR")
	public abstract Integer avNIRm();

	@SerializedName("NirMicrolamps")
	public abstract NirMicrolamps nirMicrolamps();

	public static Builder builder() {
		return new AutoValue_NirSpectrometer.Builder();
	}

	@AutoValue.Builder
	public abstract static class Builder {
		public abstract Builder setSingle(String single);

		public abstract Builder setAvNIRm(Integer avNIRm);

		public abstract Builder setNirMicrolamps(NirMicrolamps nirMicrolamps);

		public abstract NirSpectrometer build();
	}
}
