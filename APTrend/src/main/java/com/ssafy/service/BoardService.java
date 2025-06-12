package com.ssafy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.dto.board.Board;
import com.ssafy.dto.user.User;
import com.ssafy.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardRepository repo;
	
	public int createBoard(Board board) {
		int res = repo.createBoard(board);
		repo.createBoardSummary(board.getId());
		return res;
	}
	
	public int deleteBoard(int id, int userId) {
		repo.deleteBoardSummary(id);
		return repo.deleteBoard(id, userId);
	}
	
	public int updateBoard(Board board, int userId) {
		return repo.updateBoard(board, userId);
	}
	
	public Board detailBoard(int id) {
		return repo.detailBoard(id);
	}
	
	public List<Board> boardList(int page) {
		return repo.boardList(page);
	}
}
