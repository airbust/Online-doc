package com.example.controller;

import com.example.entity.Result;
import com.example.service.FileService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileService fileService;


    @GetMapping("/{fileId}")
    public Result findBlogById(@PathVariable Integer fileId) {

        return Result.create(200, "查询成功", fileService.findFileById(fileId));

    }
}
