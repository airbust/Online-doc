package com.example.filter;

import com.example.config.JwtConfig;
import com.example.controller.ErrorController;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtConfig jwtConfig;

    //用户请求任何资源都会运行这个doFilterInternal.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        //...只进行权限校验
        checkPermission(request, response, chain);
    }

    /**
     * 校验权限
     *
     * @param request
     * @param response
     */
    private void checkPermission(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean giveFlag = false;
        //从request中取出token ，例“Bearer TOKEN字符串”
        String authHeader = request.getHeader(jwtConfig.getHeader());

        if (authHeader != null && authHeader.startsWith(jwtConfig.getPrefix())) {
            UserDetails userDetails = userService.loadUserByToken(authHeader);

            if (null != userDetails) {
                //此请求是否校验过
                if (SecurityContextHolder.getContext().getAuthentication() == null) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                giveFlag = true; //token不一致
            }
        } else {
            //token空，校验失败
            giveFlag = true;
        }

        if (giveFlag) {

            //token因某原因校验失败,给定游客身份->[游客]角色未写入数据库角色表
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("NORMAL"));
            //假定身份 赋予权限
            User user = new User("NORMAL", "NORMAL", authorities);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

}

