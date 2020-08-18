package com.example.controller;

import com.example.dao.FileDao;
import com.example.entity.Result;
import com.example.service.FileService;
import com.example.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/team")
public class GroupController {
    @Autowired
    private FileService fileService;
    @Autowired
    private GroupService groupService;

    @PostMapping("/create/{groupName}")
    public Result createGroup(@PathVariable String groupName){
        try {
            groupService.createGroup(groupName);
            return Result.create(200, "创建成功");
        }
        catch(Exception e) {
            return Result.create(200, "创建失败," + e.getMessage());
        }
    }
    @PostMapping("/attend/{groupName}")
    public Result attendGroup(@PathVariable String groupName){
        try {
            groupService.attendGroup(groupName);
            return Result.create(200, "申请发送成功");
        }
        catch(Exception e) {
            return Result.create(200, "申请发送失败," + e.getMessage());
        }
    }
    @GetMapping("/getGroups")
    public Result getGroups(){
        try {
            return Result.create(200, "获取成功",groupService.getGroups());
        }
        catch(Exception e) {
            return Result.create(200, "获取失败," + e.getMessage());
        }
    }
    @PostMapping("/addMem")
    public Result saveGroupMem(String userName,String teamName){
        try {
            groupService.saveGroupMem(userName,teamName);
            return Result.create(200, "邀请发送成功");
        }
        catch(Exception e) {
            return Result.create(200, "邀请发送失败," + e.getMessage());
        }
    }
    @GetMapping("/getGroupMem/{teamName}")
    public Result getGroupMem(@PathVariable String teamName){
        try {
            return Result.create(200, "添加成功",groupService.getGroupMem(teamName));
        }
        catch(Exception e) {
            return Result.create(200, "添加失败," + e.getMessage());
        }
    }
    @PostMapping("/removeMem")
    public Result removeMem(String teamName,String userName){
        try {
            System.out.println("Remove: teamName = "+teamName+"userName = "+userName);
            groupService.removeMem(teamName,userName);
            return Result.create(200, "移除成员成功");
        }
        catch(Exception e) {
            return Result.create(200, "移除成员失败," + e.getMessage());
        }
    }
    @PostMapping("/deleteGroup")
    public Result deleteGroup(String teamName){
        try {
            System.out.println("Remove: teamName = "+teamName);
            groupService.deleteGroup(teamName);
            return Result.create(200, "解散团队成功");
        }
        catch(Exception e) {
            return Result.create(200, "解散团队失败," + e.getMessage());
        }
    }
}
