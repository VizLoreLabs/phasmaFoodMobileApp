
package com.vizlore.phasmafood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Subcase {

	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("subproblems")
	@Expose
	private List<Subproblem> subproblems = null;

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

	public List<Subproblem> getSubproblems() {
		return subproblems;
	}

	public void setSubproblems(List<Subproblem> subproblems) {
		this.subproblems = subproblems;
	}

}
