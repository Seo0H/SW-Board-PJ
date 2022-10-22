package com.board;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component //스프링 빈으로 생성
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, 
    		AuthenticationException exception) throws IOException, ServletException {
    	//로그인 메세지 처리할 것들 전달
        String msg = "";

        // exception 관련 메세지 처리
        if (exception instanceof UsernameNotFoundException) {
            msg = "ID_NOT_FOUND";
        } else if (exception instanceof BadCredentialsException) {
            msg = "AUTHORITY_NOT_PERMITTED";
        }
        
        //로그인 실패 처리 후 경로 결정.
        setDefaultFailureUrl("/member/login?message=" + msg);
        
        //부모에게 결과 보고 후 메소드 끝.
        super.onAuthenticationFailure(request, response, exception);

    }
}