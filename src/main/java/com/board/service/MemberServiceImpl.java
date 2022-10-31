package com.board.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.board.dto.AddressVO;
import com.board.dto.MemberVO;
import com.board.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberMapper mapper; 
	
	//아이디 확인
	@Override
	public int idCheck(String userid) {
		return mapper.idCheck(userid);
	}

	//로그인 정보 확인
	@Override
	public MemberVO login(String userid) {
		return mapper.login(userid);
	}

	//마지막 로그인 날짜 등록/수정
	@Override
	public void logindateUpdate(String userid) {
		mapper.logindateUpdate(userid);
	}

	//welcome 페이지 정보 가져 오기 
	public MemberVO welcomeView(String userid) {
		return mapper.welcomeView(userid);
	}

	//로그 아웃 날짜 등록
	@Override
	public void logoutUpdate(String userid) {
		mapper.logoutUpdate(userid);
	}

	//사용자 등록
	@Override
	public void memberInfoRegistry(MemberVO member) {
		mapper.memberInfoRegistry(member);
	}

	//사용자 정보 보기
	@Override
	public MemberVO memberInfoView(String userid) {
		return mapper.memberInfoView(userid);
	}

	//우편번호 전체 갯수 가져 오기
	@Override
	public int addrTotalCount(String addrSearch) {
		return mapper.addrTotalCount(addrSearch);
	}

	//우편번호 검색
	@Override
	public List<AddressVO> addrSearch(Map<String,Object> data) {
		return mapper.addrSearch(data);
	}
	
	//사용자 정보 수정
	public void memberInfoUpdate(MemberVO member) {
		mapper.memberInfoUpdate(member);
	}

}
