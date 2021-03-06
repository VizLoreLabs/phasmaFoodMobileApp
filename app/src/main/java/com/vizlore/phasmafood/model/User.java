
package com.vizlore.phasmafood.model;


import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

@AutoValue
public abstract class User {

	@SerializedName("first_name")
	public abstract String firstName();

	@SerializedName("last_name")
	public abstract String lastName();

	public abstract String email();

	public abstract String username();

	public abstract String company();

	public abstract String id();

	@Nullable
	public abstract String type();

	public static Builder builder() {
		return new AutoValue_User.Builder();
	}

	@AutoValue.Builder
	public abstract static class Builder {
		public abstract Builder firstName(String firstName);

		public abstract Builder lastName(String lastName);

		public abstract Builder username(String username);

		public abstract Builder company(String company);

		public abstract Builder id(String id);

		public abstract Builder email(String email);

		public abstract Builder type(String type);

		public abstract User build();
	}

	public static TypeAdapter<User> typeAdapter(Gson gson) {
		return new AutoValue_User.GsonTypeAdapter(gson);
	}
}
