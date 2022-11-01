package com.board.controller;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.board.dto.AddressVO;
import com.board.dto.MemberVO;
import com.board.service.MemberService;
import com.board.util.Page;

@Controller
public class MemberController {

	Logger log = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	MemberService service;
	
	@Autowired //비밀번호 암호화 이존성 주입
	private PasswordEncoder pwdEncoder;
	
	//메인 페이지 등록
	@GetMapping("/")
	public String main() {return "/member/login";}
	
	//사용자 등록 화면 보기
	@RequestMapping(value="/member/signup",method=RequestMethod.GET)
	public void getMemberRegistry() throws Exception { }
	
	//사용자 등록 처리
	@RequestMapping(value="/member/signup",method=RequestMethod.POST)
	public String postMemberRegistry(MemberVO member,
		@RequestParam("fileUpload") MultipartFile multipartFile
		) {
		
		String path = "c:\\Repository\\profile\\";
		File targetFile;
		
		if(!multipartFile.isEmpty()) {
				
				String org_filename = multipartFile.getOriginalFilename();	
				String org_fileExtension = org_filename.substring(org_filename.lastIndexOf("."));	
				String stored_filename =  UUID.randomUUID().toString().replaceAll("-", "") + org_fileExtension;	
								
				try {
					targetFile = new File(path + stored_filename);
					
					multipartFile.transferTo(targetFile);
					
					member.setOrg_filename(org_filename);
					member.setStored_filename(stored_filename);
					member.setFilesize(multipartFile.getSize());
																				
				} catch (Exception e ) { e.printStackTrace(); }
				
				String inputPassword = member.getPassword();
				String pwd = pwdEncoder.encode(inputPassword); 
				member.setPassword(pwd);			
				
		}	

		service.memberInfoRegistry(member);
		return "redirect:/";
	}
	
	//사용자 등록 시 아이디 중복 확인
	@ResponseBody
	@RequestMapping(value="/member/idCheck",method=RequestMethod.POST)
	public int idCheck(@RequestParam("userid") String userid) throws Exception{
		
		int result = service.idCheck(userid);
		
		return result;
	}
	
	//로그인 화면 보기
	@RequestMapping(value="/member/login",method=RequestMethod.GET)
	public void getLogIn(Model model,@RequestParam(name="message", required=false) String message) { 
		
		model.addAttribute("message", message);
		
	}

	//로그인 화면 보기
	@RequestMapping(value="/member/login",method=RequestMethod.POST)
	public void postLogIn(Model model,@RequestParam(name="message", required=false) String message) { 
		
		model.addAttribute("message", message);
		
	}

	//welcome 페이지 정보 가져 오기
	@RequestMapping(value="/userManage/welcome",method=RequestMethod.GET)
	public void getWelcomeView(HttpSession session,Model model) {
		
		String userid = (String)session.getAttribute("userid");
		String username = (String)session.getAttribute("username");
		
		MemberVO member = service.welcomeView(userid);
		
		model.addAttribute("userid", userid);
		model.addAttribute("username", username);
		model.addAttribute("regdate", member.getRegdate());
		model.addAttribute("lastlogindate", member.getLastlogindate());
		model.addAttribute("lastlogoutdate", member.getLastlogoutdate());
		
	}
	
	//로그아웃 하기 전 로그아웃 시간 등록
	@RequestMapping(value="/userManage/beforelogout",method=RequestMethod.GET)
	public void getLogout(HttpSession session, Model model) {	
		
		//세션 읽어오는 부분
		String userid= (String)session.getAttribute("userid");
		String username=(String)session.getAttribute("username");
		
		//로그아웃 날짜 등록
		service.logoutUpdate(userid);
		
		model.addAttribute("userid", userid);
		model.addAttribute("username", username);
		
		
		
	}
	
	//로그아웃
	@RequestMapping(value="/userManage/logout",method=RequestMethod.POST)
		public void postLogout(HttpSession session,Model model) {
		
//		String userid = (String)session.getAttribute("userid");
//		String username = (String)session.getAttribute("username");
//
//		//로그 아웃 날짜 등록
//		service.logoutUpdate(userid);
//		
//		model.addAttribute("userid", userid);
//		model.addAttribute("username", username);
//		
//		session.invalidate(); //모든 세션 종료--> 로그아웃...
		
	}
	
	//사용자 정보 보기
	@RequestMapping(value="/userManage/memberInfo")
		public void gerMemberInfoView(Model model,HttpSession session) {
		
		String userid = (String)session.getAttribute("userid");
		MemberVO member = service.memberInfoView(userid);
		MemberVO member_date = service.welcomeView(userid);
		
		model.addAttribute("member", member);
		model.addAttribute("member_date", member_date);
		
	}
	
	// <------------------- 3.과제 ------------------------> 
	//jspBoard 및 servletBoard를 참고하여 아래의 기능을 구현 
	//사용자 기본 정보 변경
	//패스워드 변경
	//사용자 아이디 찾기
	//패스워드 찾기(패스워드 임시 생성)
	//회원탈퇴 - 등록한 게시글, 댓글, 좋아요/싫어요, 첨부파일(프로파일 이미지 포함) 삭제 , @Transaction 기능 활용
	
	//사용자 기본 정보 변경 화면 보기
	@RequestMapping(value="/userManage/modifyMemberInfo", method=RequestMethod.GET)
	public void getModifyMemberInfo(Model model, HttpSession session) throws Exception {
			
			String userid = (String)session.getAttribute("userid");
			MemberVO member = service.memberInfoView(userid);
			
			model.addAttribute("member", member);
	
		}
		
	@RequestMapping(value="/member/modifyMemberInfo", method=RequestMethod.POST)
	public String postModifyMemberInfo(MemberVO member,
			@RequestParam(value= "fileUpload") MultipartFile multipartFile) {
		
		String path = "c:\\Repository\\profile\\";
		File targetFile;
		
		//새 파일 , 기존 파일 구분하는 기능 추가.
		//만약 새 파일이 들어온게 확인되면 기존 파일 삭제.
		if(!multipartFile.isEmpty()) {
			MemberVO vo = new MemberVO();
			vo = service.memberInfoView(member.getUserid());
			File file = new File(path + vo.getStored_filename());
			file.delete();
			
			String org_filename = multipartFile.getOriginalFilename();	
			String org_fileExtension = org_filename.substring(org_filename.lastIndexOf("."));	
			String stored_filename =  UUID.randomUUID().toString().replaceAll("-", "") + org_fileExtension;	
							
			try {
				targetFile = new File(path + stored_filename);
				
				multipartFile.transferTo(targetFile);
				
				member.setOrg_filename(org_filename);
				member.setStored_filename(stored_filename);
				member.setFilesize(multipartFile.getSize());
																			
			} catch (Exception e ) { e.printStackTrace(); }
		}	
		
		service.memberInfoUpdate(member);
		return "redirect:/";
	}
		
			
	
		
	//우편번호 검색
	@RequestMapping(value="/member/addrSearch",method=RequestMethod.GET)
	public void getSearchAddr(@RequestParam("addrSearch") String addrSearch,
			@RequestParam("page") int pageNum,Model model) throws Exception {
		
		int postNum = 5;
		int startPoint = (pageNum -1)*postNum + 1; //테이블에서 읽어 올 행의 위치
		int endPoint = pageNum*postNum;
		int listCount = 5;
		
		Page page = new Page();
		
		int totalCount = service.addrTotalCount(addrSearch);
		
		Map<String,Object> data = new HashMap<>();
		data.put("startPoint", startPoint);
		data.put("endPoint", endPoint);
		data.put("addrSearch", addrSearch);
		
		List<AddressVO> list = new ArrayList<>();
		list = service.addrSearch(data);

		model.addAttribute("list", list);
		model.addAttribute("pageListView", page.getPageAddress(pageNum, postNum, listCount, totalCount, addrSearch));
		
	}
	
}
