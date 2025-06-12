package com.ssafy.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.ssafy.dto.HotAptDto;

@Repository
@Mapper
public interface HotAptRepository {
	List<HotAptDto> selectHotApartments();
}
