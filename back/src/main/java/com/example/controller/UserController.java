package com.example.controller;

import com.example.dao.UserDao;
import com.example.entity.Result;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.utils.JwtTokenUtil;
import com.example.utils.FormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FormatUtil formatUtil;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

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


    @RequestMapping(value = "/uploadAvatar", method = RequestMethod.POST)
    @ResponseBody
    public Result uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
            String path="/home/zero/avatar/";
            String format = formatUtil.getFileFormat(file.getOriginalFilename());
            File file1 = new File(path + user.getName() + format);
            System.out.println();
            System.out.println(path + user.getId() + format);
            System.out.println();
            if(!file1.getParentFile().exists()){
                file1.getParentFile().mkdirs();
            }
            file.transferTo(file1);
            userService.saveAvatar(user.getId(),"http://39.107.228.168/avatar/"+user.getName()+format);
//            userService.saveAvatar(user.getId(),"http://127.0.0.1/avatar/"+user.getName()+format);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return Result.create(200, "上传头像成功",null);
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

    @GetMapping("/getUserInfo")
    public Result getUserInfo(){
        try {
            return Result.create(200, "获取用户信息成功", userService.getUserInfo());
        } catch (RuntimeException e) {
            return Result.create(200, "获取用户信息失败",e.getMessage());
        }
    }

    @GetMapping("/getOtherInfo/{userName}")
    public Result getOtherInfo(@PathVariable String userName){
        try {
            return Result.create(200, "获取用户信息成功", userService.getOtherInfo(userName));
        } catch (RuntimeException e) {
            return Result.create(200, "获取用户信息失败",e.getMessage());
        }
    }

    @PostMapping("/edituser")
    public Result updateUser(String gender,String birth,String job,String summary){
        try {
            userService.updateUserInfo(gender,birth,job,summary);
            return Result.create(200, "更新成功");
        } catch (RuntimeException e) {
            return Result.create(200, "更新失败，" + e.getMessage());
        }
    }

    @PostMapping("/updatePassword")
    public Result updatePassword(String oldPassword, String newPassword) {
        try {
            userService.updateUserPassword(oldPassword, newPassword);
            return Result.create(200, "修改密码成功");
        } catch (RuntimeException e) {
            return Result.create(200, e.getMessage());
        }
    }
    @PostMapping("/updateMail")
    public Result updateMail(String newMail, String newMailCode) {
        if (!formatUtil.checkStringNull(newMail,newMailCode)) {
            return Result.create(200, "参数错误");
        }
        //检查邮箱格式
        if (!formatUtil.checkMail(newMail)) {
            return Result.create(200, "参数错误");
        }
        try {
            userService.updateUserMail(newMail, newMailCode);
            return Result.create(200, "改绑成功");
        } catch (RuntimeException e) {
            return Result.create(200, e.getMessage());
        }
    }
}
