package com.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.entity.Discuss;

@Repository
@Mapper
public interface DiscussDao {

	void saveDiscuss(Discuss discuss);

	void deleteDiscussById(Integer discussId);

	Discuss getDiscussById(Integer discussId);

	List<Discuss> getDiscussByFileId(Integer fileId);

	List<Discuss> getUserFileDiscussByUserId(Integer userId);

	List<Discuss> getTeamFileDiscussByUserId(Integer userId);

	void readDiscussById(Integer discussId);
}
