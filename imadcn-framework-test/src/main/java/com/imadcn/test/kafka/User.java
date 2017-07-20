package com.imadcn.test.kafka;

import java.io.Serializable;

public class User implements Serializable {

	//private static final long serialVersionUID = -955751285994453568L;
//	private static final long serialVersionUID = 3549533626832858993L;

	private String name;
	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", age=" + age + "]";
	}
}
