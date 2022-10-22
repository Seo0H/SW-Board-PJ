package com.board.controller;

import java.io.File;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.board.dto.BoardVO;
import com.board.dto.FileVO;
import com.board.dto.LikeVO;
import com.board.dto.ReplyVO;
import com.board.service.BoardService;
import com.board.util.Page;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class BoardController {

	@Autowired
	BoardService service; //의존성 주입
	
	//게시물 목록 보기
	@GetMapping("/board/list")
	public void GetList(Model model, @RequestParam(name="page") int pageNum, 
			@RequestParam(name="searchType", defaultValue="title", required=false) String searchType, 
			@RequestParam(name="keyword", defaultValue="", required=false) String keyword ) throws Exception{
		
		int listCount = 5;
		int postNum = 5; //한 페이지에 보여질 게시물 목록 갯수
		int startPoint = (pageNum -1)*postNum + 1; 
		int endPoint = postNum*pageNum;
		
		Map<String,Object> data = new HashMap<>();
		data.put("startPoint", startPoint);
		data.put("endPoint", endPoint);
		data.put("searchType", searchType);
		data.put("keyword", keyword);
	
		Page page = new Page();
		int totalCount = service.totalCount(data);
				
		model.addAttribute("list", service.list(data));
		model.addAttribute("page", pageNum);
		model.addAttribute("searchType", searchType);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageListView", page.getPageList(pageNum, postNum, listCount, totalCount, searchType, keyword));
		
		
	}

	//게시물 내용 보기
	@GetMapping("/board/view")
	public void GetView(Model model,HttpSession session,
			@RequestParam("seqno") int seqno,@RequestParam(name="page") int pageNum,
			@RequestParam(name="searchType", defaultValue="title", required=false) String searchType, 
			@RequestParam(name="keyword", defaultValue="", required=false) String keyword) throws Exception{
		
		BoardVO board = service.view(seqno);
		
		//조회수 증가
		String userid = (String)session.getAttribute("userid");
		if(!userid.equals(board.getUserid())) 
			service.hitnoUpgrade(seqno);
		
		Map<String,Object> data = new HashMap<>();
		data.put("seqno", seqno);
		data.put("userid", userid);
		data.put("searchType", searchType);
		data.put("keyword", keyword);
		
		LikeVO likeCheckView = service.likeCheckView(data);
		
		//초기에 좋아요/싫어요 등록이 안되어져 있을 경우 "N"으로 초기화 
		
		if(likeCheckView == null) {
			model.addAttribute("myLikeCheck", "N");
			model.addAttribute("myDislikeCheck", "N");
		} else if(likeCheckView != null) {
			model.addAttribute("myLikeCheck", likeCheckView.getMylikecheck());
			model.addAttribute("myDislikeCheck", likeCheckView.getMydislikecheck());
		}
		model.addAttribute("view", board);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("searchType", searchType);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pre_seqno", service.pre_seqno(data));
		model.addAttribute("next_seqno",service.next_seqno(data));
		model.addAttribute("fileListView", service.fileListView(seqno));
		model.addAttribute("likeCheckView", likeCheckView);
	}
	
	//게시물 등록 화면 보기
	@GetMapping("/board/write")
	public void GetWrite() { }
	
	//첨부 파일 있는 게시물 등록
	@PostMapping("/board/writeWithFile")
	public String PostWriteWithFile(BoardVO board, HttpServletRequest request) throws Exception{
		
		log.info("<-------------- 첨부 파일 있음 ------------------->");
		int seqno = service.getSeqnoWithLastNumber();
		board.setSeqno(seqno);
	
		service.write(board);
		
		return "redirect:/board/list?page=1";
	}

	//첨부 파일 없는 게시물 등록
	@PostMapping("/board/write")
	public String PostWrite(BoardVO board) throws Exception{
		
		log.info("<-------------- 첨부 파일 없음 ------------------->");
		int seqno = service.getSeqnoWithNextval();
		board.setSeqno(seqno);
		
		service.write(board);
		
		return "redirect:/board/list?page=1";
	}
	
	//파일 업로드
	@ResponseBody
	@PostMapping("/board/fileUpload")
	public void postFileUpload(@RequestParam("SendToFileList") List<MultipartFile> multipartFile, 
			@RequestParam("kind") String kind,@RequestParam(name="seqno", defaultValue="0", required=false) int seqno,
			HttpSession session) throws Exception{
		
		log.info("파일 전송...");
		String path = "c:\\Repository\\file\\"; 
		String userid = (String)session.getAttribute("userid"); 
		if(kind.equals("I")) 
			seqno = service.getSeqnoWithNextval();
					
		File targetFile = null; 
		
		Map<String,Object> fileInfo = null;
		
		for(MultipartFile mpr:multipartFile) {
			
			String org_filename = mpr.getOriginalFilename();	
			String org_fileExtension = org_filename.substring(org_filename.lastIndexOf("."));	
			String stored_filename = UUID.randomUUID().toString().replaceAll("-", "") + org_fileExtension;	
			long filesize = mpr.getSize() ;
			
			log.info("org_filename={}", org_filename);
			log.info("stored_filename={}", stored_filename);
			
			targetFile = new File(path + stored_filename);
			mpr.transferTo(targetFile);
			
			fileInfo = new HashMap<>();
			fileInfo.put("org_filename",org_filename);
			fileInfo.put("stored_filename", stored_filename);
			fileInfo.put("filesize", filesize);
			fileInfo.put("seqno", seqno);
			fileInfo.put("userid", userid);
	
			service.fileInfoRegistry(fileInfo);

		}
	}
	
	//파일 다운로드
	@GetMapping("/board/fileDownload")
	public void fileDownload(@RequestParam(name="fileseqno" , required=false) int fileseqno, HttpServletResponse rs) throws Exception {
		
		String path = "c:\\Repository\\file\\";
		
		FileVO fileInfo = service.fileInfo(fileseqno);
		String org_filename = fileInfo.getOrg_filename();
		String stored_filename = fileInfo.getStored_filename();
		
		byte fileByte[] = FileUtils.readFileToByteArray(new File(path+stored_filename));
		
		rs.setContentType("application/octet-stream");
		rs.setContentLength(fileByte.length);
		rs.setHeader("Content-Disposition",  "attachment; fileName=\""+URLEncoder.encode(org_filename, "UTF-8")+"\";");
		rs.getOutputStream().write(fileByte);
		rs.getOutputStream().flush();
		rs.getOutputStream().close();
		
	}
		
	//게시물 수정 화면 보기
	@GetMapping("/board/modify")
	public void GetModify(Model model,@RequestParam("seqno") int seqno) 
			throws Exception {
		
		model.addAttribute("view", service.view(seqno));
		model.addAttribute("fileListView", service.fileListView(seqno));
	}
	
	@PostMapping("/board/modify")
	public String PostModify(BoardVO board) throws Exception{
		// <------------------- 과제 ------------------------> 
		service.modify(board);
		
		// 선택한 첨부 파일 삭제
		// 추가된 첨부 파일 업로드		
		
		return null;
	}
	
	//게시물 삭제
	@GetMapping("/board/delete")
	public String GetDelete(@RequestParam("seqno") int seqno) throws Exception{

		// <------------------- 과제 ------------------------> 
		// 게시물 삭제 시 댓글, 첨부파일, 좋아요/싫어요 정보 삭제
		//@Transaction 사용
		
		return null;
	}

	//좋아요/싫어요 관리
	@ResponseBody
	@PostMapping(value = "/board/likeCheck")
	public Map<String, Object> postLikeCheck(@RequestBody Map<String, Object> likeCheckData) throws Exception {
		
		int seqno = (int)likeCheckData.get("seqno");
		String userid = (String)likeCheckData.get("userid");
		int checkCnt = (int)likeCheckData.get("checkCnt");

		//현재 날짜, 시간 구해서 좋아요/싫어요 한 날짜/시간 입력 및 수정
		String likeDate = "";
		String dislikeDate = "";
		if(likeCheckData.get("mylikecheck").equals("Y")) 
			likeDate = LocalDateTime.now().toString();
		else if(likeCheckData.get("mydislikecheck").equals("Y")) 
			dislikeDate = LocalDateTime.now().toString();

		likeCheckData.put("likedate", likeDate);
		likeCheckData.put("dislikedate", dislikeDate);

		//TBL_LIKE 테이블 입력/수정
		Map<String,Object> likeCheckViewData = new HashMap<>();
		likeCheckViewData.put("seqno",seqno);
		likeCheckViewData.put("userid",userid);
		
		LikeVO likeCheckView = service.likeCheckView(likeCheckViewData);
		if(likeCheckView == null) service.likeCheckRegistry(likeCheckData);
			else service.likeCheckUpdate(likeCheckData);

		//TBL_BOARD 내의 likecnt,dislikecnt 입력/수정 
		BoardVO board = service.view(seqno);
		
		int likeCnt = board.getLikecnt();
		int dislikeCnt = board.getDislikecnt();
			
		switch(checkCnt){
	    	case 1 : likeCnt --; break;
	    	case 2 : likeCnt ++; dislikeCnt --; break;
	    	case 3 : likeCnt ++; break;
	    	case 4 : dislikeCnt --; break;
	    	case 5 : likeCnt --; dislikeCnt ++; break;
	    	case 6 : dislikeCnt ++; break;
		}
		
		Map<String,Object> boardLikeUpdateData = new HashMap<>();
		boardLikeUpdateData.put("seqno", seqno);
		boardLikeUpdateData.put("likecnt", likeCnt);
		boardLikeUpdateData.put("dislikecnt", dislikeCnt);
		service.boardLikeUpdate(boardLikeUpdateData);
		
		//AJAX에 전달할 map JSON 데이터 만들기
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("seqno", seqno);
		map.put("likeCnt", likeCnt);
		map.put("dislikeCnt", dislikeCnt);
		
		return map;
	}
	
	//댓글 처리
	@ResponseBody
	@PostMapping("/board/reply")
	public List<ReplyVO> postReply(ReplyVO reply,@RequestParam("option") String option)throws Exception{
		
		switch(option) {
		
		case "I" : service.replyRegistry(reply); //댓글 등록
				   break;
		case "U" : service.replyUpdate(reply); //댓글 수정
				   break;
		case "D" : service.replyDelete(reply); //댓글 삭제
				   break;
		}

		return service.replyView(reply);
	}
}
