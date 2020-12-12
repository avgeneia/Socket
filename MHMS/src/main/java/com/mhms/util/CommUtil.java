package com.mhms.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.multipart.MultipartFile;

import com.mhms.security.UserContext;

public class CommUtil {
	
	public static boolean getAuth(UserContext user) {
		//관리자 권한을 확인해서 전체를 조회하게
		boolean auth = false;
		Iterator<GrantedAuthority> itertor = user.getAuthorities().iterator();
		while(itertor.hasNext()) {
			GrantedAuthority arg = itertor.next();
			if(arg.getAuthority().equals("ROLE_ADMIN")) {
				auth = true;
				break;
			}
		}
		
		return auth;
	}
	
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

	public static String getTransFileName(int sid) {
		
		return String.valueOf(sid + (int) (new Date().getTime()/1000));
	}
}
