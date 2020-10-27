package com.mhms.sqlite.config;

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
		
		try {
			
			Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			
			/*USER 테이블 확인 및 생성, ADMIN 계정 주입*/
			statement.execute("DROP TABLE IF EXISTS TB_USER");
			
			statement.executeUpdate(
					"CREATE TABLE TB_USER (" +
					"UID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
					"USER_ID TEXT NOT NULL UNIQUE, " +
					"USER_PW TEXT NOT NULL UNIQUE, " +
					"ISDEL INTEGER DEFAULT 0 NOT NULL)"
					);
			
			statement.execute("DROP TABLE IF EXISTS tb_building");
			
			statement.executeUpdate(
					"CREATE TABLE \"tb_building\" (\n"
					+ "	\"bid\"	integer NOT NULL,\n"
					+ "	\"bnm\"	varchar,\n"
					+ "	\"rid\"	integer NOT NULL,\n"
					+ "	\"rnm\"	varchar,\n"
					+ "	PRIMARY KEY(\"bid\",\"rid\")\n"
					+ ")"
					);
			
			statement.execute("DROP TABLE IF EXISTS TB_CODE");
			
			statement.executeUpdate(
					"CREATE TABLE TB_CODE ( " +
					"UPR_CD TEXT NOT NULL UNIQUE, " +
					"CD TEXT NOT NULL UNIQUE, " +
					"CD_NM TEXT, " +
					"COMMENT TEXT, " +
					"SORT INTEGER, " +
					"ISDEL INTEGER DEFAULT 0, " +
					"CONSTRAINT TB_CODE_PK PRIMARY KEY (UPR_CD,CD)) "
					);
			
			statement.execute("DROP TABLE IF EXISTS TB_BBS");
			
			statement.executeUpdate(
					"CREATE TABLE \"TB_BBS\" (\n"
					+ "	\"SID\"	INTEGER NOT NULL UNIQUE,\n"
					+ "	\"CID\"	INTEGER NOT NULL UNIQUE,\n"
					+ "	\"BID\"	INTEGER NOT NULL UNIQUE,\n"
					+ "	\"TITLE\"	TEXT,\n"
					+ "	\"CONTENT\"	TEXT,\n"
					+ "	\"LINK\"	TEXT,\n"
					+ "	\"VIEWCNT\"	INTEGER,\n"
					+ "	\"NOTICE\"	INTEGER,\n"
					+ "	\"WRITER\"	TEXT,\n"
					+ "	\"WRITEDATE\"	TEXT,\n"
					+ "	FOREIGN KEY(\"BID\") REFERENCES \"TB_BUILDING\"(\"BID\"),\n"
					+ "	PRIMARY KEY(\"SID\",\"CID\",\"BID\")\n"
					+ ")"
					);
			
					/*
					 * statement.execute("DROP TABLE IF EXISTS TB_USERROLE");
					 * 
					 * statement.executeUpdate( "CREATE TABLE TB_USERROLE (\n" +
					 * "					UID INTEGER NOT NULL UNIQUE,\n" +
					 * "					BID INTEGER NOT NULL UNIQUE,\n" +
					 * "					RID INTEGER NOT NULL UNIQUE,\n" +
					 * "					AUTHLV INTEGER,\n" + "					COMMENT TEXT,\n"
					 * + "					WRITER TEXT,\n" + "					WRITERDT TEXT,\n" +
					 * "					CONSTRAINT TB_USERROLE_PK PRIMARY KEY (UID,BID,RID),\n"
					 * +
					 * "					CONSTRAINT TB_USERROLE_FK FOREIGN KEY (UID) REFERENCES TB_USER(UID),\n"
					 * +
					 * "					CONSTRAINT TB_USERROLE_FK_1 FOREIGN KEY (BID,RID) REFERENCES TB_BUILDING(BID,RID)"
					 * );
					 */
			/*
			 *
				INSERT INTO TB_USER
				(USER_ID, USER_PW, ISDEL)
				VALUES('ADMIN', '', 0);
				
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
				
				

			 * */
			
			statement.close();
			connection.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		} 
	}
}
