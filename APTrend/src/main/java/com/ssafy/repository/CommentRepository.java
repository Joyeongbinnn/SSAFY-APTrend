package com.ssafy.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.ssafy.dto.board.Comment;

@Repository
@Mapper
public interface CommentRepository {
	int createComment(Comment comment);
	int deleteComment(int id, int boardId);
	int updateComment(Comment comment, int userId);
	void updateBoardSummary();
	Comment detailComment(int id);
	List<Comment> commentList(int boardId);
}
