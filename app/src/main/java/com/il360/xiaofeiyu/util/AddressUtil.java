package com.il360.xiaofeiyu.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.il360.xiaofeiyu.common.MyApplication;

import android.util.Log;

public class AddressUtil {
	private static List<String> provinceList = null;
	private static List<String> cityList = null;
	private static List<String> districtList = null;

//	static {
//		InputStream ins = null;
//		try {
//			ins = MyApplication.getContextObject().getAssets().open("address.xml");
//			parse(ins);
//		} catch (Exception e) {
//			Log.e("AddressUtil", "AddressUtil", e);
//		}
//
//		if (ins != null) {
//			try {
//				ins.close();
//			} catch (IOException e) {
//			}
//		}
//	}
//	
//	private static void parse(InputStream in)
//			throws ParserConfigurationException, SAXException, IOException {
//
//		final DocumentBuilderFactory docfact = DocumentBuilderFactory
//				.newInstance();
//		DocumentBuilder docb = null;
//		docb = docfact.newDocumentBuilder();
//
//		Document doc = docb.parse(in);
//		Element rootElement = doc.getDocumentElement();
//		NodeList provinceNodeList = rootElement.getElementsByTagName("Province");
//		for (int i = 0; i < provinceNodeList.getLength(); i++) {
//			Element provinceNode = (Element) provinceNodeList.item(i);
////			addressList.add(provinceNode.getAttribute("Name"));
//			if(provinceNode.getAttribute("Name").equals("台湾")){
//				NodeList cityNodeList = provinceNode.getElementsByTagName("City");
//				for (int j = 0; j < cityNodeList.getLength(); j++) {
//					Element cityNode = (Element) cityNodeList.item(j);
////					addressList2.add(cityNode.getAttribute("Name"));
//					
//					if(cityNode.getAttribute("Name").equals("连江县")){
//						NodeList districtNodeList = cityNode.getElementsByTagName("District");
//						for (int k = 0; k < districtNodeList.getLength(); k++) {
//							Element districtNode = (Element) districtNodeList.item(k);
//							districtList.add(districtNode.getAttribute("Name"));
//						}
//					}
//				}
//			}
//		}
//	}
	
	public static List<String> getProvinceList(){
		provinceList = new ArrayList<String>();
		
		InputStream ins = null;
		try {
			ins = MyApplication.getContextObject().getAssets().open("address.xml");
			parseProvince(ins);
		} catch (Exception e) {
			Log.e("AddressUtil", "AddressUtil", e);
		}

		if (ins != null) {
			try {
				ins.close();
			} catch (IOException e) {
			}
		}
		
		return provinceList;
	}
	
	
	private static void parseProvince(InputStream in) throws ParserConfigurationException, SAXException, IOException {

		final DocumentBuilderFactory docfact = DocumentBuilderFactory.newInstance();
		DocumentBuilder docb = null;
		docb = docfact.newDocumentBuilder();

		Document doc = docb.parse(in);
		Element rootElement = doc.getDocumentElement();
		NodeList provinceNodeList = rootElement.getElementsByTagName("Province");
		for (int i = 0; i < provinceNodeList.getLength(); i++) {
			Element provinceNode = (Element) provinceNodeList.item(i);
			provinceList.add(provinceNode.getAttribute("Name"));
		}
	}
	
	public static List<String> getCityList(String province) {
		cityList = new ArrayList<String>();

		InputStream ins = null;
		try {
			ins = MyApplication.getContextObject().getAssets().open("address.xml");
			parseCity(ins, province);
		} catch (Exception e) {
			Log.e("AddressUtil", "AddressUtil", e);
		}

		if (ins != null) {
			try {
				ins.close();
			} catch (IOException e) {
			}
		}

		return cityList;

	}
	
	private static void parseCity(InputStream in, String province) throws ParserConfigurationException, SAXException, IOException {

		final DocumentBuilderFactory docfact = DocumentBuilderFactory.newInstance();
		DocumentBuilder docb = null;
		docb = docfact.newDocumentBuilder();

		Document doc = docb.parse(in);
		Element rootElement = doc.getDocumentElement();
		NodeList provinceNodeList = rootElement.getElementsByTagName("Province");
		for (int i = 0; i < provinceNodeList.getLength(); i++) {
			Element provinceNode = (Element) provinceNodeList.item(i);
			if (provinceNode.getAttribute("Name").equals(province)) {
				NodeList cityNodeList = provinceNode.getElementsByTagName("City");
				for (int j = 0; j < cityNodeList.getLength(); j++) {
					Element cityNode = (Element) cityNodeList.item(j);
					cityList.add(cityNode.getAttribute("Name"));
				}
				return;
			}
		}
	}
	
	
	public static List<String> getDistrictList(String province, String city) {
		districtList = new ArrayList<String>();

		InputStream ins = null;
		try {
			ins = MyApplication.getContextObject().getAssets().open("address.xml");
			parseDistrict(ins, province, city);
		} catch (Exception e) {
			Log.e("AddressUtil", "AddressUtil", e);
		}

		if (ins != null) {
			try {
				ins.close();
			} catch (IOException e) {
			}
		}

		return districtList;
	}
	
	private static void parseDistrict(InputStream in, String province, String city) throws ParserConfigurationException, SAXException, IOException {

		final DocumentBuilderFactory docfact = DocumentBuilderFactory.newInstance();
		DocumentBuilder docb = null;
		docb = docfact.newDocumentBuilder();

		Document doc = docb.parse(in);
		Element rootElement = doc.getDocumentElement();
		NodeList provinceNodeList = rootElement.getElementsByTagName("Province");
		for (int i = 0; i < provinceNodeList.getLength(); i++) {
			Element provinceNode = (Element) provinceNodeList.item(i);
			if (provinceNode.getAttribute("Name").equals(province)) {
				NodeList cityNodeList = provinceNode.getElementsByTagName("City");
				for (int j = 0; j < cityNodeList.getLength(); j++) {
					Element cityNode = (Element) cityNodeList.item(j);
					if (cityNode.getAttribute("Name").equals(city)) {
						NodeList districtNodeList = cityNode.getElementsByTagName("District");
						for (int k = 0; k < districtNodeList.getLength(); k++) {
							Element districtNode = (Element) districtNodeList.item(k);
							districtList.add(districtNode.getAttribute("Name"));
						}
						return;
					}
				}
			}
		}
	}
	
}
