package com.example.controller;

import com.example.entity.Result;
import com.example.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/doc")
public class FileController {
    @Autowired
    private FileService fileService;


    @GetMapping("/{fileId}")
    public Result findBlogById(@PathVariable Integer fileId) {

        return Result.create(200, "查询成功", fileService.findFileById(fileId));

    }
    @DeleteMapping("/delete/{fileId}")
    public Result deleteFile(@PathVariable Integer fileId) {
        try {
            fileService.deleteFile(fileId);
            return Result.create(200, "删除成功");
        } catch (RuntimeException e) {
            return Result.create(200, "删除失败" + e.getMessage());
        }
    }

    @PostMapping
    public Result saveDoc(String docTitle, String docBody) {
        try {
            System.out.println("title = " + docTitle);
            System.out.println("body = " + docBody);
            fileService.newFile(docTitle,docBody);
            return Result.create(200, "保存成功");
        } catch (RuntimeException e) {
            return Result.create(200, "注册失败，" + e.getMessage());
        }
    }
    @PostMapping("/update")
    public Result updateDoc(Integer docId, String docTitle, String docBody) {
        try {
            System.out.println("title = " + docTitle);
            System.out.println("body = " + docBody);
            fileService.editFile(docId,docTitle,docBody);
            return Result.create(200, "更新成功");
        } catch (RuntimeException e) {
            return Result.create(200, "更新失败，" + e.getMessage());
        }
    }

    @GetMapping("/getCreation")
    public Result getCreation() {
        try {
            return Result.create(200, "获取成功",fileService.getCreationFile());
        } catch (RuntimeException e) {
            return Result.create(200, "获取失败，" + e.getMessage());
        }
    }

    @GetMapping("/deletedDoc")
    public Result getDeletedDocment() {
        try {
            return Result.create(200, "获取成功",fileService.getDeletedFile());
        } catch (RuntimeException e) {
            return Result.create(200, "获取失败，" + e.getMessage());
        }
    }
    @GetMapping("/deletedDoc/{fileId}")
    public Result recoverFile(@PathVariable Integer fileId) {
        try {
            fileService.recoverFile(fileId);
            return Result.create(200, "删除成功");
        } catch (RuntimeException e) {
            return Result.create(200, "删除失败" + e.getMessage());
        }
    }
}
