package com.example.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.example.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.config.JwtConfig;
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
		User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
		Favor favor = favorDao.getFavorByUserIdAndFileId(user.getId(),fileId);
		if(file == null)
			throw new RuntimeException("文档不存在");
		if(favor == null)
			favorDao.saveFavor(new Favor(user.getId(), fileId, new Date()));
	}

	public List<File> getCollectionFile() {
		User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
//		List<Integer> tmp = favorDao.getFavorFileIdByUserId(user.getId(), (page - 1) * showCount, showCount);
		List<Integer> tmp = favorDao.getFavorFileIdByUserId(user.getId());
		List<File> file = new ArrayList<>();
//		System.out.println("tmp = " + tmp);
		for(Integer i : tmp)
			file.add(fileDao.getFileById(i));
//		System.out.println("file = " + file);
		return file;
	}

	public void removeFavoredFile(Integer fileId) {
		User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
		favorDao.deleteFavor(user.getId(), fileId);
	}
}
