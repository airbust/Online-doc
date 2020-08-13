package com.example.controller;

import com.example.entity.Result;
import com.example.dao.FileDao;
import com.example.entity.*;
import com.example.service.FileService;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@CrossOrigin
@RestController
@RequestMapping("/doc")
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private FileDao fileDao;

    @GetMapping("/{docId}")
    public Result findFileById(@PathVariable Integer docId) {
    	Integer fileId = docId;
    	try {
    		File file = fileService.findFileById(fileId);
    		return Result.create(200, "查询成功", file);
    	}catch(Exception e) {
    		return Result.create(200, "查询失败," + e.getMessage());
    	}
        

    }
    
    @PostMapping("/send")
    public Result newFile(String docTitle, String docBody) {
    	try {
    		fileService.newFile(docTitle, docBody);
    		return Result.create(200, "上传成功");
    	}
    	catch(Exception e) {
    		return Result.create(200, "上传失败," + e.getMessage());
    	}
    }
    
    
    @PostMapping("/edit")
    public Result newFile(Integer docId, String docTitle, String docBody) {
    	try {
    		fileService.editFile(docId, docTitle, docBody);
    		return Result.create(200, "编辑成功");
    	}
    	catch(Exception e) {
    		return Result.create(200, "编辑失败," + e.getMessage());
    	}
    }
    
    @DeleteMapping("/delete")
    public Result deleteFile(Integer docId) {
    	try {
    		fileService.deleteFile(docId);
    		return Result.create(200, "删除成功");
    	}
    	catch(Exception e) {
    		return Result.create(200, "删除失败," + e.getMessage());
    	}
    }	
    
    @GetMapping("/getRecycle")
    public Result getRecycleFile() {
    	return Result.create(200, "查询成功", fileService.getDeletedFile());
    }
    
    @GetMapping("/getCreation")
    public Result getCreationFile() {
    	return Result.create(200, "查询成功", fileService.getCreationFile());
    }
    
    @GetMapping("/isEditable") 
    public Result isEditable(Integer docId) {
    	try {
        	boolean res = fileService.isEditable(docId);
        	return Result.create(200, "查询成功", res);
    	}
    	catch(Exception e) {
    		return Result.create(200, "查询失败," + e.getMessage());
    	}
    }
    
    @GetMapping("/isEditing") 
    public Result isEditing(Integer docId) {
    	try {
        	boolean res = fileService.isEditable(docId);
        	return Result.create(200, "查询成功", !res);
    	}
    	catch(Exception e) {
    		return Result.create(200, "查询失败," + e.getMessage());
    	}
    }
    
    @GetMapping("/recoverDeletedDocumentById")
    public Result recoverDeletedDocumentById(Integer docId) {
    	try {
        	fileService.recoverFile(docId);
        	return Result.create(200, "操作成功");
    	}
    	catch(Exception e) {
    		return Result.create(200, "操作失败," + e.getMessage());
    	}
    }
    
    @GetMapping("/getMyDocument")
    public Result getMyDocument(Integer page, Integer showCount) {
    	try {
        	List<File> file = fileService.getMyDocument(page, showCount);
        	return Result.create(200, "查询成功", file);
    	}
    	catch(Exception e) {
    		return Result.create(200, "查询失败," + e.getMessage());
    	}
    }
}
