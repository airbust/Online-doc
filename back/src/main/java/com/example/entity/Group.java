package com.example.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class Group {

    private Integer groupId;
    private Integer adminId;
    private String groupName;
    private String groupInfo;
    private String groupMateIds;
    private Date createTime;

}
