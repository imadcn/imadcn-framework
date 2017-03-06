package com.imadcn.demo.xml.jaxb.po;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Country {
	
	private String coutryName;
	private List<Province> provinces;
	
	public String getCoutryName() {
		return coutryName;
	}

	@XmlAttribute
	public void setCoutryName(String coutryName) {
		this.coutryName = coutryName;
	}

	public List<Province> getProvinces() {
		return provinces;
	}

	@XmlElement
	public void setProvinces(List<Province> provinces) {
		this.provinces = provinces;
	}
}
