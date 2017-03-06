package com.imadcn.demo.xml.jaxb;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ChInA")
public class China {

	private List<Province> provinces;

	public List<Province> getProvinces() {
		return provinces;
	}

	@XmlElement
	public void setProvinces(List<Province> provinces) {
		this.provinces = provinces;
	}
}
