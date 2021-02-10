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

import VO.DataSetVO;
import VO.HeaderVO;
import VO.RowVO;

public class TelegramParser {
	
	public static TelegramParser telparser;
	
	static List<HeaderVO> commHeaderList = new ArrayList<HeaderVO>();
	static Map<String, String> interfaceList = new HashMap<String, String>();
	static List<DataSetVO> dataSetList = new ArrayList<DataSetVO>();
	
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
		
		System.out.println(" new TelegramParser !!!!!");
		
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
							
							HeaderVO hVO = new HeaderVO();
							hVO.setId(nnm.getNamedItem("id").getNodeValue());
							hVO.setSize(Integer.parseInt(nnm.getNamedItem("size").getNodeValue()));
							commHeaderList.add(hVO);
							
						} else if(Lv1NodeName.contentEquals("Interface_id")) {
							
							String key = nnm.getNamedItem("code").getNodeValue();
							String value = nnm.getNamedItem("id").getNodeValue();
							interfaceList.put(key, value);
							
						} else if(node.getNodeName().equals("DataSet")) {
							
							DataSetVO dsVO = new DataSetVO();
							
							String InterfaceID = node.getAttributes().item(0).getChildNodes().item(0).getTextContent();
							
							dsVO.setId(InterfaceID);
							
							List<RowVO> listVO = new ArrayList<RowVO>();
							for(int j = 0; j < node.getChildNodes().getLength(); j++) {
								
								Node endNode = node.getChildNodes().item(j); 

								RowVO rVO = new RowVO();
								if(endNode.getAttributes() != null) {
									
									String id = endNode.getAttributes().item(0).getNodeValue();
									int poz = Integer.parseInt(endNode.getAttributes().item(1).getNodeValue());
									int size = Integer.parseInt(endNode.getAttributes().item(2).getNodeValue());
									
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
	
	//단위테스트용
//	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
//		
//		new TelegramParser();
//		System.out.println("Result !!!!");
//	}
}
