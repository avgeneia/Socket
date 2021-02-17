package com.NettyBoot.Common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.ini4j.Config;
import org.ini4j.Wini;
import org.springframework.core.io.ClassPathResource;

public class IniFile {
	
	/** 설정파일 관리자 */
	static IniFile ini = null;
	
	static Wini wini = null;
	
	/** 설정 파일 경로 */
	String confFilePath = "config.ini";
	
	public IniFile() throws FileNotFoundException, IOException {
		
		// 설정파일 관리자 선언
		wini = new Wini();
		
		// 파일 인코딩 성정
		Config cfg = wini.getConfig();
		cfg.setFileEncoding(Charset.defaultCharset());
		wini.setConfig(cfg);
		
		// 설정파일 로드
		
		ClassPathResource cpr = new ClassPathResource(confFilePath);
		
		File file = new File("config.ini");
		if(file.exists() == false) {
			Files.copy(cpr.getInputStream(), file.toPath());
		}
		
		wini.load(file);
	}
	
	public static IniFile getInstance() {
		
		if(ini == null) {
			
			try {
				ini = new IniFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return ini;
	}
	
	public String getIni(String section, String key) {
		
		return wini.get(section, key);
	}
	
}
