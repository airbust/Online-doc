package com.example.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupListVO {
    List<GroupVO> adminGroups;
    List<GroupVO> mateGroups;
}
