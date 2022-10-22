package com.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReplyVO {

	private int replyseqno;
	private int seqno;
	private String replywriter;
	private String replycontent;
	private String replyregdate;
	private String userid;

}
