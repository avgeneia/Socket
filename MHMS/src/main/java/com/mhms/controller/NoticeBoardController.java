package com.mhms.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.io.ByteStreams;
import com.mhms.dto.BuildingDto;
import com.mhms.security.UserContext;
import com.mhms.service.BuildingService;
import com.mhms.service.NoticeService;
import com.mhms.sqlite.entities.Notice;
import com.mhms.util.CommUtil;

@Controller
@ControllerAdvice
public class NoticeBoardController {

	@Autowired
	private BuildingService buildingService;

	@Autowired
	private NoticeService noticeService;

	@Autowired
	ResourceLoader resourceLoader;

	/*
	 * 조회 1건 : bbs
	 */
	@RequestMapping("/selectBBS")
	@ResponseBody
	public Map<String, Object> selectBBS(HttpServletRequest request, @AuthenticationPrincipal UserContext user) {

		Map<String, Object> map = new HashMap<String, Object>();

		try {
			Notice dto = noticeService.selectNotice(request.getParameterMap(), user);
			map.put("CODE", "0");
			map.put("DATA", dto);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("CODE", e.getErrorCode());
			map.put("MSG", e.getMessage());
		}
		
		map.put("answerList", this.noticeService.selectAnswer(request.getParameterMap(), user));
		
		return map;
	}
	
	/*
	 * 조회 1건 : notice
	 */
	@RequestMapping("/selectNotice")
	@ResponseBody
	public Map<String, Object> selectnotice(HttpServletRequest request, @AuthenticationPrincipal UserContext user) {

		Map<String, Object> map = new HashMap<String, Object>();

		try {
			Notice dto = noticeService.selectNotice(request.getParameterMap(), user);
			map.put("CODE", "0");
			map.put("DATA", dto);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("CODE", e.getErrorCode());
			map.put("MSG", e.getMessage());
		}
		
		map.put("answerList", this.noticeService.selectAnswer(request.getParameterMap(), user));
		
		return map;
	}
	
	/*
	 * 조회 목록(MAIN)
	 */
	@RequestMapping("/noticeBoard")
	public String noticeBoard(Model model, @AuthenticationPrincipal UserContext user) {

		Map<String, String> map = new HashMap<String, String>();

		map.put("title", "BBS");

		List<BuildingDto> builList = buildingService.initBuild(user);
		BuildingDto buildDto = new BuildingDto(-1, "선택");
		builList.add(0, buildDto);

		// model.addAttribute("infoVO", map);
		model.addAttribute("bbsList", noticeService.BBSList(user));
		model.addAttribute("initBuildVO", builList);
		model.addAttribute("pageInfo", "noticeBoard");

		return "noticeBoard";
	}
	
	/*
	 * 조회 목록(tab : bbs)
	 */
	@RequestMapping("/BBSList")
	public String bbsList(Model model, @AuthenticationPrincipal UserContext user) {

		List<BuildingDto> builList = buildingService.initBuild(user);
		BuildingDto buildDto = new BuildingDto(-1, "선택");
		builList.add(0, buildDto);

		// model.addAttribute("infoVO", map);
		model.addAttribute("bbsList", noticeService.BBSList(user));
		model.addAttribute("initBuildVO", builList);

		return "noticeBoard :: #table_bbs";
	}
	
	/*
	 * 조회 목록(tab : notice)
	 */
	@RequestMapping("/NoticeList")
	public String noticeList(Model model, @AuthenticationPrincipal UserContext user) {

		List<BuildingDto> builList = buildingService.initBuild(user);
		BuildingDto buildDto = new BuildingDto(-1, "선택");
		builList.add(0, buildDto);

		// model.addAttribute("infoVO", map);
		model.addAttribute("noticeList", noticeService.noticeList(user));
		model.addAttribute("initBuildVO", builList);

		return "noticeBoard :: #table_notice";
	}
	
	/*
	 * 추가
	 */
	@RequestMapping("/insertNotice")
	@ResponseBody
	public Map<String, String> insertNotice(MultipartHttpServletRequest request,
			@AuthenticationPrincipal UserContext user) {

		Map<String, String> map = new HashMap<String, String>();

		MultipartFile file = request.getFile("file");

		int sid = 0;
		boolean isFile = false;
		if (file == null) {
			isFile = false;
		}

		try {

			// 게시글 정상 입력 후 해당글의 파일ID 리턴
			sid = noticeService.insertNotice(request.getParameterMap(), user);

			if (!isFile) {
				String filepath = request.getSession().getServletContext().getRealPath("/") + "upload" + File.separator;
				String filename = CommUtil.getTransFileName(sid, 0, Integer.parseInt(request.getParameter("bid")));

				boolean successFile = CommUtil.fileUpload(file, filepath + filename);

				if (successFile) {
					noticeService.updateFile(request.getParameterMap(), file.getOriginalFilename(), sid);
				}
			}

			map.put("CODE", "0");
			map.put("MSG", "완료되었습니다.");

		} catch (SQLException e) {
			System.out.println("SQLException :: " + e.getErrorCode());

			map.put("CODE", String.valueOf(e.getErrorCode()));
			map.put("MSG", e.getMessage());
		}

		return map;
	}

	/*
	 * 수정
	 */
	@RequestMapping("/updateNotice")
	@ResponseBody
	public Map<String, String> updateNotice(MultipartHttpServletRequest request) {
		
		Map<String, String> map = new HashMap<String, String>();

		MultipartFile file = request.getFile("file");
		
		boolean isFile = false;
		if (file == null) {
			isFile = false;
		}
		
		try {
			long result = noticeService.updateNotice(request.getParameterMap());
			
			if(result == 1) {
				map.put("CODE", "0");
				map.put("MSG", "완료되었습니다.");
			}
			
			int sid = Integer.parseInt(request.getParameter("sid"));
			
			if (!isFile) {
				String filepath = request.getSession().getServletContext().getRealPath("/") + "upload" + File.separator;
				String filename = CommUtil.getTransFileName(sid, 0, Integer.parseInt(request.getParameter("bid")));

				boolean successFile = CommUtil.fileUpload(file, filepath + filename);

				if (successFile) {
					noticeService.updateFile(request.getParameterMap(), file.getOriginalFilename(), sid);
				}
			}
			
		} catch (SQLException e) {
			map.put("CODE", String.valueOf(e.getErrorCode()));
			map.put("MSG", e.getMessage());
		}
		
		return map;
	}
	
	/*
	 * 삭제
	 */
	@RequestMapping("/deleteNotice")
	@ResponseBody
	public Map<String, String> deleteNotice(HttpServletRequest request, @AuthenticationPrincipal UserContext user) {

		Map<String, String> map = new HashMap<String, String>();
		try {
			long result = noticeService.deleteNotice(request.getParameterMap());

			if (result == 1) {
				map.put("CODE", "0");
				map.put("MSG", "완료되었습니다.");
			}
		} catch (SQLException e) {
			map.put("CODE", String.valueOf(e.getErrorCode()));
			map.put("MSG", e.getMessage());
		}
		return map;
	}

	/*
	 * 파일다운로드
	 */
	@RequestMapping(path="/downloadNotice", method=RequestMethod.GET)
	public void downloadNotice(
			  HttpServletRequest request
		    , HttpServletResponse response
		    , @AuthenticationPrincipal UserContext user) throws SQLException, IOException {
		
		String filepath = request.getSession().getServletContext().getRealPath("/") + "upload" + File.separator;
		String filename = CommUtil.getTransFileName(Integer.parseInt(request.getParameter("sid")),
				Integer.parseInt(request.getParameter("cid")), Integer.parseInt(request.getParameter("bid")));

		File file = new File(filepath + filename);
		
		Map<String, String[]> map = new HashMap<String, String[]>();
		
		String[] sid = {request.getParameter("sid")};
		String[] cid = {request.getParameter("cid")};
		String[] bid = {request.getParameter("bid")};
		
		map.put("sid", sid);
		map.put("cid", cid);
		map.put("bid", bid);
		
		Notice noticeDto = noticeService.selectNotice(map, user);
		
		response.setHeader("Content-Disposition","attachment; filename=\"" + noticeDto.getFilename() +"\";");
	    response.setContentType("application/x-download");
	    
	    FileInputStream fileIn = new FileInputStream(file); //파일 읽어오기
	    ByteStreams.copy(fileIn, response.getOutputStream());
	    response.flushBuffer();
		
    }

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseBody
	public Map<String, String> handleFileUploadError(RedirectAttributes ra) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("CODE", "-1");
		map.put("MSG", "파일용량은 10MB를 초과할 수 없습니다.");

		return map;
	}

	@RequestMapping("/selectAnswerBBS")
	public String selectAnswerBBS(Model model, HttpServletRequest request, @AuthenticationPrincipal UserContext user) {
		
		model.addAttribute("answerList", this.noticeService.selectAnswer(request.getParameterMap(), user));
		
		return "fragments/popup/bbsPopup :: #answerBBS";
	}
	  
	@RequestMapping("/selectAnswerNotice")
	public String selectAnswerNotice(Model model, HttpServletRequest request, @AuthenticationPrincipal UserContext user) {
		
		model.addAttribute("answerList", this.noticeService.selectAnswer(request.getParameterMap(), user));
		
		return "fragments/popup/noticePopup :: #answerNotice";
	}
	 
	
	@RequestMapping("/insertAnswer")
	public String insertAnswer(Model model, @AuthenticationPrincipal UserContext user, HttpServletRequest request) throws SQLException {
		
		int result = this.noticeService.insertAnswer(request.getParameterMap(), user);
		
	    Map<String, String> map = new HashMap<>();
	    
	    if (result > 0) {
	    	map.put("CODE", "0");
	    	map.put("MSG", "완료되었습니다.");
	    }
	    
	    model.addAttribute("answerList", this.noticeService.selectAnswer(request.getParameterMap(), user));
	    
	    if (request.getParameter("type").equals("BBS")) {
	    	return "fragments/popup/bbsPopup :: #answerBBS";
	    } else {
	    	return "fragments/popup/noticePopup :: #answerNotice";
	    }
	}
	
	@RequestMapping("/deleteAnswerBBS")
	public String deleteAnswerBBS(Model model, HttpServletRequest request, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<>();
		
		try {
			
			long result = this.noticeService.deleteAnswerBBS(request.getParameterMap());
			
			if (result == 1L) {
				map.put("CODE", "0");
				map.put("MSG", "완료되었습니다.");
			} 
			
	    } catch (SQLException e) {
	    	map.put("CODE", String.valueOf(e.getErrorCode()));
	    	map.put("MSG", e.getMessage());
	    } 
		
		model.addAttribute("answerList", this.noticeService.selectAnswer(request.getParameterMap(), user));
	    model.addAttribute("resultVO", map);
	    
	    return "fragments/popup/bbsPopup :: #answerBBS";
	}
	  
	@RequestMapping("/deleteAnswerNotice")
	public String deleteAnswerNotice(Model model, HttpServletRequest request, @AuthenticationPrincipal UserContext user) {
		
		Map<String, String> map = new HashMap<>();
		
		try {
			
			long result = this.noticeService.deleteAnswerBBS(request.getParameterMap());
			
			if (result == 1L) {
				map.put("CODE", "0");
				map.put("MSG", "완료되었습니다.");
			}
		
	    } catch (SQLException e) {
	    	map.put("CODE", String.valueOf(e.getErrorCode()));
	    	map.put("MSG", e.getMessage());
	    }
		
		model.addAttribute("answerList", this.noticeService.selectAnswer(request.getParameterMap(), user));
		model.addAttribute("resultVO", map);
		
		return "fragments/popup/noticePopup :: #answerNotice";
	}
	
}	
