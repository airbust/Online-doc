package com.example.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Role {

    private Integer id;
    private Integer groupWrite;
    private Integer groupDiscuss;
    private Integer groupShare;
    private Integer otherRead;
    private Integer otherWrite;
    private Integer otherDiscuss;
    private Integer otherShare;

    public Role() {
    }
}
