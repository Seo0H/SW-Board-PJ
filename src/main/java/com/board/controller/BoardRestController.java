package com.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.board.dto.BoardVO;
import com.board.mapper.BoardMapper;
import com.board.service.BoardService;


@RestController
public class BoardRestController {

	@Autowired
	BoardService service;
	
	//게시물 목록 보기
	@RequestMapping(value={"/RESTAPI/list/{page}/{searchType}/{keyword}","/RESTAPI/list/{page}"},method=RequestMethod.GET)
	public List<BoardVO> GetList(@PathVariable(value="page") int pageNum, 
			@PathVariable(value="searchType", required=false) String searchType, 
			@PathVariable(value="keyword", required=false) String keyword ) throws Exception{
		
		int postNum = 5; //한 페이지에 보여질 게시물 목록 갯수
		int startPoint = (pageNum -1)*postNum + 1; 
		int endPoint = postNum*pageNum;
		
		if(searchType == null) searchType = "mtitle";
		if(keyword == null) keyword = "";
		
		Map<String,Object> data = new HashMap<>();
		data.put("startPoint", startPoint);
		data.put("endPoint", endPoint);
		data.put("searchType", searchType);
		data.put("keyword", keyword);
	
		return service.list(data);
		
	}

	//게시물 내용 보기
	@RequestMapping(value={"/RESTAPI/view/{seqno}"}, method=RequestMethod.GET)
	public BoardVO GetView(HttpSession session, @PathVariable("seqno") int seqno) throws Exception{
		
		BoardVO board = service.view(seqno);

		return board;
	}
	
	//게시물 등록
	@RequestMapping(value={"/RESTAPI/write/{userid}/{mwriter}/{mtitle}/{mcontent}"}, method=RequestMethod.GET)
	public void PostWrite(@PathVariable("userid") String userid, @PathVariable("mwriter") String mwriter, 
			@PathVariable("mtitle") String mtitle, @PathVariable("mcontent") String mcontent) throws Exception{
		
		int seqno = service.getSeqnoWithNextval();
		BoardVO board = new BoardVO();
		
		board.setSeqno(seqno);
		board.setUserid(userid);
		board.setMwriter(mwriter);
		board.setMtitle(mtitle);
		board.setMcontent(mcontent);
		
		service.write(board);
		 
	}
	
	//게시물 삭제
	@RequestMapping(value={"/RESTAPI/delete/{seqno}"}, method=RequestMethod.GET)
	public void getDelete(@PathVariable("seqno") int seqno) throws Exception {
		
		service.delete(seqno);
		
	}
	
}
