package com.example.service;

import com.example.config.JwtConfig;
import com.example.dao.FileDao;
import com.example.dao.GroupDao;
import com.example.dao.UserDao;
import com.example.entity.File;
import com.example.entity.Group;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class FileService {
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

	public File findFileById(Integer fileId) throws RuntimeException {
		// TODO 权限
		File file = fileDao.getFileById(fileId);
		if(file == null)
			throw new RuntimeException("文件不存在");
		return file;
	}

	// ******************************************************************************

	public void newFile(String fileName, String fileBody) throws RuntimeException {
		if (fileName.equals(""))
			throw new RuntimeException("标题为空");
		User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
		File file = new File(fileName, fileBody, new Date(), user.getId(), 0);
		fileDao.saveFile(file);
	}

	public void editFile(Integer fileId,String fileName,String fileBody) throws RuntimeException {
		
		File file = fileDao.getFileById((fileId));
		if(file == null) 
			throw new RuntimeException("文件不存在");
		file.setFileName(fileName);
		file.setFileBody(fileBody);
		file.setModifyTime(new Date());
		file.setModifyCnt(file.getModifyCnt() + 1);
		fileDao.saveFile(file);
	}

	public void deleteFile(Integer fileId) throws RuntimeException {
		File file = fileDao.getFileById(fileId);
		if(file == null)
			throw new RuntimeException("文件不存在");
		fileDao.deleteFile(fileId);
	}
	
	public void recoverFile(Integer fileId) throws RuntimeException {
		File file = fileDao.getFileById(fileId);
		if(file == null)
			throw new RuntimeException("文件未删除");
		if(fileId > File.fileCnt || fileId <= 0)
			throw new RuntimeException("文件不存在");
		fileDao.recoverFile(fileId);
	}
	
	public void updateFileState(Integer fileId) throws RuntimeException {
		File file = fileDao.getFileById(fileId);
		if(file == null)
			throw new RuntimeException("文件不存在");
		fileDao.updateEditState(fileId);
	}
	
	public boolean isEditable(Integer fileId) throws RuntimeException{
		File file = fileDao.getFileById(fileId);
		if(file == null)
			throw new RuntimeException("文件不存在");
		return file.getIsEdit() == 0;
	}
	
	public List<File> getDeletedFile() {
		User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
		return fileDao.getDeletedFileByUserId(user.getId());
	}
	
	public List<File> getCreationFile() {
		User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
		return fileDao.getFileByUserId(user.getId());
	}
	
	/**
	 * 通过用户名加载用户到 Spring Security
	 * 
	 * @param fileId 文档id
	 * @return 用户信息
	 */
	public UserDetails loadUserByUsername(String fileId) throws UsernameNotFoundException {
		User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
		Integer userId = user.getId();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>(1);
		// 用于添加用户的权限。将用户权限添加到authorities
		File file = fileDao.getFileById(Integer.valueOf(fileId));
		Role role = new Role();
		if (file.getUserId() != 0) { // 个人文档
			if (file.getUserId().equals(userId))
				role.setName("USER");
			else
				role.setName("OTHER");
		} else { // 团队文档
			Group group = groupDao.getGroupById(file.getGroupId());
			if (group.getAdminId().equals(userId))
				role.setName("USER");
			else {
				List<User> members = groupDao.getMemberById(file.getGroupId());
				boolean flag = true;
				for (User member : members) {
					if (member.getId().equals(userId)) {
						role.setName("GROUP");
						flag = false;
					}
				}
				if (flag)
					role.setName("OTHER");
			}
		}
		authorities.add(new SimpleGrantedAuthority(role.getName()));
		return new org.springframework.security.core.userdetails.User(user.getName(), "***********", authorities);
	}
	
	

}
