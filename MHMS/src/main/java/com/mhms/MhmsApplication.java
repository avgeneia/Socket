package com.mhms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MhmsApplication{
	
	public static void main(String[] args) {
		//비밀번호 출력 테스트
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode("user04"));
		
		SpringApplication.run(MhmsApplication.class, args);
	}
}