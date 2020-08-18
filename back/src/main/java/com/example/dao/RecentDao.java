package com.example.dao;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


import com.example.entity.Recent;

@Repository
@Mapper
public interface RecentDao {
	
	public void saveRecent(Recent recent);
	
}
