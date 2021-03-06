package com.vizlore.phasmafood.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Stevan Medic
 * <p>
 * Created on Dec 2018
 * <p>
 * A generic class that contains data and status about loading this data.
 */
public class Resource<T> {
	@NonNull
	public final Status status;
	@Nullable
	public final T data;
	@Nullable
	public final String message;

	private Resource(@NonNull Status status, @Nullable T data,
					 @Nullable String message) {
		this.status = status;
		this.data = data;
		this.message = message;
	}

	public static <T> Resource<T> success(@NonNull T data) {
		return new Resource<>(Status.SUCCESS, data, null);
	}

	public static <T> Resource<T> error(String msg, @Nullable T data) {
		return new Resource<>(Status.ERROR, data, msg);
	}

	public static <T> Resource<T> loading(@Nullable T data) {
		return new Resource<>(Status.LOADING, data, null);
	}

	public enum Status {SUCCESS, ERROR, LOADING}
}
