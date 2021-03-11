package com.NettyBoot.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.NettyBoot.Common.TelegramParser;
import com.NettyBoot.VO.HeaderVO;
import com.NettyBoot.VO.InterfaceVO;

public class TelegramSpliter {
	
	private static List<Map<String, String>> msgList = new ArrayList<Map<String, String>>();
	
	public TelegramSpliter() {
		
		TelegramParser.getInstance();
		
	}
	
	public List<Map<String, String>> getParser(String msg) {

		msgList.clear();
		
		process(msg);
		
		return msgList;
	}
	
	public void process(String msg) {

		String telegramBody = msg;
		
		List<HeaderVO> chl = TelegramParser.commHeaderList;

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
		
		//전문의 본문사이즈를 저장.
		int telSize =  Integer.parseInt(telegramBody.substring(dPoz, dPoz + dSize));
		
		//한 개의 전문을 구함.
		String arg = telegramBody.substring(hPoz, dPoz + dSize +telSize);
		
		Map<String, String> rowData = new HashMap<String, String>();
		
		List<InterfaceVO> ifl = TelegramParser.interfaceList;
		
		for(int i = 0; i < ifl.size(); i++) {
			
			if(ifl.get(i).getCode().equals(interfaceID)) {

				rowData.put("TYPE", ifl.get(i).getType());
				rowData.put("KEY", ifl.get(i).getKey());
				break;
			}
		}
		
		rowData.put("MSG", arg);
		
		//전문 단건을 저장.
		msgList.add(rowData);
		
		//저장 후 남아있는 전문이 있으면 재귀처리하여 저장한다.
		arg = telegramBody.replace(arg, "");
		
		if(arg.length() > 0) {
			process(arg);
		}
		
	}
}
