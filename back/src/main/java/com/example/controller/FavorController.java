package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Result;
import com.example.service.FavorService;

@RestController
@RequestMapping("/doc")
public class FavorController {
	@Autowired
	FavorService favorService;
	
	@PostMapping("/collect")
	Result collectDocument(Integer docId) {
		try {
			favorService.favorFile(docId);
			return Result.create(200, "收藏成功");
		}
		catch(Exception e) {
			return Result.create(200, "收藏失败," + e.getMessage());
		}
	}
	
	@GetMapping("/MyCollectingDoc")
	Result collectDocument(Integer page, Integer showCount) {
		return Result.create(200, "查询成功", favorService.getCollectionFile(page, showCount));
	}
	
	@DeleteMapping("/removeCollectedDoc")
	Result removeCollectedDocument(Integer docId) {
		favorService.removeFavoredFile(docId);
		return Result.create(200, "删除成功");
	}
}
