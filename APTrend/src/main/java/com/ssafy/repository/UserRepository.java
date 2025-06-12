package com.ssafy.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.ssafy.dto.user.User;
import com.ssafy.dto.user.UserAuth;
import com.ssafy.dto.user.UserJoinUserAuth;

@Repository
@Mapper
public interface UserRepository {
	int createUser(User user);
	int createUserAuth(UserAuth user);
	int deleteUser(int id);
	int updateUser(User user);
	UserAuth findByLocalLoginId(String loginId);
	int findById(int id);
	User findByIdReturnUser(int id);
	User findByLoginId(String loginId);
	User findByEmail(String email);
	int findByEmailReturnInt(String email);
	UserJoinUserAuth findLocalDataByLoginId(String loginId);
	int findUserIdByEmail(String email);
	User findUserByEmail(String email);
}
