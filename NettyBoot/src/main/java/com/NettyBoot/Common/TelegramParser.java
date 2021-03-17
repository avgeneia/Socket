package com.NettyBoot.Common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.NettyBoot.VO.DataSetVO;
import com.NettyBoot.VO.HeaderVO;
import com.NettyBoot.VO.InterfaceVO;
import com.NettyBoot.VO.RowVO;

/*
 * 전문에 대한 템플릿을 읽어오는 Class, DataParser Class를 위한 Class
 */
public class TelegramParser {
	
	public static TelegramParser telparser;
	
	public static List<HeaderVO> commHeaderList = new ArrayList<HeaderVO>();
	public static List<InterfaceVO> interfaceList = new ArrayList<InterfaceVO>();
	public static List<DataSetVO> dataSetList = new ArrayList<DataSetVO>();
	
	public static TelegramParser getInstance() {
		
		if(telparser == null) {
			
			try {
				
				telparser = new TelegramParser();
			} catch (IOException | ParserConfigurationException | SAXException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return telparser;
	}
	
	public TelegramParser() throws ParserConfigurationException, SAXException, IOException {
		
		commHeaderList.clear();
		interfaceList.clear();
		dataSetList.clear();
		
		ClassPathResource cpr = new ClassPathResource("xml" + File.separator + "PACKET.xml");

		File file = new File("PACKET.xml");
		if(file.exists() == false) {
			Files.copy(cpr.getInputStream(), file.toPath());
		}
		//File file = new File(System.getProperty("user.dir") + File.separator + "xml\\PACKET.xml");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(file);
		
		// root 구하기
		Element root = doc.getDocumentElement();
				
		// root의 속성
		for(int i = 0; i < root.getChildNodes().getLength(); i++) {
			
			NodeList nodeList = root.getChildNodes().item(i).getChildNodes(); 
			
			//RULE 아래 목록을 가져와서 그 아래에 항목이 0이 아닌 것 만 순회
			if(nodeList.getLength() > 0) {
				
				String Lv1NodeName = root.getChildNodes().item(i).getNodeName();
				for(int n = 0; n < nodeList.getLength(); n++) {
					
					Node node = nodeList.item(n);
					
					if(node.getAttributes() != null) {
						
						NamedNodeMap nnm = node.getAttributes();
						
						if(Lv1NodeName.contentEquals("CommHeader")) {
							
							HeaderVO hVO = new HeaderVO();
							hVO.setId(nnm.getNamedItem("id").getNodeValue());
							hVO.setSize(Integer.parseInt(nnm.getNamedItem("size").getNodeValue()));
							hVO.setPoz(Integer.parseInt(nnm.getNamedItem("poz").getNodeValue()));
							commHeaderList.add(hVO);
							
						} else if(Lv1NodeName.contentEquals("Interface_id")) {
							
							InterfaceVO ivo = new InterfaceVO();
							String id = fn_getItem(nnm, "id");
							ivo.setId(id);
							String code = fn_getItem(nnm, "code");
							ivo.setCode(code);
							String type = fn_getItem(nnm, "type");
							ivo.setType(type);
							String key = fn_getItem(nnm, "key");
							ivo.setKey(key);
							interfaceList.add(ivo);
							
						} else if(node.getNodeName().equals("DataSet")) {
							
							DataSetVO dsVO = new DataSetVO();
							
							String InterfaceID = node.getAttributes().item(0).getChildNodes().item(0).getTextContent();
							
							dsVO.setId(InterfaceID);
							
							List<RowVO> listVO = new ArrayList<RowVO>();
							for(int j = 0; j < node.getChildNodes().getLength(); j++) {
								
								Node endNode = node.getChildNodes().item(j); 

								RowVO rVO = new RowVO();
								if(endNode.getAttributes() != null) {
									
									String id = endNode.getAttributes().getNamedItem("id").getChildNodes().item(0).getNodeValue();
									int poz = Integer.parseInt(endNode.getAttributes().getNamedItem("poz").getChildNodes().item(0).getNodeValue());
									int size = Integer.parseInt(endNode.getAttributes().getNamedItem("size").getChildNodes().item(0).getNodeValue());
									
									rVO.setId(id);
									rVO.setSize(size);
									rVO.setPoz(poz);
								}
								
								if(rVO.getId() != null) {
									listVO.add(rVO);
								}
							}
							dsVO.setRow(listVO);
							
							dataSetList.add(dsVO);
						}
					}
				}
			}
		}
	}
	
	String fn_getItem(NamedNodeMap nnm, String key) {
		
		return nnm.getNamedItem(key)!=null?nnm.getNamedItem(key).getNodeValue():"";
	}
	
	//단위테스트용
//	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
//		
//		new TelegramParser();
//		System.out.println("Result !!!!");
//	}
}
