package com.example.dao;

import com.example.entity.Group;
import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface GroupDao {

    Group getGroupById(Integer groupId);
    List<User> getMemberById(Integer groupId);

}
