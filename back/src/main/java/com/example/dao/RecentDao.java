package com.example.dao;

import com.example.entity.Recent;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface RecentDao {

    void saveRecent(Recent recent);

    Recent findRecent(Integer id, Integer fileId);
}
