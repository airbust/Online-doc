package com.example.dao;

import com.example.entity.File;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface FileDao {

    File getFileById(Integer fileId);
}
