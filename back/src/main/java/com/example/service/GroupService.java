package com.example.service;

import com.example.dao.GroupDao;
import com.example.dao.UserDao;
import com.example.entity.Group;
import com.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

public class GroupService {
    @Autowired
    GroupDao groupDao;

//    @Autowired
//    UserService userService;

    @Autowired
    UserDao userDao;

    public int insertGroup(Group group) {
        return groupDao.insertSelective(group);
    }

    public List<Group> selectGroupsByAdminId(Integer userId) {
        Example example = new Example(Group.class);
        example.createCriteria().andEqualTo("adminId", userId);
        return groupDao.selectByExample(example);
    }

    public List<Group> selectGroupsByGroupMate(Integer userId) {
        List<Group> groups = new ArrayList<>();
        for(Group group : selectAll()) {
            String[] groupMateIds = group.getGroupMateIds().split(",");
            for(String groupMateId : groupMateIds) {
                if (groupMateId.equals(userId.toString())) {
                    groups.add(group);
                    break;
                }
            }
        }
        return groups;
    }

    public List<Group> selectAll() {
        return groupDao.selectAll();
    }

    public Group selectGroupByName(String groupName) {
        Example example = new Example(Group.class);
        example.createCriteria().andEqualTo("groupName", groupName);
        return groupDao.selectOneByExample(example);
    }

    public void updateGroup (Group group) {
        groupDao.updateByPrimaryKeySelective(group);
    }

    public Group selectGroupById(Integer groupId) {
        return groupDao.selectByPrimaryKey(groupId);
    }

    public List<User> selectGroupMateIds(Integer groupId) {
        List<User> groupMates = new ArrayList<>();
        String groupMateIds = groupDao.selectByPrimaryKey(groupId).getGroupMateIds();
        if(groupMateIds.equals("")) {
            return null;
        } else {
            String[] groupMateIdsArray = groupMateIds.split(",");
            for (String groupMateId : groupMateIdsArray) {
                groupMates.add(userDao.getUserById(Integer.valueOf(groupMateId)));
            }
        }
        return groupMates;
    }

    public void deleteGroup(Integer groupId) {
        groupDao.deleteByPrimaryKey(groupId);
    }
}
