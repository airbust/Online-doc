package com.example.dao;

import com.example.entity.File;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface FileDao {

    File getFileById(Integer fileId);

    void saveFile(File file);

    void modifyFile(File file);

    List<File> getAllFileById(Integer fileId);

    List<File> getFileByUserId(Integer userId);

    List<File> getFileByGroupId(Integer groupId);

    List<File> getDeletedFileByUserId(Integer userId);

    List<File> getFileByTeamName(String teamName);

    List<File> getDeletedFileByGroupId(Integer groupId);

    void deleteFile(Integer fileId);
    void foreverDeleted(Integer fileId);
    void recoverFile(Integer fileId);

    void setEditStatus(Integer isEdit, Integer fileId);


    List<File> getGroupFileByUserId(Integer userId);
    List<File> getGroupFileByAdminId(Integer userId);

    List<File> getRecentFileByUserId(Integer userId);

    List<File> getHistoryFile(Integer fileId);

    File getHistoryFileById(Integer fileId, Integer modifyCnt);

    Integer getLastFileId();
}
