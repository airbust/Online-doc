package com.example.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Group {

    private Integer groupId;
    private Integer adminId;
    private String groupName;
    private String groupInfo;

}
