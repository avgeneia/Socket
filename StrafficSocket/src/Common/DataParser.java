package Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vo.TelegramVO;

public class DataParser {
	
	private String msg = null;
	
	private List<Map<String, Map<String, String>>> dataSet = new ArrayList<Map<String, Map<String, String>>>();
	
	TelegramParser tp = TelegramParser.getInstance();
	
	boolean recursion = false; //재귀호출 플래그
	
	public DataParser(String msg) {
		
		process(msg);
	}
	
	public void process(String msg) {
		
		Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
		
		/* 여러 전문 처리 시 한 건 씩 처리 후 재귀호출.
		 */
		System.out.println(" DataParser !!!");
		
		//this.msg = msg;
		
		//header 처리
		/* data 구분을 위한 처리구간.
		 * 1. header 특정
		 * 2. interface 링크 확인
		 * 3. data 분리 및 확인
		 * 주의 : 재귀호출이 필요함, 전문분석을 위한
		 *    -> 전문 분석 후 사이즈를 채크하여 재귀호출.
		 */
		
		List<Map<String, String>> chl = tp.commHeaderList;
		//[{size=6, id=H}, {size=4, id=DataLen}]
		
		int hSize = -1; //headerSize 구분
		int dSize = -1; //DataSize 구분
		
		for(int i = 0; i < chl.size(); i++) {
			
			if(chl.get(i).get("id").equals("H")) {
			
				hSize = Integer.parseInt(chl.get(i).get("size"));
			}
			
			if(chl.get(i).get("id").equals("DataLen")) {
				
				dSize = Integer.parseInt(chl.get(i).get("size"));
			}
		}
		
		String interfaceID = msg.substring(0, hSize);
		int telSize =  Integer.parseInt(msg.substring(hSize, hSize + dSize));
		String telBody = msg.substring(hSize + dSize, hSize + dSize + telSize);
		
		//전문뒤에 데이터가 더 있는지 확인. 재귀호출 플래그 처리
		if(msg.substring(hSize + dSize + telSize).length() > 0) {
			recursion = true;
		}
		
		//interface 처리
		/* interfaceID 매핑 구간
		 * 
		 */
		List<Map<String, String>> ifl = tp.interfaceList;
		
		Map<String, String> rowData = new HashMap<String, String>();
		for(int i = 0; i < ifl.size(); i++) {
			
			//현재 처리중인 인터페이스 구분
			if(interfaceID.equals(ifl.get(i).get("code"))) {
				
				String realIf = ifl.get(i).get("id");
				//data 처리
				List<Map<String, List<TelegramVO>>> dsl = tp.dataSetList;
				
				for(int n = 0; n < dsl.size(); n++) {
					
//					if(dsl.get(n).get("")) {
//						
//					}
				}
			}
		}
		
		data.put(interfaceID, rowData);
		
		/* 재귀 호출.
		 * 처리된 전문을 Replace 처리 후 진행.
		 */
		if(recursion) {
			recursion = false;
			process(msg.substring(hSize + dSize + telSize));
		}
	}
}
