package com.ssafy.repository;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.ssafy.dto.ApartDto;
import com.ssafy.dto.DealStatsDto;

@Repository
@Mapper
public interface ApartRepository {
	List<ApartDto> selectAll();
	List<DealStatsDto> findMonthlyDealsById(@Param("id") String id);

}
