package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Result;
import com.example.service.FavorService;

@RestController
@RequestMapping("/doc")
public class FavorController {
	@Autowired
	FavorService favorService;

	@PostMapping("/collect/{docId}")
	Result collectDocument(@PathVariable Integer docId) {
		try {
			favorService.favorFile(docId);
			return Result.create(200, "收藏成功");
		}
		catch(Exception e) {
			return Result.create(200, "收藏失败," + e.getMessage());
		}
	}

//	@GetMapping("/MyCollectingDoc")
//	Result collectDocument(Integer page, Integer showCount) {
//		return Result.create(200, "查询成功", favorService.getCollectionFile(page, showCount));
//	}
	@GetMapping("/getCollect")
	Result collectDocument() {
		return Result.create(200, "查询成功", favorService.getCollectionFile());
	}

	@DeleteMapping("/removeCollectedDoc/{docId}")
	Result removeCollectedDocument(@PathVariable Integer docId) {
		favorService.removeFavoredFile(docId);
		return Result.create(200, "删除成功");
	}
}
