package com.example.service;

import com.example.config.JwtConfig;
import com.example.dao.FileDao;
import com.example.dao.GroupDao;
import com.example.dao.UserDao;
import com.example.entity.File;
import com.example.entity.Group;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class FileService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private FileDao fileDao;
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public Map<String, Object> findFileById(Integer fileId) throws RuntimeException {
        //TODO 前提用户已登录
        String userName = jwtTokenUtil.getUsernameFromRequest(request);

        //签发token
        final UserDetails userDetails = this.loadUserByUsername(Integer.toString(fileId));
        final String token = jwtTokenUtil.generateToken(userDetails);
        //获取用户权限
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            roles.add(authority.getAuthority());
        }
        Map<String, Object> map = new HashMap<>(3);
        map.put("token", jwtConfig.getPrefix() + token);
        map.put("name", userName);
        map.put("roles", roles);

        //设置过期时间 (key value time s)
        redisTemplate.opsForValue().
                set("TOKEN_" + userName, jwtConfig.getPrefix() + token, 60, TimeUnit.SECONDS);
        return map;
    }

    //******************************************************************************

    /**
     * 通过用户名加载用户到 Spring Security
     * @param fileId 文档id
     * @return 用户信息
     */
    public UserDetails loadUserByUsername(String fileId) throws UsernameNotFoundException {
        User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Integer userId = user.getId();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(1);
        //用于添加用户的权限。将用户权限添加到authorities
        File file = fileDao.getFileById(Integer.valueOf(fileId));
        Role role = new Role();
        if(file.getUserId()!=0){ // 个人文档
            if(file.getUserId().equals(userId)) role.setName("USER");
            else role.setName("OTHER");
        }
        else{ // 团队文档
            Group group = groupDao.getGroupById(file.getGroupId());
            if(group.getAdminId().equals(userId)) role.setName("USER");
            else{
                List<User> members = groupDao.getMemberById(file.getGroupId());
                Boolean flag = true;
                for(User member : members){
                    if(member.getId().equals(userId)) {
                        role.setName("GROUP"); flag=false;
                    }
                }
                if(flag) role.setName("OTHER");
            }
        }
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return new org.springframework.security.core.userdetails.User(user.getName(), "***********", authorities);
    }

}
