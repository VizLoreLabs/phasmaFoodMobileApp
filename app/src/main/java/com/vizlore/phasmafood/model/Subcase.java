
package com.vizlore.phasmafood.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

@AutoValue
public abstract class Subcase {

	public abstract Integer id();

	public abstract String name();

	public abstract List<Subproblem> subproblems();

	public static TypeAdapter<Subcase> typeAdapter(Gson gson) {
		return new AutoValue_Subcase.GsonTypeAdapter(gson);
	}
}
