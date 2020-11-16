package com.mhms.sqlite.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBInitializeConfig {

	@Autowired
	private DataSource dataSource;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String init;
	
	@PostConstruct
	public void initialize(){
		
		try {
			
			Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			
//			if(init.equals("update")) {
//				statement.executeUpdate(
//						"INSERT INTO tb_user\n"
//						+ "(uid, user_nm, user_pw, isdel, role)\n"
//						+ "VALUES(0, 'admin', '$2a$10$diCRhQWlcTulrMxenKt6feBXXTfsonwICjbTklE8gc0JE4du8gLmi', 0, 'ROLE_ADMIN')"
//						);
//			}
			
			
			statement.close();
			connection.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		} 
	}
}
