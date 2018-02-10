
package com.vizlore.phasmafood.model.wizard;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class Subproblem {

	public abstract String name();

	public static TypeAdapter<Subproblem> typeAdapter(Gson gson) {
		return new AutoValue_Subproblem.GsonTypeAdapter(gson);
	}
}
