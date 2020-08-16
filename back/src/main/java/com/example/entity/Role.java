package com.example.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Role {

    private Integer id;
    private Integer fileId;
    private Integer groupWrite;
    private Integer groupDiscuss;
    private Integer otherRead;
    private Integer otherDiscuss;

    public Role() {

    }
    public Role(Integer fileId) {
        this.fileId = fileId;
        this.groupWrite = 1;
        this.groupDiscuss = 1;
        this.otherRead = 1;
        this.otherDiscuss = 0;
    }
}
