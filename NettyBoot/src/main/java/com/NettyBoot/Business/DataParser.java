package com.NettyBoot.Business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.NettyBoot.VO.DataSetVO;
import com.NettyBoot.VO.HeaderVO;
import com.NettyBoot.VO.RowVO;

/*
 * 전문을 템플릿에 맞게 변환하는 작업을 수행하는 class
 * DataParser() : 생성자. 
 * process() : 실제로 작업을 처리하는 함수.
 * getDataSet(interface_id) : 하나의 전문에 대한 그룹데이터를 반환하는 함수.
 * 								in_data : 인터페이스ID
 */
public class DataParser {
	
	private String msg = null;
	
	private List<Map<String, Map<String, String>>> dataSet = new ArrayList<Map<String, Map<String, String>>>();
	
	List<String> interfaceList = new ArrayList<String>();
	
	public DataParser(String msg) {
		
		process(msg);
	}
	
	public void process(String msg) {
		
		//this.msg = msg;
		
		//header 처리
		/* data 구분을 위한 처리구간.
		 * 1. header 특정
		 * 2. interface 링크 확인
		 * 3. data 분리 및 확인
		 * 주의 : 재귀호출이 필요함, 전문분석을 위한
		 *    -> 전문 분석 후 사이즈를 채크하여 재귀호출.
		 */
		
		List<HeaderVO> chl = TelegramParser.commHeaderList;
		//[{size=6, id=H}, {size=4, id=DataLen}]
		
		int hSize = -1; //headerSize 구분
		int dSize = -1; //DataSize 구분
		
		for(int i = 0; i < chl.size(); i++) {
			
			if(chl.get(i).getId().equals("HeaderID")) {
			
				hSize = chl.get(i).getSize();
			}
			
			if(chl.get(i).getId().equals("DataLen")) {
				
				dSize = chl.get(i).getSize();
			}
		}
		
		String interfaceID = msg.substring(0, hSize);
		int telSize =  Integer.parseInt(msg.substring(hSize, hSize + dSize));
		String telBody = msg.substring(hSize + dSize, hSize + dSize + telSize);
		
		//전문뒤에 데이터가 더 있는지 확인. 재귀호출 플래그 처리
//		if(msg.substring(hSize + dSize + telSize).length() > 0) {
//			recursion = true;
//		}
		
		//interface 처리
		/* interfaceID 매핑 구간
		 * 
		 */
		Map<String, String> ifl = TelegramParser.interfaceList;
		
		Map<String, Map<String, String>> ds = new HashMap<String, Map<String, String>>();
		String realIf = ifl.get(interfaceID); //code to real interface_id
		
		interfaceList.add(realIf);
		
		List<DataSetVO> dsl = TelegramParser.dataSetList;
		Map<String, String> map = new HashMap<String, String>();
		for(int i = 0; i < dsl.size(); i++) {
			
			for(int j = 0; j < dsl.get(i).getRow().size(); j++) {
				RowVO row = dsl.get(i).getRow().get(j);
				
				int poz = row.getPoz();
				int size = row.getSize();
				
				String key = dsl.get(i).getRow().get(j).getId();
				String value = telBody.substring(poz, poz + size);
				
				map.put(key, value);
			}
		}
		
		ds.put(realIf, map);
		dataSet.add(ds);
		
		/* 재귀 호출.
		 * 처리된 전문을 Replace 처리 후 진행.
		 */
//		if(recursion) {
//			recursion = false;
//			process(msg.substring(hSize + dSize + telSize));
//		}
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, String> getDataSet(String key) {
		
		Map<String, String> result = new HashMap<String, String>();
		
		for(int i = 0; i < dataSet.size(); i++) {
			
			if(dataSet.get(i).get(key) != null) {
				
				result = dataSet.get(i).get(key);
			}
		}
		
		return result;
	}
	
	public List<Map<String, Map<String, String>>> getDataSet() {
		
		return this.dataSet;
	}

	public List<String> getInterfaceList() {
		return interfaceList;
	}

	public void setInterfaceList(List<String> interfaceList) {
		this.interfaceList = interfaceList;
	}
}
