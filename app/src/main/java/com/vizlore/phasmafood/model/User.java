
package com.vizlore.phasmafood.model;


import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class User {

	public abstract String name();

	public abstract String lastName();

	public abstract Integer height();

	public abstract Integer weight();

	@AutoValue.Builder
	public abstract static class Builder {
		public abstract Builder name(String name);

		public abstract Builder lastName(String lastName);

		public abstract Builder height(Integer height);

		public abstract Builder weight(Integer weight);

		public abstract User build();
	}

	public static TypeAdapter<User> typeAdapter(Gson gson) {
		return new AutoValue_User.GsonTypeAdapter(gson);
	}
}
