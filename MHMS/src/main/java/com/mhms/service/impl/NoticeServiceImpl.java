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

import com.mhms.dto.AnswerDto;
import com.mhms.dto.NoticeDto;
import com.mhms.dto.QAnswerDto;
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

		query.from(notice);
		query.where(notice.sid.eq(Integer.parseInt(map.get("sid")[0])).and(notice.cid.eq(Integer.parseInt(map.get("cid")[0])).and(notice.bid.eq(Integer.parseInt(map.get("bid")[0])))));
		Notice result = query.singleResult(notice);
		
		/* L1 : 작성자가 로그인사용자일 때 수정권한을 부여한다.
		 * L2 : 현재 로그인 사용자가 시스탬 관리자일 경우 수정권한을 부여한다.
		 * L3 : 현재 로그인 사용자가 해당 글의 매니저일 경우 수정권한을 부여한다.
		 * */
		/* L1 : 작성자가 로그인사용자일 때 수정권한을 부여한다.
		 * L2 : 현재 로그인 사용자가 시스탬 관리자일 경우 수정권한을 부여한다.
		 * L3 : 현재 로그인 사용자가 해당 글의 매니저일 경우 수정권한을 부여한다.
		 * */
		if(result.getWriter().equals(user.getUsername())
		|| CommUtil.isRole(user, "ROLE_ADMIN")
		|| (CommUtil.isRole(user, "ROLE_MANAGER") && CommUtil.getBuildUnion(user, result.getBid()))) {
			result.setIsupdate(1);
		} else {
			result.setIsupdate(0);
		}
		
		return result;
	}
	
	@Override
	public List<NoticeDto> BBSList(UserContext user) {
		// TODO Auto-generated method stub
		JPAQuery query = new JPAQuery(entityManager);
		QNotice notice = QNotice.notice;
		QUserRole userRole = QUserRole.userRole;
		
		query.from(notice);
		if(!CommUtil.isRole(user, "ROLE_ADMIN")) {
			query.join(userRole).on(notice.bid.eq(userRole.building.bid));
			query.where(userRole.building.bid.in(user.getBid()).and(notice.notice_lv.eq(0)).and(userRole.account.uid.eq(user.getUid())));
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
			|| CommUtil.isRole(user, "ROLE_ADMIN")
			|| (CommUtil.isRole(user, "ROLE_MANAGER") && CommUtil.getBuildUnion(user, dto.get(i).getBid()))) {
				dto.get(i).setIsupdate(1);
			} else {
				dto.get(i).setIsupdate(0);
			}
			
			dto.get(i).setNo(i + 1);
		}
		
		return dto;
	}

	@Override
	public List<NoticeDto> noticeList(UserContext user) {
		// TODO Auto-generated method stub
		JPAQuery query = new JPAQuery(entityManager);
		QNotice notice = QNotice.notice;
		QUserRole userRole = QUserRole.userRole;
		
		query.from(notice);
		if(!CommUtil.isRole(user, "ROLE_ADMIN")) {
			query.join(userRole).on(notice.bid.eq(userRole.building.bid));
			query.where(userRole.building.bid.in(user.getBid()).and(notice.notice_lv.eq(1)).and(userRole.account.uid.eq(user.getUid())));
		} else {
			query.where(notice.notice_lv.eq(1));
		}
		
		List<NoticeDto> dto = query.list(new QNoticeDto(notice.sid, notice.cid, notice.bid, notice.title, notice.notice_lv, notice.viewcnt, notice.content, notice.filename, notice.writer, notice.writedate));
		
		for(int i = 0; i <  dto.size(); i++) {
			
			/* L1 : 작성자가 로그인사용자일 때 수정권한을 부여한다.
			 * L2 : 현재 로그인 사용자가 시스탬 관리자일 경우 수정권한을 부여한다.
			 * L3 : 현재 로그인 사용자가 해당 글의 매니저일 경우 수정권한을 부여한다.
			 * */
			if(dto.get(i).getWriter().equals(user.getUsername())
			|| CommUtil.isRole(user, "ROLE_ADMIN")
			|| (CommUtil.isRole(user, "ROLE_MANAGER") && CommUtil.getBuildUnion(user, dto.get(i).getBid()))) {
				dto.get(i).setIsupdate(1);
			} else {
				dto.get(i).setIsupdate(0);
			}
			
			dto.get(i).setNo(i + 1);
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
		
		updateClause.set(notice.filename, filename)
		            .where(notice.sid.eq(sid).and(notice.cid.eq(0)).and(notice.bid.eq(Integer.parseInt(map.get("bid")[0]))))
		            .execute();
		
	}
	
	@Transactional
	@Override
	public long updateNotice(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		
		QNotice notice = QNotice.notice;
		JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, notice);
		long result = 
		updateClause.set(notice.title, map.get("title")[0])
		            .set(notice.content, map.get("content")[0])
		            .set(notice.link, map.get("link")[0])
		            .where(notice.sid.eq(Integer.parseInt(map.get("sid")[0]))
		            		.and(notice.cid.eq(Integer.parseInt(map.get("cid")[0]))
		            				.and(notice.bid.eq(Integer.parseInt(map.get("bid")[0])))))
		            .execute();
		
		return result;
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

	@Override
	public List<AnswerDto> selectAnswer(Map<String, String[]> map, UserContext user) {
		// TODO Auto-generated method stub
		JPAQuery query = new JPAQuery(this.entityManager);
	    QNotice notice = QNotice.notice;
	    query.from(notice);
	    query.where(notice.sid.eq(Integer.parseInt((map.get("sid"))[0])).and(notice.cid.ne(0).and(notice.bid.eq(Integer.parseInt((map.get("bid"))[0])))));
	    query.orderBy(notice.cid.asc());
	    List<AnswerDto> dto = query.list(new QAnswerDto(notice.sid, notice.cid, notice.bid, notice.content, notice.writer, notice.writedate));
	    for (int i = 0; i < dto.size(); i++) {
	      if ((dto.get(i)).getWriter().equals(user.getUsername()) || 
	        CommUtil.isRole(user, "ROLE_ADMIN") || (
	        CommUtil.isRole(user, "ROLE_MANAGER") && CommUtil.getBuildUnion(user, (dto.get(i)).getBid()))) {
	        (dto.get(i)).setIsupdate(1);
	      } else {
	        (dto.get(i)).setIsupdate(0);
	      } 
	      String date = CommUtil.convertDateFormat((dto.get(i)).getWritedate());
	      (dto.get(i)).setWritedate(date);
	    } 
	    return dto;
	}

	@Override
	public int insertAnswer(Map<String, String[]> map, UserContext user) throws SQLException {
		// TODO Auto-generated method stub
		Connection conn = this.dataSource.getConnection();
	    String insertSQL = "";
	    PreparedStatement pstmt = null;
	    JPAQuery query = new JPAQuery(this.entityManager);
	    QNotice notice = QNotice.notice;
	    query.from(notice);
	    query.where(notice.sid.eq(Integer.parseInt((map.get("sid"))[0])));
	    query.orderBy(notice.cid.desc());
	    Notice dto = query.singleResult(notice);
	    int maxCid = 0;
	    if (dto == null) {
	      maxCid = 1;
	    } else {
	      maxCid = (dto.getCid() > 0) ? (dto.getCid() + 1) : 1;
	    } 
	    
	    insertSQL = "INSERT INTO tb_notice (bid, sid, content, write_date, writer, cid, notice, viewcnt) VALUES(?, ?, ?, strftime(\"%Y%m%d\",'now','localtime'), ?, ?, 2, 0)";
	    pstmt = conn.prepareStatement(insertSQL);
	    pstmt.setInt(1, Integer.parseInt(((String[])map.get("bid"))[0]));
	    pstmt.setInt(2, Integer.parseInt(((String[])map.get("sid"))[0]));
	    pstmt.setString(3, ((String[])map.get("content"))[0]);
	    pstmt.setString(4, user.getUsername());
	    pstmt.setInt(5, maxCid);
	    int result = pstmt.executeUpdate();
	    pstmt.getConnection().close();
	    return result;
	}

	@Transactional
	@Override
	public long deleteAnswerBBS(Map<String, String[]> map) throws SQLException {
		// TODO Auto-generated method stub
		QNotice notice = QNotice.notice;
	    JPADeleteClause deleteClause = new JPADeleteClause(this.entityManager, notice);
	    long result = deleteClause.where(notice.sid.eq(Integer.valueOf(Integer.parseInt((map.get("sid"))[0]))).and(notice.cid.eq(Integer.valueOf(Integer.parseInt((map.get("cid"))[0])))).and(notice.bid.eq(Integer.valueOf(Integer.parseInt((map.get("bid"))[0]))))).execute();
	    return result;
	}

}
