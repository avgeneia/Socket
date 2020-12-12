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

import com.mhms.dto.NoticeDto;
import com.mhms.dto.QNoticeDto;
import com.mhms.security.UserContext;
import com.mhms.service.NoticeService;
import com.mhms.sqlite.entities.Notice;
import com.mhms.sqlite.entities.QNotice;
import com.mhms.sqlite.entities.QUserRole;
import com.mhms.util.CommUtil;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

@Service
public class NoticeServiceImpl implements NoticeService {
	
	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	private DataSource dataSource;

	@Override
	public Notice selectNotice(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public List<NoticeDto> BBSList(UserContext user) {
		// TODO Auto-generated method stub
		JPAQuery query = new JPAQuery(entityManager);
		QNotice notice = QNotice.notice;
		QUserRole userRole = QUserRole.userRole;
		
		boolean auth = CommUtil.getAuth(user);
		
		query.from(notice);
		if(!auth) {
			query.join(userRole).on(notice.bid.eq(userRole.building.bid));
			query.where(userRole.building.bid.in(user.getBid()).and(notice.notice_lv.eq(0)));
		} else {
			query.where(notice.notice_lv.eq(0));
		}
		
		List<NoticeDto> dto = query.list(new QNoticeDto(notice.sid, notice.bid, notice.title, notice.notice_lv, notice.viewcnt, notice.content, notice.filename, notice.writer, notice.writedate));
		
		return dto;
	}

	@Override
	public int insertNotice(Map<String, String[]> map, UserContext user) throws SQLException {
		// TODO Auto-generated method stub
		
		Connection conn = dataSource.getConnection();
		String insertSQL = "";
		PreparedStatement pstmt = null;
		
		JPAQuery query = new JPAQuery(entityManager);
		QNotice notice = QNotice.notice;
		
		query.from(notice);
		query.orderBy(notice.sid.desc());
		Notice dto = query.singleResult(notice);
		
		int maxSid = 0;
		if(dto == null) {
			maxSid = 1;
		} else {
			maxSid = dto.getSid() > 0? dto.getSid() + 1 : 1;
		}
		
		insertSQL = "INSERT INTO tb_notice (bid, sid, content, link, notice, title, viewcnt, write_date, writer, cid) VALUES(?, ?, ?, ?, ?, ?, 0, strftime(\"%Y%m%d\",'now','localtime'), ?, 0)";

		pstmt = conn.prepareStatement(insertSQL);
		pstmt.setInt(1, Integer.parseInt(map.get("bid")[0]));
		pstmt.setInt(2, maxSid>0?maxSid + 1: 1);
		pstmt.setString(3, map.get("content")[0]);
		pstmt.setString(4, map.get("link")[0]);
		pstmt.setInt(5, Integer.parseInt(map.get("notice")[0]));
		pstmt.setString(6, map.get("title")[0]);
		pstmt.setString(7, user.getUsername());
		pstmt.executeUpdate();
		 
		return maxSid;
	}
	
	@Transactional
	@Override
	public void updateFile(Map<String, String[]> map, String filename, int sid) throws SQLException {
		// TODO Auto-generated method stub
		
		QNotice notice = QNotice.notice;
		JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, notice);
		
		updateClause.set(notice.filename, filename)
		            .where(notice.sid.eq(sid).and(notice.cid.eq(0)))
		            .execute();
		            
	}
	
	@Transactional
	@Override
	public long updateNotice(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Transactional
	@Override
	public long deleteNotice(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

}
