package com.example.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.example.entity.DiscussResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
	private HttpServletRequest request;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	public void newDiscuss(Integer fileId, String discussBody) throws RuntimeException {
		if(discussBody == null || discussBody.equals(""))
			throw new RuntimeException("评论内容为空");
		User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
		Discuss dis = new Discuss(user.getId(), fileId, discussBody, new Date(), 0);
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

	public List<DiscussResult> getDiscussByFileId(Integer fileId) {
		List<Discuss> discussList = discussDao.getDiscussByFileId(fileId);
		System.out.println("discussList = " + discussList);
		List<DiscussResult> discussResultList = new ArrayList<>();
		for(Discuss discuss : discussList){
			User user = userDao.getUserById(discuss.getUserId());
			discussResultList.add(new DiscussResult(discuss,user,null));
		}
		System.out.println("discussResultList = " + discussResultList);
		return discussResultList;
	}

	public List<DiscussResult> getAllDiscuss() {
		Integer userId = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request)).getId();
		List<Discuss> discussList= discussDao.getUserFileDiscussByUserId(userId);
//		System.out.println("discussList = " + discussList);
		List<Discuss> discussList1 = discussDao.getTeamFileDiscussByUserId(userId);
//		System.out.println("discussList1 = " + discussList1);

		List<DiscussResult> discussResultList = new ArrayList<>();
		for(Discuss discuss : discussList){
			User user = userDao.getUserById(discuss.getUserId());
			String fileName = fileDao.getFileById(discuss.getFileId()).getFileName();
			discussResultList.add(new DiscussResult(discuss,user,fileName));
		}
		for(Discuss discuss : discussList1){
			User user = userDao.getUserById(discuss.getUserId());
			String fileName = fileDao.getFileById(discuss.getFileId()).getFileName();
			discussResultList.add(new DiscussResult(discuss,user,fileName));
		}
		return discussResultList;
	}

	public void readDiscussById(Integer discussId) {
		System.out.println("Read discussId = " + discussId);
		discussDao.readDiscussById(discussId);
	}
}
