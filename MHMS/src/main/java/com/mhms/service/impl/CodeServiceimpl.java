package com.mhms.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mhms.dto.CodeDto;
import com.mhms.dto.QCodeDto;
import com.mhms.security.UserContext;
import com.mhms.service.CodeService;
import com.mhms.sqlite.entities.Code;
import com.mhms.sqlite.entities.QCode;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

@Service
public class CodeServiceimpl implements CodeService{
	
	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	private DataSource dataSource;

	@Override
	public Code selectCode(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		
		JPAQuery query = new JPAQuery(entityManager);

		QCode code = QCode.code;
		
		Code codeList = query.from(code)
				             .where(code.upr_cd.eq("*").and(code.cd.eq(map.get("cd")[0])))
				             .singleResult(code);
		
		return codeList;
	}
	
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
		
		return dto;
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
			insertSQL = "INSERT INTO tb_code (cd, upr_cd, cd_nm, comment, sort, useyn) VALUES(?, ?, ?, ?, ?, 1)";
			pstmt = conn.prepareStatement(insertSQL);
			pstmt.setString(1, map.get("cd")[0]);
			pstmt.setString(2, map.get("upr_cd")[0]);
			pstmt.setString(3, map.get("cd_nm")[0]);
			pstmt.setString(4, map.get("comment")[0]);
			pstmt.setInt(5, Integer.parseInt(map.get("sort")[0]));
			result = pstmt.executeUpdate();
		}
		
		return result;
	}
	
	@Transactional
	@Override
	public long updateCode(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		
		QCode code = QCode.code;
		
		JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, code);
		String type = map.get("gbn")[0];
		
		long reuslt = 0;
		if(type.equals("upr")) {
			reuslt = updateClause.set(code.cd_nm, map.get("cd_nm")[0])
								 .set(code.sort, Integer.parseInt(map.get("sort")[0]))
								 .set(code.comment, map.get("comment")[0])
								 .where(code.upr_cd.eq("*").and(code.cd.eq(map.get("cd")[0])))
								 .execute(); 
		} else {
			reuslt = updateClause.set(code.cd_nm, map.get("cd_nm")[0])
								 .set(code.sort, Integer.parseInt(map.get("sort")[0]))
								 .set(code.comment, map.get("comment")[0])
								 .where(code.upr_cd.eq("*").and(code.cd.eq(map.get("cd")[0])))
								 .execute();
		}
		
		return reuslt;
	}
	
	@Transactional
	@Override
	public long updateCodeUseyn(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		
		QCode code = QCode.code;
		
		JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, code);
		String type = map.get("gbn")[0];

		long reuslt = 0;
		if(type.equals("upr")) {
			reuslt = updateClause.set(code.useyn, Integer.parseInt(map.get("useyn")[0]))
								 .where(code.upr_cd.eq("*").and(code.cd.eq(map.get("cd")[0])))
								 .execute(); 
		} else {
			reuslt = updateClause.set(code.useyn, Integer.parseInt(map.get("useyn")[0]))
								 .where(code.upr_cd.eq("*").and(code.cd.eq(map.get("cd")[0])))
								 .execute();
		}
		
		return reuslt;
	}
	
	@Transactional
	@Override
	public long deleteCode(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub

		QCode code = QCode.code;
		
		JPADeleteClause deleteClause = new JPADeleteClause(entityManager, code);
		long result = 0;
		if(map.get("gbn")[0].equals("upr")) {
			result = deleteClause.where(code.upr_cd.eq("*").and(code.cd.eq(map.get("cd")[0]))).execute();
		} else {
			result = deleteClause.where(code.upr_cd.eq(map.get("upr_cd")[0]).and(code.cd.eq(map.get("cd")[0]))).execute();
		}
		
		return result;
	}

}
