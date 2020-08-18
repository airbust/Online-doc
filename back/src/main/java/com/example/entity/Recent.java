package com.example.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class Recent {

    private Integer recentId;
    private Integer userId;
    private Integer fileId;
    private Date recentTime;

    public Recent() {}

    public Recent(Integer userId, Integer fileId, Date recentTime) {
        this.userId = userId;
        this.fileId = fileId;
        this.recentTime = recentTime;
    }
}
