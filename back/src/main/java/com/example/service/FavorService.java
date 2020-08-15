package com.example.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.config.JwtConfig;
import com.example.dao.DiscussDao;
import com.example.dao.FavorDao;
import com.example.dao.FileDao;
import com.example.dao.GroupDao;
import com.example.dao.UserDao;
import com.example.entity.Favor;
import com.example.entity.File;
import com.example.entity.User;
import com.example.utils.JwtTokenUtil;

@Service
public class FavorService {
	
	@Autowired
	FavorDao favorDao;
	@Autowired
	DiscussDao discussDao;
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
	
	public void favorFile(Integer fileId) throws RuntimeException {
		File file = fileDao.getFileById(fileId);
		if(file == null)
			throw new RuntimeException("文档不存在");
		User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
		favorDao.saveFavor(new Favor(user.getId(), fileId, new Date()));
	}
	
	public List<File> getCollectionFile(Integer page, Integer showCount) {
		User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
		List<Integer> tmp = favorDao.getFavorFileIdByUserId(user.getId(), (page - 1) * showCount, showCount);
		List<File> file = new ArrayList<>();
		for(Integer i : tmp)
			file.add(fileDao.getFileById(i));
		return file;
	}
	
	public void removeFavoredFile(Integer fileId) {
		User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
		favorDao.deleteFavor(user.getId(), fileId);
	}
}