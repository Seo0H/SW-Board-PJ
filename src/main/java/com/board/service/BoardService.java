package com.board.service;

import java.util.List;
import java.util.Map;

import com.board.dto.BoardVO;
import com.board.dto.FileVO;
import com.board.dto.LikeVO;
import com.board.dto.ReplyVO;

public interface BoardService {

	//게시물 목록보기
	public List<BoardVO> list(Map<String,Object> data) throws Exception;

	//전체 게시물 갯수 계산
	public int totalCount(Map<String,Object> data) throws Exception;
	
	//게시물 내용 보기
	public BoardVO view(int seqno) throws Exception;
	
	//게시물 번호 구하기 - 시퀀스의 Last Number 사용
	public int getSeqnoWithLastNumber() throws Exception;
	
	//게시물 번호 구하기 - 시퀀스의 nexval 사용
	public int getSeqnoWithNextval() throws Exception;
	
	//게시물 이전 보기
	public int pre_seqno(Map<String,Object> data) throws Exception;
	
	//게시물 다음 보기
	public int next_seqno(Map<String,Object> data) throws Exception;
		
	//파일 업로드 정보 등록
	public void fileInfoRegistry(Map<String,Object> fileInfo) throws Exception;
	
	//다운로드를 위한 파일 정보 보기
	public FileVO fileInfo(int fileseqno) throws Exception;
	
	//게시글 내에서 업로드된 파일 목록 보기
	public List<FileVO> fileListView(FileVO seqno) throws Exception;
	
	//선택 파일 삭제
	public int fileDel(int fileseqno) throws Exception ;
	
	//게시물 등록
	public void write(BoardVO board) throws Exception;
	
	//게시물 수정
	public void modify(BoardVO board) throws Exception;
	
	//게시물 삭제
	public void delete(int seqno) throws Exception;
	
	//게시물에 업로드된 파일 삭제
	public List<FileVO> deleteFileOnBoard(int seqno)throws Exception;
	
	//좋아요/싫어요 확인 가져 오기
	public LikeVO likeCheckView(Map<String,Object> data) throws Exception;
	
	//좋아요/싫어요 갯수 수정하기
	public void boardLikeUpdate(Map<String,Object> data) throws Exception;
	
	//좋아요/싫어요 확인 등록하기
	public void likeCheckRegistry(Map<String,Object> data) throws Exception;
	
	//좋아요/싫어요 확인 수정하기
	public void likeCheckUpdate(Map<String,Object> data) throws Exception;
		
	//댓글 보기
	public List<ReplyVO> replyView(ReplyVO reply) throws Exception;
	
	//댓글 수정
	public void replyUpdate(ReplyVO reply) throws Exception;
	
	//댓글 등록 
	public void replyRegistry(ReplyVO reply) throws Exception;
	
	//댓글 삭제
	public void replyDelete(ReplyVO reply) throws Exception;
	
	//게시물 조회수 증가
	public void hitnoUpgrade(int seqno) throws Exception;
}
