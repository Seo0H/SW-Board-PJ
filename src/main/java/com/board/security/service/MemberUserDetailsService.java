package com.board.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.board.dto.MemberVO;
import com.board.mapper.MemberMapper;
import com.board.security.dto.MemberAuthDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Service //스프링빈 생성
@Log4j2
public class MemberUserDetailsService implements UserDetailsService {

  private final MemberMapper mapper;
    
  @Override 
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 

	  log.info("MemberUserDetailsService loadUserByUsername = " + username);
	  
	  Optional<MemberVO> memberAuth = Optional.ofNullable(mapper.memberInfoView(username));
	  if(!memberAuth.isPresent()) {
		  throw new UsernameNotFoundException("아이디/패스워드가 부정확합니다.");
	  }
	  
	  MemberVO member = memberAuth.get();
	  
	  List grantedAuthorities = new ArrayList<>();
	  SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole());
      grantedAuthorities.add(grantedAuthority);
      
      User user = new User(username, member.getPassword(), grantedAuthorities);
      
      log.info("role = " + member.getRole());

	  return user;

  }

}
	

