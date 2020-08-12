package com.example.controller;

import com.example.entity.Result;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @GetMapping("/hello")
    public Result Hello(){
        return Result.create(200, "Test success!");
    }

    @PostMapping("/register")
    public Result register(String name, String password, String mail, String code){
        try {
            userService.register(name, password, mail, code);
            return Result.create(200, "注册成功");
        } catch (RuntimeException e) {
            return Result.create(200, "注册失败，" + e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result login(String name, String password){
        try {
            System.out.println("name = " + name);
            Map map = userService.login(name,password);
            return Result.create(200, "登录成功", map);
        } catch (RuntimeException e) {
            return Result.create(200, e.getMessage());
        }
    }

    /**
     * 用户退出登录
     * 删除redis中的token
     */
    @GetMapping("/logout")
    public Result logout() {
        userService.logout();
        return Result.create(200, "退出成功");
    }

    @PostMapping("/sendmail")
    public Result sendmail(String mail){
        String redisMailCode = redisTemplate.opsForValue().get("MAIL_"+mail);
        if(redisMailCode!=null){
            return Result.create(200,"请稍后再发送");
        }else{
            userService.sendMail(mail);
            return Result.create(200,"发送成功");
        }
    }

}
