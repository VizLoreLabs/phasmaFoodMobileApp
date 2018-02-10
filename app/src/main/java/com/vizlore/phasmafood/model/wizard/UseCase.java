
package com.vizlore.phasmafood.model.wizard;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

@AutoValue
public abstract class UseCase {

	public abstract Integer id();
	public abstract String name();
	public abstract List<Subcase> subcases();

	public static TypeAdapter<UseCase> typeAdapter(Gson gson) {
		return new AutoValue_UseCase.GsonTypeAdapter(gson);
	}
}
