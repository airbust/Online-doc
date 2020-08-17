package com.example.dao;

import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserDao {

    User getUserById(Integer id);
    User getUserByName(String name);
    void saveUser(User newUser);
    void updateUser(User user);
    List<User> getMemberById(Integer groupId);

    void updateAvatar(Integer userId, String avatar);
}
