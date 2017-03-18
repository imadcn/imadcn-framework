package com.imadcn.demo.xml.jaxb.po;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Province {

	private List<City> city;

	public List<City> getCity() {
		return city;
	}

	@XmlElement(name="CitY")
	public void setCity(List<City> city) {
		this.city = city;
	}
}

