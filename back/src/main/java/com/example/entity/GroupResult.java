package com.example.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class GroupResult {
    private Integer groupId;
    private List<User> admin;
    private List<User> member;

}
