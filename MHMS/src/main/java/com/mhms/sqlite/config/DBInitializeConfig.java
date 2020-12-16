package com.mhms.sqlite.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.mhms.dto.UserListDto;
import com.mhms.service.UserService;

@Configuration
public class DBInitializeConfig {

	@Autowired
	private UserService userService;
	
	@Autowired
	private DataSource dataSource;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String init;
	
	@PostConstruct
	public void initialize(){
		
		try {
			
			List<UserListDto> user = userService.selectUser(0);
			
			if(user == null) {
				Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement();
				
				statement.execute("DROP TABLE IF EXISTS tb_user");
				String insertSQL = "INSERT INTO tb_user (uid, \"role\", usernm, userpw, useyn) VALUES(0, 'ROLE_ADMIN', 'admin', '$2a$10$ntUtSk9QO88aSFHq1p411O/T8Pyax4aU/xP4JsGMVIcJR9pHLCoiO', 1)";
				statement.executeUpdate(insertSQL);
				
				statement.execute("DROP TABLE IF EXISTS tb_userrole");
				insertSQL = "INSERT INTO tb_userrole (sid, comment, \"role\", writer, writerdate, bid, rid, uid) VALUES(0, '관리자', 'ROLE_ADMIN', 'system', '', 0, 0, 0)";
				statement.executeUpdate(insertSQL);
				
				statement.close();
				connection.close();
			}
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		} 
	}
}
