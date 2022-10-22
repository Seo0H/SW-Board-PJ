package com.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LikeVO {

	private int seqno;
	private String userid;
	private String mylikecheck;
	private String mydislikecheck;
	private String likedate;
	private String dislikedate;
	
}
