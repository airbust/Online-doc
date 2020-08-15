package com.example.controller;

import com.example.dao.UserDao;
import com.example.entity.Group;
import com.example.entity.Result;
import com.example.entity.User;
import com.example.service.GroupService;
import com.example.service.UserService;
import com.example.utils.DateUtil;
import com.example.utils.JwtTokenUtil;
import com.example.utils.TransferUtil;
import com.example.vo.GroupListVO;
import com.example.vo.GroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.xml.TransformerUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/team")
public class GroupController {

    @Autowired(required = false)
    GroupService groupService;

    @Autowired
    UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/setup")
    public Result setupTeam(@RequestBody String teamName) {
        User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Group group = new Group();
        group.setAdminId(user.getId());
        group.setGroupName(teamName);
        group.setCreateTime(DateUtil.getCurrentTime());
        group.setGroupMateIds("");
        int rows = groupService.insertGroup(group);
        if(rows >= 1) {
            return Result.create(200, "创建成功", group);
        } else {
            return Result.create(202, "创建失败", null);
        }
    }

    @GetMapping("/myTeamInfo")
    public Result getMyTeamInfo(){
        User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        List<Group> adminGroups = groupService.selectGroupsByAdminId(user.getId());
        List<GroupVO> adminGroupVOs = new ArrayList<>();
        for(Group group : adminGroups) {
            GroupVO groupVO = new GroupVO();
            try {
                TransferUtil.fatherToChild(group, groupVO);
            } catch (Exception e) {
                e.printStackTrace();
            }
            groupVO.setEditable(true);
            adminGroupVOs.add(groupVO);
        }

        List<GroupVO> mateGroupVOs = new ArrayList<>();
        List<Group> mateGroups = groupService.selectGroupsByGroupMate(user.getId());
        for(Group group : mateGroups) {
            GroupVO groupVO = new GroupVO();
            try {
                TransferUtil.fatherToChild(group, groupVO);
            } catch (Exception e) {
                e.printStackTrace();
            }
            groupVO.setEditable(false);
            mateGroupVOs.add(groupVO);
        }
        GroupListVO groupListVO = new GroupListVO();
        groupListVO.setAdminGroups(adminGroupVOs);
        groupListVO.setMateGroups(mateGroupVOs);
        return Result.create(200, "", groupListVO);
    }

    @DeleteMapping("/dismiss")
    public Result dismissTeam(@RequestParam("teamId") Integer teamId){
        groupService.deleteGroup(teamId);
        return Result.create(200, "",null);
    }

    @PostMapping("/{teamId}/{userId}")
    public Result addTeamMember(@PathVariable("teamId") Integer teamId, @PathVariable("userId") Integer userId) {
        Group group = groupService.selectGroupById(teamId);
        group.setGroupMateIds((group.getGroupMateIds().equals("")? String.valueOf(userId): group.getGroupMateIds() + "," + userId));
        return Result.create(200, "加入成功", null);
    }

    @PostMapping("/join/{teamId}")
    public Result joinTeam(@PathVariable("teamId") Integer teamId) {
        User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        return addTeamMember(teamId, user.getId());
    }

    @DeleteMapping("/{teamId}/{userId}")
    public Result removeTeamMember(@PathVariable("teamId") Integer teamId, @PathVariable("userId") Integer userId) {
        Group group = groupService.selectGroupById(teamId);
        List<String> teammateIds = new ArrayList<String>(Arrays.asList(group.getGroupMateIds().split(",")));
        teammateIds.remove(userId.toString());
        StringBuilder idStr = new StringBuilder();
        if(teammateIds.size() != 0) {
            for(int i = 0; i < teammateIds.size(); ++i) {
                if(i == 0) {
                    idStr.append(teammateIds.get(i));
                } else {
                    idStr.append(",").append(teammateIds.get(i));
                }
            }
        }
        group.setGroupMateIds(idStr.toString());
        groupService.updateGroup(group);
        return Result.create(200, "移除成功");
    }

    @DeleteMapping("/quit/{teamId}")
    public Result quitTeam(@PathVariable("teamId") Integer teamId) {
        User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        return removeTeamMember(teamId, user.getId());
    }
}
