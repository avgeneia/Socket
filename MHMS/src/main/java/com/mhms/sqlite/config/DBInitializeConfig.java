package com.mhms.sqlite.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBInitializeConfig {

	@Autowired
	private DataSource dataSource;
	
	@PostConstruct
	public void initialize(){
		
		InetAddress local;
		
		try {
			
			Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			
			/*USER 테이블 확인 및 생성, ADMIN 계정 주입*/
			/*
			 * statement.execute("DROP TABLE IF EXISTS TB_USER");
			 * 
			 * statement.executeUpdate( "CREATE TABLE TB_USER (" +
			 * "UID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
			 * "USER_ID TEXT NOT NULL, " + "USER_PW TEXT NOT NULL, " +
			 * "ISDEL INTEGER DEFAULT 0 NOT NULL)" );
			 * 
			 * local = InetAddress.getLocalHost(); String ip = local.getHostAddress();
			 * 
			 * System.out.println();
			 * 
			 * statement.executeUpdate( "INSERT INTO TB_USER" + "(USER_ID, USER_PW, ISDEL)"
			 * + "VALUES('ADMIN', " + "ADMIN" + ip + ", 0)" );
			 */
			/*
			 * CREATE TABLE TB_USER (
					UID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
					USER_ID TEXT NOT NULL UNIQUE,
					USER_PW TEXT NOT NULL UNIQUE,
					ISDEL INTEGER DEFAULT 0 NOT NULL
				);
				
				INSERT INTO TB_USER
				(USER_ID, USER_PW, ISDEL)
				VALUES('ADMIN', '', 0);
				
				CREATE TABLE TB_BUILDING (
					BID INTEGER NOT NULL UNIQUE,
					RID INTEGER NOT NULL UNIQUE,
					BNM TEXT,
					RNM TEXT,
					CONSTRAINT TB_BUILDING_PK PRIMARY KEY (BID,RID)
				);

				CREATE TABLE TB_CODE (
					UPR_CD TEXT NOT NULL UNIQUE,
					CD TEXT NOT NULL UNIQUE,
					CD_NM TEXT,
					COMMENT TEXT,
					SORT INTEGER,
					ISDEL INTEGER DEFAULT 0,
					CONSTRAINT TB_CODE_PK PRIMARY KEY (UPR_CD,CD)
				);
				
				CREATE TABLE TB_USERROLE (
					UID INTEGER NOT NULL UNIQUE,
					BID INTEGER NOT NULL UNIQUE,
					RID INTEGER NOT NULL UNIQUE,
					AUTHLV INTEGER,
					COMMENT TEXT,
					WRITER TEXT,
					WRITERDT TEXT,
					CONSTRAINT TB_USERROLE_PK PRIMARY KEY (UID,BID,RID),
					CONSTRAINT TB_USERROLE_FK FOREIGN KEY (UID) REFERENCES TB_USER(UID),
					CONSTRAINT TB_USERROLE_FK_1 FOREIGN KEY (BID,RID) REFERENCES TB_BUILDING(BID,RID)
				);
				
				CREATE TABLE TB_BBS (
					SID	INTEGER NOT NULL UNIQUE,
					CID	INTEGER NOT NULL UNIQUE,
					BID	INTEGER NOT NULL UNIQUE,
					TITLE	TEXT,
					CONTENT	TEXT,
					LINK	TEXT,
					VIEWCNT	INTEGER,
					NOTICE	INTEGER,
					WRITER	TEXT,
					WRITEDATE	TEXT,
					CONSTRAINT TB_BBS_PK PRIMARY KEY (UID,BID,RID),
					CONSTRAINT TB_BBS_FK FOREIGN KEY (UID) REFERENCES TB_USER(UID)
				)

			 * */
			
			
			statement.executeUpdate(
					"INSERT INTO UserLogin " +
					"(userName,password,firstName,lastName,email,mobile) " +
					"VALUES " + "('bharat0126','dbase123','Bharat','Verma',"
						+ " 'bharatverma2488@gmail.com','8861456151')"
					);
			statement.close();
			connection.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
