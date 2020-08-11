package com.example.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class File {

    private Integer fileId;
    private String fileName;
    private String fileInfo;
    private String fileBody;
    private Date modifyTime;
    private Integer modifyCnt;
    private Integer userId;
    private Integer groupId;

}
