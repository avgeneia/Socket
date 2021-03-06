package com.NettyBoot.Business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.NettyBoot.Common.TelegramParser;
import com.NettyBoot.VO.DataSetVO;
import com.NettyBoot.VO.HeaderVO;
import com.NettyBoot.VO.InterfaceVO;
import com.NettyBoot.VO.RowVO;

/*
 * 전문을 템플릿에 맞게 변환하는 작업을 수행하는 class
 * DataParser() : 생성자. 
 * process() : 실제로 작업을 처리하는 함수.
 * getDataSet(interface_id) : 하나의 전문에 대한 그룹데이터를 반환하는 함수.
 * 								in_data : 인터페이스ID
 */
public class DataParserBak {
	
	private String msg = null;
	
	private Map<String, Map<String, Object>> dataSet = new HashMap<String, Map<String, Object>>();
	
	List<String> interfaceList = new ArrayList<String>();
	
	boolean recursion = false;
	
	public DataParserBak(String msg) {
		
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
		int hPoz = -1;
		
		int dSize = -1; //DataSize 구분
		int dPoz = -1;
		
		for(int i = 0; i < chl.size(); i++) {
			
			if(chl.get(i).getId().equals("HeaderID")) {
				
				hPoz = chl.get(i).getPoz();
				hSize = chl.get(i).getSize();
			}
			
			if(chl.get(i).getId().equals("DataLen")) {
				
				dPoz = chl.get(i).getPoz();
				dSize = chl.get(i).getSize();
			}
		}
		
		String interfaceID = msg.substring(hPoz, hSize);
		int telSize =  Integer.parseInt(msg.substring(dPoz, dPoz + dSize));
		String telBody = msg.substring(dPoz + dSize, dPoz + dSize + telSize);
		
		//interface 처리
		/* interfaceID 매핑 구간
		 * 
		 */
		List<InterfaceVO> ifl = TelegramParser.interfaceList;
		
		String realIf = ""; //code to real interface_id
		for(int i = 0; i < ifl.size(); i++) {
			
			if(ifl.get(i).getCode().equals(interfaceID)) {
				
				realIf = ifl.get(i).getId();
				break;
			}
		}
		
		interfaceList.add(realIf);
		
		List<DataSetVO> dsl = TelegramParser.dataSetList;
		Map<String, Object> map = new HashMap<String, Object>();
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
		
		dataSet.put(realIf, map);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getDataSet(String key) {
		
		return dataSet.get(key);
	}
	
	public Map<String, Map<String, Object>> getDataSet() {
		
		return this.dataSet;
	}

	public List<String> getInterfaceList() {
		return interfaceList;
	}

	public void setInterfaceList(List<String> interfaceList) {
		this.interfaceList = interfaceList;
	}
}
