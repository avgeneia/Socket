package com.mhms.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mhms.dto.CodeDto;
import com.mhms.dto.QCodeDto;
import com.mhms.security.UserContext;
import com.mhms.service.CodeService;
import com.mhms.sqlite.entities.QCode;
import com.mysema.query.jpa.impl.JPAQuery;

@Service
public class CodeServiceimpl implements CodeService{
	
	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	private DataSource dataSource;
	
	@Override
	public List<CodeDto> getCode(String upcd) {
		// TODO Auto-generated method stub

		JPAQuery query = new JPAQuery(entityManager);

		QCode code = QCode.code;
		
		List<CodeDto> codeList = query.from(code)
				                      .where(code.upr_cd.eq(upcd))
				                      .list(new QCodeDto(code.upr_cd, code.cd, code.cd_nm, code.comment, code.useyn, code.sort));
		
		return codeList;
	}

	@Override
	public List<CodeDto> uprCodeList(UserContext user) {
		// TODO Auto-generated method stub

		JPAQuery query = new JPAQuery(entityManager);
		
		QCode code = QCode.code;
		
		List<CodeDto> dto = null;
		
		query.from(code);
		query.where(code.upr_cd.eq("*"));
		query.orderBy(code.sort.asc());
		dto = query.list(new QCodeDto(code.upr_cd, code.cd, code.cd_nm, code.comment, code.useyn, code.sort));
		
		return dto;
	}
	

	@Override
	public List<CodeDto> codeList(String uprCd) {
		// TODO Auto-generated method stub

		JPAQuery query = new JPAQuery(entityManager);
		
		QCode code = QCode.code;
		
		List<CodeDto> dto = null;
		
		query.from(code);
		query.where(code.upr_cd.eq(uprCd));
		query.orderBy(code.sort.asc());
		dto = query.list(new QCodeDto(code.upr_cd, code.cd, code.cd_nm, code.comment, code.useyn, code.sort));
		
		return null;
	}
	
	@Override
	public int insertCode(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		
		Connection conn = dataSource.getConnection();
		String insertSQL = "";
		PreparedStatement pstmt = null;
		int result = 0; 
		
		String type = map.get("gbn")[0];
		
		//type 0 : 상위코드, 1 : 하위코드
		if(type.equals("upr")) {
			insertSQL = "INSERT INTO tb_code (cd, upr_cd, cd_nm, comment, sort, useyn) VALUES(?, '*', ?, ?, ?, 1)";
			pstmt = conn.prepareStatement(insertSQL);
			pstmt.setString(1, map.get("cd")[0]);
			pstmt.setString(2, map.get("cd_nm")[0]);
			pstmt.setString(3, map.get("comment")[0]);
			pstmt.setInt(4, Integer.parseInt(map.get("sort")[0]));
			result = pstmt.executeUpdate();
		} else {
			result = 0;
		}
		
		return result;
	}

	@Override
	public long updateCode(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long deleteCode(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

}
