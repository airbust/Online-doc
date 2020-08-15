package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Discuss;
import com.example.entity.Result;
import com.example.service.DiscussService;

@RestController
@RequestMapping("/discuss")
public class DiscussController {
	
	@Autowired
	DiscussService discussService;
	
	@PostMapping("/send")
	Result newDiscuss(Integer docId, String discussBody) {
		try {
			discussService.newDiscuss(docId, discussBody);
			return Result.create(200, "评论成功");
		}
		catch (Exception e) {
			return Result.create(200, "评论失败" + e.getMessage());
		}
	}
	
	@DeleteMapping("/delete")
	Result deleteDiscuss(Integer discussId) {
		try {
			discussService.deleteDiscussById(discussId);
			return Result.create(200, "删除成功");
		}
		catch (Exception e) {
			return Result.create(200, "删除失败" + e.getMessage());
		}
	}
	
	@GetMapping("/getByDocId")
	Result getDiscussByDocId(Integer docId) {
		return Result.create(200, "查询成功", discussService.getDiscussByFileId(docId));
	}
	
	@GetMapping("/getById")
	Result getDiscussById(Integer discussId) {
		try {
			Discuss dis = discussService.getDiscussById(discussId);
			return Result.create(200, "查询成功", dis);
		}
		catch (Exception e) {
			return Result.create(200, "查询失败," + e.getMessage());
		}
	}
}
