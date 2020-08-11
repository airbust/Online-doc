package com.example.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Role {

    private Integer id;//角色id
    private String name;//角色名 user group other
    private Integer auth_read;
    private Integer auth_write;
    private Integer auth_discuss;
    private Integer auth_share;

    public Role() {
    }
    public Role(String name) {
        this.name = name;
    }}
