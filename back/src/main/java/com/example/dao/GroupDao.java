package com.example.dao;

import com.example.entity.Group;
import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface GroupDao {

    void saveGroup(Group group);

    Group getGroupByName(String teamName);

    Group getGroupById(Integer groupId);

    //获取用户所在团队，分别作为admin与member两种
    List<Integer> getGroupByUserIdAsMem(Integer userId);
    List<Integer> getGroupByUserIdAsAdmin(Integer userId);

    Group getGroupByAdminId(Integer adminId);

    //保存团队成员
    void saveGroupMem(Integer groupId, Integer userId);

    void removeMem(Integer groupId, Integer userId);

    //根据团队名解散团队
    void deleteGroup(Integer groupId);
    void deleteUserGroup(Integer groupId);
}
