package com.example.service;

import com.example.config.JwtConfig;
import com.example.dao.UserDao;
import com.example.entity.User;
import com.example.utils.JwtTokenUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public void saveAvatar(Integer userId, String avatar){
        User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        userDao.updateAvatar(userId,avatar);
    }

    /**
     * 登录
     * @param name 用户名
     * @param pwd 密码
     * @return token
     */
    public Map<String, Object> login(String name, String pwd) throws RuntimeException {
        //登录验证
        User user = userDao.getUserByName(name);
        if (user == null) throw new RuntimeException("用户名错误");
        if (!user.getPassword().equals(pwd)) throw new RuntimeException("密码错误");

        //签发token
        final UserDetails userDetails = this.loadUserByUsername(user.getName());
        final String token = jwtTokenUtil.generateToken(userDetails);
        //获取用户权限
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            roles.add(authority.getAuthority());
        }
        Map<String, Object> map = new HashMap<>(3);
        map.put("token", jwtConfig.getPrefix() + token);
        map.put("name", user.getName());
        map.put("roles", roles);

        //设置过期时间 (key value time s)
        redisTemplate.opsForValue().
                set("TOKEN_" + name, jwtConfig.getPrefix() + token, 60, TimeUnit.SECONDS);
        return map;
    }

    /**
     * 注册
     * @param name 用户名
     * @param password 密码
     * @param mail 邮箱
     * @param code 验证码
     */
    public void register(String name, String password, String mail, String code) {
        if (userDao.getUserByName(name) != null) throw new RuntimeException("用户名已存在");
        if (!checkMailCode(mail, code)) throw new RuntimeException("验证码错误");

        User newUser = new User();
        newUser.setName(name);
        newUser.setPassword(password);
        newUser.setMail(mail);
        userDao.saveUser(newUser);
    }

    /**
     * 退出登录
     * 删除redis中的key
     */
    public void logout() {
        String username = jwtTokenUtil.getUsernameFromRequest(request);
        redisTemplate.delete("TOKEN_" + username);
    }

    //*****************************************************************************

    /**
     * 判断验证码
     * @param mail 邮箱
     * @param code 验证码
     * @return 验证码正误
     */
    public boolean checkMailCode(String mail, String code) {
        String mailCode = redisTemplate.opsForValue().get("MAIL_" + mail);
        return code.equals(mailCode);
    }

    /**
     * 发送邮件验证码
     * @param mail 邮箱
     */
    public void sendMail(String mail) {
        Random _random = new Random();
        int random = _random.nextInt(899999) + 100001;
        Map<String, String> map = new HashMap<>();
        String code = Integer.toString(random);
        map.put("mail", mail);
        map.put("code", code);
        //保存发送记录
        redisTemplate.opsForValue()
                .set("MAIL_" + mail, code, 1, TimeUnit.MINUTES);
        rabbitTemplate.convertAndSend("MAIL", map);
    }

    public void updateUserPassword(String oldPassword, String newPassword) {
        //校验原密码
        String name = jwtTokenUtil.getUsernameFromRequest(request);
        User user = new User();
        user.setName(name);
        user = userDao.getUserByName(user.getName());
//        if (!encoder.matches(oldPassword, user.getPassword()))
        if(!oldPassword.equals(user.getPassword()))
            throw new RuntimeException("密码错误");
//        user.setPassword(encoder.encode(newPassword));
        user.setPassword(newPassword);
        userDao.updateUser(user);

    }
    public void updateUserMail(String newMail, String newMailCode) {
        //获取向旧邮箱发出的验证码
        String userName = jwtTokenUtil.getUsernameFromRequest(request);
        User user = userDao.getUserByName(userName);
        //校验新邮箱验证码
        if (!checkMailCode(newMail,newMailCode))
            throw new RuntimeException("新邮箱无效验证码");
        user.setMail(newMail);
        userDao.updateUser(user);
    }

    /**
     * 通过用户名加载用户到 Spring Security
     * @param name 用户名
     * @return 用户信息
     */
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userDao.getUserByName(name);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(1);
        //登录时貌似添加权限无意义？token仅存用户名就行...
        authorities.add(new SimpleGrantedAuthority("OTHER"));
        return new org.springframework.security.core.userdetails.User(user.getName(), "***********", authorities);
    }

    public User getUserInfo(){
        return userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
    }
    public User getOtherInfo(String userName) {
        return userDao.getUserByName(userName);
    }
    /**
     * 更新用户信息
     */
    public void updateUserInfo(String gender, String birth, String job, String summary) {
        User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        if(gender!=null) user.setGender(gender);
        if(birth!=null) user.setBirth(birth);
        if(job!=null) user.setJob(job);
        if(summary!=null) user.setInfo(summary);
        System.out.println("user.toString() = " + user.toString());
        userDao.updateUser(user);
    }

    /**
     * 从token中提取用户信息
     * @param authHeader Bearer
     * @return 用户信息
     */
    public UserDetails loadUserByToken(String authHeader) {
        //除去前缀，获取token字符串
        final String authToken = authHeader.substring(jwtConfig.getPrefix().length());
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        //token非法
        if (null == username) return null;
        String redisToken = redisTemplate.opsForValue().get("TOKEN_" + username);
        //从redis中取不到值或不匹配
        if (!authHeader.equals(redisToken)) return null;

        User user = new User();
        user.setName(username);

        List<String> roles = jwtTokenUtil.getRolesFromToken(authToken);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return new org.springframework.security.core.userdetails.User(user.getName(), "***********", authorities);
    }


}
