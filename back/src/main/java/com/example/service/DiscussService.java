package com.example.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.example.dao.GroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.config.JwtConfig;
import com.example.dao.DiscussDao;
import com.example.dao.FileDao;
import com.example.dao.UserDao;
import com.example.entity.Discuss;
import com.example.entity.User;
import com.example.utils.JwtTokenUtil;

@Service
public class DiscussService {

	@Autowired
	DiscussDao discussDao;
	@Autowired
	private UserDao userDao;
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

	public void newDiscuss(Integer fileId, String discussBody) throws RuntimeException {
		if(discussBody == null || discussBody =="" )
			throw new RuntimeException("评论内容为空");
		User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
		Discuss dis = new Discuss(user.getId(), fileId, discussBody, new Date());
		discussDao.saveDiscuss(dis);
	}

	public void deleteDiscussById(Integer discussId) {
		discussDao.deleteDiscussById(discussId);
	}

	public Discuss getDiscussById(Integer discussId) throws RuntimeException {
		Discuss dis = discussDao.getDiscussById(discussId);
		if(dis == null)
			throw new RuntimeException("评论不存在");
		return dis;
	}

	public List<Discuss> getDiscussByFileId(Integer fileId) {
		return discussDao.getDiscussByFileId(fileId);
	}

}
