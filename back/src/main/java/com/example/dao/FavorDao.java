package com.example.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.entity.Favor;

@Repository
@Mapper
public interface FavorDao {
	void saveFavor(Favor favor);

//	List<Integer> getFavorFileIdByUserId(Integer userId, Integer start, Integer showCount);
	List<Integer> getFavorFileIdByUserId(Integer userId);

	void deleteFavorById(Integer favorId);

	void deleteFavor(Integer userId, Integer fileId);

	Favor getFavorByUserIdAndFileId(Integer userId, Integer fileId);
}
