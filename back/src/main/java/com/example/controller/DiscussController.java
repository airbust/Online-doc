package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Discuss;
import com.example.entity.Result;
import com.example.service.DiscussService;

@RestController
@RequestMapping("/discuss")
public class DiscussController {

	@Autowired
	DiscussService discussService;

	@PostMapping("/send/{fileId}")
	Result newDiscuss(@PathVariable Integer fileId, String discussBody) {
		try {
//			System.out.println("fileId = " + fileId);
//			System.out.println("discussBody = " + discussBody);
			discussService.newDiscuss(fileId, discussBody);
			return Result.create(200, "评论成功");
		}
		catch (Exception e) {
			return Result.create(200, "评论失败" + e.getMessage());
		}
	}

	@DeleteMapping("/delete/{discussId}")
	Result deleteDiscuss(@PathVariable Integer discussId) {
		try {
			discussService.deleteDiscussById(discussId);
			return Result.create(200, "删除成功");
		}
		catch (Exception e) {
			return Result.create(200, "删除失败" + e.getMessage());
		}
	}

	@PostMapping("/read/{discussId}")
	Result readDiscuss(@PathVariable Integer discussId) {
		try {
			discussService.readDiscussById(discussId);
			return Result.create(200, "已读成功");
		}
		catch (Exception e) {
			return Result.create(200, "已读失败" + e.getMessage());
		}
	}

	@GetMapping("/getByDocId/{fileId}")
	Result getDiscussByDocId(@PathVariable String fileId) {
		return Result.create(200, "查询成功", discussService.getDiscussByFileId(Integer.valueOf(fileId)));
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

	@GetMapping("/getAllDiscuss")
	Result getAllDiscuss(){
		try {
			return Result.create(200, "查询成功", discussService.getAllDiscuss());
		}
		catch (Exception e) {
			return Result.create(200, "查询失败," + e.getMessage());
		}
	}
}
