package com.example.dao;

import com.example.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface RoleDao {

    Role getAuthByFileId(Integer fileId);
    void saveAuthByFileId(Role role);

    void updateOtherAuth(Integer fileId, Integer read, Integer discuss);
    void updateGroupAuth(Integer fileId, Integer discuss, Integer write);
}
