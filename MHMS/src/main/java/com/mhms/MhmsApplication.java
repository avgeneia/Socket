package com.mhms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mhms.service.UserService;

@SpringBootApplication
public class MhmsApplication{
	
	public static void main(String[] args) {
		//비밀번호 출력 테스트
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode("admin"));
		
		SpringApplication.run(MhmsApplication.class, args);
	}
}