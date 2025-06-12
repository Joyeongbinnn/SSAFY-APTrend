package com.ssafy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.dto.board.Board;
import com.ssafy.dto.board.Comment;
import com.ssafy.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository repo;
	public int createBoard(Comment comment) {
		int res = repo.createComment(comment);
		return res;
	}
	
	public int deleteComment(int id, int boardId) {
		return repo.deleteComment(id, boardId);
	}
	
	public int updateComment(Comment comment, int userId) {
		return repo.updateComment(comment, userId);
	}
	
	public Comment detailComment(int id) {
		return repo.detailComment(id);
	}
	
	public List<Comment> commentList(int boardId) {
		return repo.commentList(boardId);
	}
}
