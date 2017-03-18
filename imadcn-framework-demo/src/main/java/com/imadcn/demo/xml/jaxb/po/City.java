package com.imadcn.demo.xml.jaxb.po;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class City {

	private String iata;
	private String cityName;
	private String postCode;
	
	public String getIata() {
		return iata;
	}

	@XmlAttribute(name="iAtA")
	public void setIata(String iata) {
		this.iata = iata;
	}

	public String getCityName() {
		return cityName;
	}

	@XmlElement(name="city_name")
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getPostCode() {
		return postCode;
	}

	@XmlElement
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
}
