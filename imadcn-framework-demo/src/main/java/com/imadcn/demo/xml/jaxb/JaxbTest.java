package com.imadcn.demo.xml.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.imadcn.demo.xml.jaxb.po.Country;
import com.imadcn.demo.xml.jaxb.po.City;
import com.imadcn.demo.xml.jaxb.po.Province;

public class JaxbTest {
	
	public Country getData() {
		Country china = new Country();
		List<Province> provincesList = new ArrayList<>();
		china.setCoutryName("China");
		china.setProvinces(provincesList);
		
		Province sichuan = new Province();
		List<City> citiesOfSichuan = new ArrayList<>();
		sichuan.setCity(citiesOfSichuan);
		City ctu = new City();
		ctu.setCityName("成都");
		ctu.setIata("CTU");
		ctu.setPostCode("610000");
		
		City lsa = new City();
		lsa.setCityName("乐山");
		lsa.setIata("LSA");
		lsa.setPostCode("614000");
		
		citiesOfSichuan.add(lsa);
		citiesOfSichuan.add(ctu);
		
		Province jiangsu = new Province();
		List<City> citiesOfJiangsu = new ArrayList<>();
		jiangsu.setCity(citiesOfJiangsu);
		City nkg = new City();
		nkg.setCityName("南京");
		nkg.setIata("NKG");
		nkg.setPostCode("210000 ");
		
		City suq = new City();
		suq.setCityName("宿迁");
		suq.setIata("SUQ");
		suq.setPostCode("223800");
		
		citiesOfJiangsu.add(nkg);
		citiesOfJiangsu.add(suq);
		
		provincesList.add(sichuan);
		provincesList.add(jiangsu);
		
		return china;
	} 
	
	public void test() throws Exception {
		JAXBContext context = JAXBContext.newInstance(Country.class, Province.class, City.class);    // 获取上下文对象  
        Marshaller marshaller = context.createMarshaller(); // 根据上下文获取marshaller对象  
          
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");  // 设置编码字符集  
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // 格式化XML输出，有分行和缩进  
          
        marshaller.marshal(getData(), System.out);   // 打印到控制台  
          
/*        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        marshaller.marshal(getData(), baos);  
        String xmlObj = new String(baos.toByteArray());         // 生成XML字符串  
        System.out.println(xmlObj);  */
	} 
	
	public static void main(String[] args) throws Exception {
		JaxbTest test = new JaxbTest();
		test.test();
	}

}
