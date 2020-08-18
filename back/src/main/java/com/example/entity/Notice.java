package com.example.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class Notice {
    private Integer noticeId;
    private String userName;
    private String groupAdmin;
    private String groupName;
    private Integer op; // 1 邀请 2 申请 3 批准 4 同意 5 踢除
    private String info;
    private Date time;
    private Integer isRead;

    public Notice() { }
    public Notice(String userName,String groupAdmin, String groupName, Integer op, String info, Date time, Integer isRead) {
        this.userName = userName;
        this.groupAdmin = groupAdmin;
        this.groupName = groupName;
        this.op = op;
        this.info = info;
        this.time = time;
        this.isRead = isRead;
    }
}
