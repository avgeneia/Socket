package Common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import vo.TelegramVO;

public class TelegramParser {
	
	public TelegramParser telparser = null;
	
	static List<Map<String, String>> commHeaderList = new ArrayList<Map<String, String>>();
	static List<Map<String, String>> interfaceList = new ArrayList<Map<String, String>>();
	static List<Map<String, List<TelegramVO>>> dataSetList = new ArrayList<Map<String, List<TelegramVO>>>();
	
	public TelegramParser() throws ParserConfigurationException, SAXException, IOException {

		File file = new File(System.getProperty("user.dir") + File.separator + "xml\\PACKET.xml");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(file);
		
		// root 구하기
		Element root = doc.getDocumentElement();
				
		
		// root의 속성
		System.out.println("class name: " + root.getAttribute("CommHeader"));
		
		for(int i = 0; i < root.getChildNodes().getLength(); i++) {
			
			NodeList nodeList = root.getChildNodes().item(i).getChildNodes(); 
			
			//RULE 아래 목록을 가져와서 그 아래에 항목이 0이 아닌 것 만 순회
			if(nodeList.getLength() > 0) {
				
				String Lv1NodeName = root.getChildNodes().item(i).getNodeName();
				System.out.println("Lv1 Node :: " + Lv1NodeName);
				for(int n = 0; n < nodeList.getLength(); n++) {
					
					Node node = nodeList.item(n);
					
					if(node.getAttributes() != null) {
						
						System.out.println("  Lv2 Node :: " + node.getNodeName());
						NamedNodeMap nnm = node.getAttributes();
						
						if(Lv1NodeName.contentEquals("CommHeader")) {
							
							Map<String, String> map = new HashMap<String, String>();
							for(int j = 0; j < nnm.getLength(); j++) {
								
								String key = nnm.item(j).getNodeName();
								String value = nnm.item(j).getNodeValue();
								map.put(key, value);
							}
							
							commHeaderList.add(map);
							
						} else if(Lv1NodeName.contentEquals("Interface_id")) {
							
							Map<String, String> map = new HashMap<String, String>();
							for(int j = 0; j < nnm.getLength(); j++) {
								
								String key = nnm.item(j).getNodeName();
								String value = nnm.item(j).getNodeValue();
								map.put(key, value);
							}
							
							interfaceList.add(map);
							
						} else if(node.getNodeName().equals("DataSet")) {
							
							String InterfaceID = node.getAttributes().item(0).getChildNodes().item(0).getTextContent();
							
							Map<String, List<TelegramVO>> map = new HashMap<String, List<TelegramVO>>();
							
							List<TelegramVO> dataList = new ArrayList<TelegramVO>();
							for(int j = 0; j < node.getChildNodes().getLength(); j++) {
								
								
								Node endNode = node.getChildNodes().item(j); 
								
								TelegramVO ds = new TelegramVO();
								if(endNode.getAttributes() != null) {
									
									String id = endNode.getAttributes().item(0).getNodeValue();
									int poz = Integer.parseInt(endNode.getAttributes().item(1).getNodeValue());
									int size = Integer.parseInt(endNode.getAttributes().item(2).getNodeValue());
									
									ds.setId(id);
									ds.setSize(size);
									ds.setPoz(poz);
								}
								
								if(ds.getId() != null) {
									dataList.add(ds);
								}
							}
							map.put(InterfaceID, dataList);
							
							dataSetList.add(map);
						}
					}
				}
			}
		}
	}
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		
		TelegramParser tp = new TelegramParser();
		System.out.println("Result !!!!");
	}
}
