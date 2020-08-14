package com.example.utils;

import com.example.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -5625635588908941275L;

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_KEY_ROLES = "roles";

    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 生成token
     * @param userDetails 用户信息
     * @return token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(3);
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername()); //放入用户名
        claims.put(CLAIM_KEY_CREATED, new Date());//放入token生成时间
        List<String> roles = new ArrayList<>();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            roles.add(authority.getAuthority());
        }
        claims.put(CLAIM_KEY_ROLES, roles);//放入用户权限
        return Jwts.builder()
                .setClaims(claims) //声明
                .setExpiration(generateExpirationDate()) //过期时间
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret()) //签名算法
                .compact();
    }

    /**
     * 从request中获取用户名
     *
     * @param request
     * @return
     */
    public String getUsernameFromRequest(HttpServletRequest request) {
        String token = request.getHeader(jwtConfig.getHeader());
        token = token.substring(jwtConfig.getPrefix().length());
        return token == null ? null : getUsernameFromToken(token);
    }

    /**
     * 从token中获取用户名
     * @param token token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
//            System.out.println("claims = " + claims);//null debug
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 从token中获取用户角色
     * @param authToken token
     * @return 角色列表
     */
    public List<String> getRolesFromToken(String authToken) {
        List<String> roles;
        try {
            final Claims claims = getClaimsFromToken(authToken);
            roles = (List<String>)claims.get(CLAIM_KEY_ROLES);
        } catch (Exception e) {
            roles = null;
        }
        return roles;
    }

    /**
     * 生成过期时间 单位[ms]
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + jwtConfig.getTime() * 1000);
    }

}

