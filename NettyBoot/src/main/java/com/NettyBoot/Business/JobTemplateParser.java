package com.NettyBoot.Business;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.NettyBoot.VO.JobVO;

public class JobTemplateParser {
	
	public static JobTemplateParser jp = null;
	
	private List<JobVO> jobTemplate = new ArrayList<JobVO>();
	
	//job template 파일
	final String filePath = "xml" + File.separator + "JOB.xml";
	
	public static JobTemplateParser getInstance() {
		
		if(jp == null) {
			
			try {
				jp = new JobTemplateParser();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return jp;
	}
	
	public JobTemplateParser() throws Exception {
		
		ClassPathResource cpr = new ClassPathResource(filePath);

		File file = new File("JOB.xml");
		if(file.exists() == false) { 
			Files.copy(cpr.getInputStream(), file.toPath());
		}
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(file);
		
		// root 구하기
		Element root = doc.getDocumentElement();
		
		//root 속성
		for(int i = 0; i < root.getChildNodes().getLength(); i++) {
			
			NodeList nodeList = root.getChildNodes().item(i).getChildNodes(); 
			
			//RULE 아래 목록을 가져와서 그 아래에 항목이 0이 아닌 것 만 순회
			if(nodeList.getLength() > 0) {
				
				String Lv1NodeName = root.getChildNodes().item(i).getNodeName();
				for(int n = 0; n < nodeList.getLength(); n++) {
					
					Node node = nodeList.item(n);
					
					if(node.getAttributes() != null) {
						
						if(Lv1NodeName.contentEquals("JobGroup")) {
							
							String id = node.getAttributes().item(0).getNodeValue();
							
							JobVO vo = new JobVO();
							vo.setId(id);
							List<Map<String, String>> ifdata = new ArrayList<Map<String, String>>();
							for(int j = 0; j < node.getChildNodes().getLength(); j++) {
								
								if(node.getChildNodes().item(j).getAttributes() == null) {
									continue;
								}
								
								NamedNodeMap nnm = node.getChildNodes().item(j).getAttributes();
								
								Map<String, String> att = new HashMap<String, String>();
								for(int k = 0; k < nnm.getLength(); k++) {
									
									String key = nnm.item(k).getNodeName();
									String value = nnm.item(k).getNodeValue();
									att.put(key, value);
								}
								ifdata.add(att);
							}
							vo.setRowdata(ifdata);
							jobTemplate.add(vo);
						}
					}
				}
			}
		}
	}

	public List<JobVO> getJobTemplate() {
		return jobTemplate;
	}

	public void setJobTemplate(List<JobVO> jobTemplate) {
		this.jobTemplate = jobTemplate;
	}
	
	public JobVO getJobSchedule(String id) {
		
		for(int i = 0; i < jobTemplate.size(); i++) {
			
			String jobId = jobTemplate.get(i).getId();
			
			if(jobId.equals(id)) {
				return jobTemplate.get(i);
			} 
		}
		
		return null;
	}
	
	/*
	 * 필요한 Thread 갯수를 리턴
	 */
	public int getThreadCnt() {
		
		int result = 0;
		
		for(int i = 0; i < jobTemplate.size(); i++) {
			
			result += jobTemplate.get(i).getThreadCnt();
		}
		
		return result;
	}
}
