package com.mhms.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import com.mhms.dto.CodeDto;
import com.mhms.dto.QCodeDto;
import com.mhms.service.CodeService;
import com.mhms.sqlite.entities.QCode;
import com.mysema.query.jpa.impl.JPAQuery;

@Service
public class CodeServiceimpl implements CodeService{
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public List<CodeDto> getCode(String upcd) {
		// TODO Auto-generated method stub

		JPAQuery query = new JPAQuery(entityManager);

		QCode code = QCode.code;
		
		List<CodeDto> codeList = query.from(code)
				                      .where(code.upr_cd.eq(upcd))
				                      .list(new QCodeDto(code.upr_cd, code.cd, code.cd_nm, code.comment, code.isdel, code.sort));
		
		return codeList;
	}

}
