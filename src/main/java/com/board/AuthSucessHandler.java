package com.board;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.board.dto.MemberVO;
import com.board.service.MemberService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor //롬복
@Log4j2
@Component //스프링 빈으로 생성
public class AuthSucessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final MemberService service; //의존성 주입

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    	
    	//로그인 성공 후 해줘야 할 것들을 여기 메소드 내에 작성
        service.logindateUpdate(authentication.getName());
        
        //authentication.getName() -> ID값을 가져온다.
        MemberVO member = service.login(authentication.getName());
        
        HttpSession session = request.getSession();              
		session.setAttribute("userid",member.getUserid());
		session.setAttribute("username",member.getUsername());
		session.setAttribute("role", member.getRole());
		log.info("Session 설정 완료 !!!");

        setDefaultTargetUrl("/userManage/welcome");
        
        super.onAuthenticationSuccess(request, response, authentication);
    }
}