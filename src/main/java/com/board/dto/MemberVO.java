package com.board.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberVO {

	private String userid;
	private String username;
	private String password;
	private String telno;
	private String email;
	private String zipcode;
	private String address;
	private LocalDateTime regdate;
	private String lastlogindate;
	private String lastlogoutdate;
	private LocalDateTime lastpwdate;
	private int pwcheck;
	private String role;
	private String org_filename;
	private String stored_filename;
	private long filesize;

}	