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

    /**
     * 基础文档操作
     * @param docId
     * @return
     */
    @GetMapping("/{docId}")
    public Result findFileById(@PathVariable Integer docId) {
    	Integer fileId = docId;
    	try {
    		DocResult docResult = fileService.findFileById(fileId);
    		return Result.create(200, "查询成功", docResult);
    	}catch(Exception e) {
    		return Result.create(200, "查询失败," + e.getMessage());
    	}
    }
    @PostMapping("/saveFile")
    public Result newFile(String docTitle, String docBody) {
    	try {
    		fileService.newFile(docTitle, docBody ,null);
    		return Result.create(200, "上传成功");
    	}
    	catch(Exception e) {
    		return Result.create(200, "上传失败," + e.getMessage());
    	}
    }
    @PostMapping("/saveTeamFile")
    public Result newTeamFile(String docTitle, String docBody ,String teamName) {
        try {
            fileService.newFile(docTitle, docBody,teamName);
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
    @DeleteMapping("/delete/{fileId}")
    public Result deleteFile(@PathVariable Integer fileId) {
        try {
            fileService.deleteFile(fileId);
            return Result.create(200, "删除成功");
        } catch (RuntimeException e) {
            return Result.create(200, "删除失败" + e.getMessage());
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
    @PostMapping("/{fileId}/updateAuth")
    public Result updateAuth(@PathVariable Integer fileId, String role, String auth){
        try {
            fileService.updateAuth(fileId,role,auth);
            return Result.create(200, "修改成功");
        } catch (RuntimeException e) {
            return Result.create(200, "修改失败" + e.getMessage());
        }
    }

    /**
     * 回收站相关
     * @return
     */
    @GetMapping("/getDelete")
    public Result getDeleted() {
        try {
            return Result.create(200, "获取成功",fileService.getDeletedFile());
        } catch (RuntimeException e) {
            return Result.create(200, "获取失败，" + e.getMessage());
        }
    }
    @PostMapping("/recoverDelete/{fileId}")
    public Result recoverDelete(@PathVariable Integer fileId) {
        try {
            fileService.recoverFile(fileId);
            return Result.create(200, "操作成功");
        }
        catch(Exception e) {
            return Result.create(200, "操作失败," + e.getMessage());
        }
    }
    @PostMapping("/foreverDelete/{fileId}")
    public Result foreverDelete(@PathVariable Integer fileId) {
        try {
            fileService.foreverDeleted(fileId);
            return Result.create(200, "操作成功");
        }
        catch(Exception e) {
            return Result.create(200, "操作失败," + e.getMessage());
        }
    }


    /**
     * 获取各类文档列表
     * @return
     */
    @GetMapping("/getRecent")
    public Result getRecent() {
        try {
            return Result.create(200,"获取成功",fileService.getRecentFile());
        } catch (RuntimeException e) {
            return Result.create(200,"获取失败，" + e.getMessage());
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
    @GetMapping("/getHistoryFile/{fileId}")
    public Result getTeamFile(@PathVariable Integer fileId){
        try {
            List<File> fileList = fileService.getHistoryFile(fileId);
            return Result.create(200, "获取获取文档成功",fileList);
        } catch (RuntimeException e) {
            return Result.create(200, "获取获取文档失败" + e.getMessage());
        }
    }
    @GetMapping("/getHistoryFile/{fileId}/{modifyCnt}")
    public Result getTeamFile(@PathVariable Integer fileId,@PathVariable Integer modifyCnt){
        try {
            return Result.create(200, "获取获取文档成功",fileService.getHistoryFileById(fileId,modifyCnt));
        } catch (RuntimeException e) {
            return Result.create(200, "获取获取文档失败" + e.getMessage());
        }
    }
    @GetMapping("/getTeamFile/{teamName}")
    public Result getTeamFile(@PathVariable String teamName){
        try {
            List<File> fileList = fileService.getTeamFile(teamName);
//            System.out.println("fileList = " + fileList);
            return Result.create(200, "获取团队文档成功",fileList);
        } catch (RuntimeException e) {
            return Result.create(200, "获取团队文档失败" + e.getMessage());
        }
    }
    @GetMapping("/getRelativeFile")
    public Result getRelativeFile(){
        try {
            List<File> fileList = fileService.getRelativeFile();
            return Result.create(200,"获取用户桌面文档成功",fileList);
        } catch (RuntimeException e) {
            return Result.create(200,"获取桌面文档失败",e.getMessage());
        }
    }



    /**
     * 编辑锁相关
     * @param docId
     * @return
     */
    @PostMapping("/{docId}/isEditable")
    public Result isEditable(@PathVariable Integer docId) {
        try {
            System.out.println("结束编辑 docId = " + docId);
            fileService.changeEditable(false,docId);
            return Result.create(200, "结束编辑-lock");
        }
        catch(Exception e) {
            return Result.create(200, "查询失败," + e.getMessage());
        }
    }

    @PostMapping("/{docId}/isEditing")
    public Result isEditing(@PathVariable Integer docId) {
        try {
            System.out.println("开始编辑 docId = " + docId);
            fileService.changeEditable(true,docId);
            return Result.create(200, "开始编辑-unlock");
        }
        catch(Exception e) {
            return Result.create(200, "查询失败," + e.getMessage());
        }
    }


}
