package com.example.service;

import com.example.config.JwtConfig;
import com.example.dao.FileDao;
import com.example.dao.GroupDao;
import com.example.dao.RoleDao;
import com.example.dao.UserDao;
import com.example.entity.File;
import com.example.entity.Group;
import com.example.entity.GroupResult;
import com.example.entity.User;
import com.example.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private FileDao fileDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    public void createGroup(String groupName) {
        User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Group group = groupDao.getGroupByName(groupName);
        if(group != null)
            throw new RuntimeException("团队名已存在");
        groupDao.saveGroup(new Group(user.getId(),groupName,"还没有介绍"));
    }
    public void attendGroup(String groupName) {
        User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Group group = groupDao.getGroupByName(groupName);
        if(group == null)
            throw new RuntimeException("团队名不存在");
        List<Group> tmp = this.getGroups();
        for(Group i : tmp){
            if(i.getGroupId().equals((group.getGroupId()))) throw new RuntimeException("已在该团队中");
        }
        groupDao.saveGroupMem(group.getGroupId(),user.getId());
    }

    public List<Group> getGroups() {
        User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        List<Group> groupList = new ArrayList<>();

        List<Integer> groupIdList = groupDao.getGroupByUserIdAsMem(user.getId());
        for(Integer i : groupIdList)
            groupList.add(groupDao.getGroupById(i));
        List<Integer> groupIdList2 = groupDao.getGroupByUserIdAsAdmin(user.getId());
        for(Integer i : groupIdList2)
            groupList.add(groupDao.getGroupById(i));
//        Group tmp = groupDao.getGroupByAdminId(user.getId());
//        System.out.println("tmp = " + tmp);
//        if(tmp!=null) groupList.add(tmp);
        System.out.println("groupList = " + groupList);
        return groupList;
    }

    public void saveGroupMem(String userName, String teamName) {
        User user = userDao.getUserByName(userName);
        List<Integer> groupIdList = groupDao.getGroupByUserIdAsMem(user.getId());
        Group group = groupDao.getGroupByName(teamName);
        for(Integer i: groupIdList)
            if(i.equals(group.getGroupId()))
                throw new RuntimeException("该用户已在团队中");
        if(group.getAdminId().equals(user.getId()))
            throw new RuntimeException("该用户已在团队中");
        groupDao.saveGroupMem(group.getGroupId(),user.getId());
    }

    public GroupResult getGroupMem(String teamName) {
        Group group = groupDao.getGroupByName(teamName);
        System.out.println("group = " + group);
        if(group==null) throw new RuntimeException("团队不存在");
        GroupResult groupResult = new GroupResult();
        List<User> adminList = new ArrayList<>();
        adminList.add(userDao.getUserById(group.getAdminId()));
        System.out.println("adminList = " + adminList);
        List<User> memberList = userDao.getMemberById(group.getGroupId());
        System.out.println("memberList = " + memberList);
        groupResult.setGroupId(group.getGroupId());
        groupResult.setAdmin(adminList);
        groupResult.setMember(memberList);
        System.out.println("groupResult = " + groupResult);
        return groupResult;
    }

    public void removeMem(String teamName, String userName) {
        User user = userDao.getUserByName(userName);
        Group group = groupDao.getGroupByName(teamName);
        groupDao.removeMem(group.getGroupId(),user.getId());
    }

    public void deleteGroup(String teamName) {
        Integer groupId = groupDao.getGroupByName(teamName).getGroupId();
        groupDao.deleteGroup(groupId);
        groupDao.deleteUserGroup(groupId);
    }
}
