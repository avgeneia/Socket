package com.mhms.util;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.multipart.MultipartFile;

import com.mhms.security.UserContext;

public class CommUtil {
	
	//권한여부를 가져오는 함수
	public static boolean isRole(UserContext user, String role) {
		//관리자 권한을 확인해서 전체를 조회하게
		boolean auth = false;
		Iterator<GrantedAuthority> itertor = user.getAuthorities().iterator();
		while(itertor.hasNext()) {
			GrantedAuthority arg = itertor.next();
			if(arg.getAuthority().equals(role)) {
				auth = true;
				break;
			}
		}
		
		return auth;
	}
	
	//파일 업로드 함수
	public static boolean fileUpload(MultipartFile uploadFile, String filename)  {
		
	    if (uploadFile != null) {
	      
	      try {
	        // 1. FileOutputStream 사용
	        // byte[] fileData = file.getBytes();
	        // FileOutputStream output = new FileOutputStream("C:/images/" + fileName);
	        // output.write(fileData);
	         
	        // 2. File 사용
	        File file = new File(filename);
	        uploadFile.transferTo(file);
	        
	        return true;
	        
	      } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	      }
	      
	    } // if		
	    
		return false;
	}
	
	//게시글 등록 시 파일 명칭 변경시 사용하는 함수
	public static String getTransFileName(int sid, int cid, int bid) {
		
		//비밀번호 출력 테스트
		//BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
		
		//;
		
		return EncryptionUtils.encryptMD5(sid + "_" + cid + "_" + bid);
	}
	
	//사용자 객체에서 건물 소속여부를 가져오는 함수
	public static boolean getBuildUnion(UserContext user, int bid) {
		
		boolean result = false;
		
		for(int i = 0; i < user.getBid().size(); i++) {
			
			if(user.getBid().get(i) == bid) {
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	public static String convertDateFormat(String arg) {
		
	    SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");
	    
	    Date to = null;
	    
	    try {
	      to = fm.parse(arg);
	    } catch (ParseException e) {
	      e.printStackTrace();
	    }
	    
	    SimpleDateFormat fm2 = new SimpleDateFormat("yyyy.MM.dd");
	    
	    String to2 = fm2.format(to);
	    
	    return to2;
	}
}
