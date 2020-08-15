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
    		DocResult docResult = fileService.findFileById(fileId);
            System.out.println("docResult = " + docResult);
    		return Result.create(200, "查询成功", docResult);
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

//    @DeleteMapping("/delete")
//    public Result deleteFile(Integer docId) {
//    	try {
//    		fileService.deleteFile(docId);
//    		return Result.create(200, "删除成功");
//    	}
//    	catch(Exception e) {
//    		return Result.create(200, "删除失败," + e.getMessage());
//    	}
//    }

    @GetMapping("/getRecycle")
    public Result getRecycleFile() {
    	return Result.create(200, "查询成功", fileService.getDeletedFile());
    }

//    @GetMapping("/getCreation")
//    public Result getCreationFile() {
//    	return Result.create(200, "查询成功", fileService.getCreationFile());
//    }

    @GetMapping("/{docId}/isEditable")
    public Result isEditable(@PathVariable String docId) {
    	try {
            System.out.println("结束编辑 docId = " + Integer.valueOf(docId));
        	fileService.changeEditable(false,Integer.valueOf(docId));
        	return Result.create(200, "结束编辑-lock");
    	}
    	catch(Exception e) {
    		return Result.create(200, "查询失败," + e.getMessage());
    	}
    }

    @GetMapping("/{docId}/isEditing")
    public Result isEditing(@PathVariable String docId) {
    	try {
            System.out.println("开始编辑 docId = " + Integer.valueOf(docId));
        	fileService.changeEditable(true,Integer.valueOf(docId));
        	return Result.create(200, "开始编辑-unlock");
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

//    @GetMapping("/getMyDocument")
//    public Result getMyDocument(Integer page, Integer showCount) {
//    	try {
//        	List<File> file = fileService.getMyDocument(page, showCount);
//        	return Result.create(200, "查询成功", file);
//    	}
//    	catch(Exception e) {
//    		return Result.create(200, "查询失败," + e.getMessage());
//    	}
//    }
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
