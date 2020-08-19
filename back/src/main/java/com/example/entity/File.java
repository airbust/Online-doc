package com.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class File {
	// 一个问题是，这样记录的话 数据库初始需要为空，之后想一下怎么处理
//	static public Integer fileCnt = 10;

    private Integer fileId;
    private String fileName;
    private String fileInfo;
    private String fileBody;
    private Date modifyTime;
    private Integer modifyCnt;
    private Integer userId;
    private Integer groupId;
    private Integer isEdit;
    private Integer isDelete;

	public File(Integer fileId, String fileName, String fileBody, Date modifyTime, Integer userId, Integer groupId) {
		this.fileId = fileId;
		this.fileName = fileName;
		this.fileBody = fileBody;
		this.modifyTime = modifyTime;
		this.userId = userId;
		this.groupId = groupId;
//		this.fileId = ++fileCnt;
		this.modifyCnt = 0;
		this.isEdit = 0;
		this.isDelete = 0;
	}

}
