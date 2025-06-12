package com.ssafy.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.ssafy.dto.board.Board;
import com.ssafy.dto.user.User;

@Repository
@Mapper
public interface BoardRepository {
	int createBoard(Board board);
	void createBoardSummary(int id);
	int deleteBoard(int id, int userId);
	void deleteBoardSummary(int id);
	int updateBoard(Board board, int userId);
	void updateBoardSummary();
	Board detailBoard(int id);
	List<Board> boardList(int page);
}
