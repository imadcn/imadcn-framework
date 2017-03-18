package com.imadcn.framework.config;

public class Overwatch {
	private Integer id;
	private String name;
	private Heroes type;

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

	public Heroes getType() {
		return type;
	}

	public void setType(Heroes type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Overwatch [id=" + id + ", name=" + name + ", type=" + type + "]";
	}
}
