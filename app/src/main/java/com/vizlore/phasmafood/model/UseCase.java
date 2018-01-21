
package com.vizlore.phasmafood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UseCase {

	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("subcases")
	@Expose
	private List<Subcase> subcases = null;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Subcase> getSubcases() {
		return subcases;
	}

	public void setSubcases(List<Subcase> subcases) {
		this.subcases = subcases;
	}

}
