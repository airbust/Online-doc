package com.example.config;

import lombok.Data;
import org.springframework.stereotype.Component;

//@ConfigurationProperties(prefix = "jwt")
@Data
@Component
public class JwtConfig {

    private long time = 432000;     // 5天(以秒s计)过期时间
    private String secret = "Secret";// JWT密码
    private String prefix = "Bearer ";   // Token前缀
    private String header = "Authorization"; // 存放Token的Header Key

}
