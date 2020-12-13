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
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

@Service
public class NoticeServiceImpl implements NoticeService {
	
	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	private DataSource dataSource;

	@Override
	public Notice selectNotice(Map<String, String[]> map, UserContext user) throws SQLException {
		// TODO Auto-generated method stub
		JPAQuery query = new JPAQuery(entityManager);
		QNotice notice = QNotice.notice;

		boolean auth = CommUtil.getAuth(user);
		
		query.from(notice);
		query.where(notice.sid.eq(Integer.parseInt(map.get("sid")[0])).and(notice.cid.eq(Integer.parseInt(map.get("cid")[0]))));
		Notice result = query.singleResult(notice);
		
		/* L1 : 작성자가 로그인사용자일 때 수정권한을 부여한다.
		 * L2 : 현재 로그인 사용자가 시스탬 관리자일 경우 수정권한을 부여한다.
		 * L3 : 현재 로그인 사용자가 해당 글의 매니저일 경우 수정권한을 부여한다.
		 * */
		if(result.getWriter().equals(user.getUsername())
		|| auth
		|| CommUtil.getBuildUnion(user, result.getBid())) {
			result.setIsupdate(1);;
		}
		
		return result;
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
		
		List<NoticeDto> dto = query.list(new QNoticeDto(notice.sid, notice.cid, notice.bid, notice.title, notice.notice_lv, notice.viewcnt, notice.content, notice.filename, notice.writer, notice.writedate));
		
		for(int i = 0; i <  dto.size(); i++) {
			
			/* L1 : 작성자가 로그인사용자일 때 수정권한을 부여한다.
			 * L2 : 현재 로그인 사용자가 시스탬 관리자일 경우 수정권한을 부여한다.
			 * L3 : 현재 로그인 사용자가 해당 글의 매니저일 경우 수정권한을 부여한다.
			 * */
			if(dto.get(i).getWriter().equals(user.getUsername())
			|| auth
			|| CommUtil.getBuildUnion(user, dto.get(i).getBid())) {
				dto.get(i).setIsupdate(1);
			}
		}
		
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
		pstmt.setInt(2, maxSid);
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
		
		JPAQuery query = new JPAQuery(entityManager);
		Notice noticeDto = query.from(notice).where(notice.sid.eq(sid).and(notice.cid.eq(0)).and(notice.bid.eq(Integer.parseInt(map.get("bid")[0])))).singleResult(notice);
		
		long result = updateClause.set(notice.filename, filename)
					              .where(notice.sid.eq(sid).and(notice.cid.eq(0)).and(notice.bid.eq(Integer.parseInt(map.get("bid")[0]))))
					              .execute();
		
		System.out.println("result :: " + result);
		            
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
		
		QNotice notice = QNotice.notice;
		JPADeleteClause deleteClause = new JPADeleteClause(entityManager, notice);
		long result = deleteClause.where(notice.sid.eq(Integer.parseInt(map.get("sid")[0])).and(notice.cid.eq(Integer.parseInt(map.get("cid")[0]))).and(notice.bid.eq(Integer.parseInt(map.get("bid")[0])))).execute();
		
		return result;
	}

}
