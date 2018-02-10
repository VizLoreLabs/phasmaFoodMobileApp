
package com.vizlore.phasmafood.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Subproblem {

	public abstract String name();

	public static TypeAdapter<Subproblem> typeAdapter(Gson gson) {
		return new AutoValue_Subproblem.GsonTypeAdapter(gson);
	}
}
