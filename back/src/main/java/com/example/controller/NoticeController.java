package com.example.controller;

import com.example.entity.Result;
import com.example.service.FileService;
import com.example.service.GroupService;
import com.example.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.reactive.ReactiveResourceSynchronization;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;
    @Autowired
    private GroupService groupService;

    @GetMapping
    public Result getUserNotice(){
        try {
            return Result.create(200, "获取成功",noticeService.getUserNotice());
        }
        catch(Exception e) {
            return Result.create(200, "获取失败," + e.getMessage());
        }
    }
    @PostMapping("/read/{noticeId}")
    public Result readNotice(@PathVariable Integer noticeId){
        try {
            System.out.println("Read noticeId = " + noticeId);
            noticeService.readNotice(noticeId);
            return Result.create(200, "已读成功");
        }
        catch(Exception e) {
            return Result.create(200, "已读失败," + e.getMessage());
        }
    }
    @PostMapping("/permit/{noticeId}")
    public Result permit(@PathVariable Integer noticeId){
        try {
            noticeService.permit(noticeId);
            return Result.create(200, "批准成功");
        }
        catch(Exception e) {
            return Result.create(200, "批准失败," + e.getMessage());
        }
    }
}
