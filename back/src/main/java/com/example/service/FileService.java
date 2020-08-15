package com.example.service;

import com.example.config.JwtConfig;
import com.example.dao.FileDao;
import com.example.dao.GroupDao;
import com.example.dao.RoleDao;
import com.example.dao.UserDao;
import com.example.entity.*;
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
	private RoleDao roleDao;
	@Autowired
	private JwtConfig jwtConfig;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	public DocResult findFileById(Integer fileId) throws RuntimeException {
		// TODO 确保登录
		System.out.println("查询文章id = " + fileId);
		File file = fileDao.getFileById(fileId); //文档
		Role role = roleDao.getAuthByFileId(fileId); //文档权限
		String userName = jwtTokenUtil.getUsernameFromRequest(request);
		final UserDetails userDetails = this.loadUserByUsername(Integer.toString(fileId));
		final String token = jwtTokenUtil.generateToken(userDetails);
		//获取用户权限
		Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
		List<String> roles = new ArrayList<>();
		for (GrantedAuthority authority : authorities) {
			roles.add(authority.getAuthority());
		}
		Map<String, Object> map = new HashMap<>(3);//token(角色)
		map.put("token", jwtConfig.getPrefix() + token);
		map.put("name", userName);
		map.put("roles", roles);
		//设置过期时间 (key value time s)
		redisTemplate.opsForValue().
				set("TOKEN_" + userName, jwtConfig.getPrefix() + token, 60, TimeUnit.SECONDS);
		if(file == null)
			throw new RuntimeException("文件不存在");
		if(role == null)
			throw new RuntimeException("文件权限损坏");
		return new DocResult(file,role,map);//文档、文档权限、角色Token
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
//		fileCnt bug 暂时注释
//		if(fileId > File.fileCnt || fileId <= 0)
//			throw new RuntimeException("文件不存在");
		fileDao.recoverFile(fileId);
	}

	public void updateFileState(Integer fileId) throws RuntimeException {
		File file = fileDao.getFileById(fileId);
		if(file == null)
			throw new RuntimeException("文件不存在");
		fileDao.updateEditState(fileId);
	}

	public void changeEditable(boolean lock, Integer fileId) throws RuntimeException{
		if(lock) fileDao.setEditStatus(1,fileId);
		else fileDao.setEditStatus(0,fileId);
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
//		Role role = new Role();
		String roleName = "OTHER";
		if (file.getUserId() != 0) { // 个人文档
			if (file.getUserId().equals(userId))
				roleName = "USER";
		} else { // 团队文档
			Group group = groupDao.getGroupById(file.getGroupId());
			if (group.getAdminId().equals(userId))
				roleName = "USER";
			else {
				List<User> members = groupDao.getMemberById(file.getGroupId());
				for (User member : members) {
					if (member.getId().equals(userId)) {
						roleName = "GROUP";
						break;
					}
				}
			}
		}
		authorities.add(new SimpleGrantedAuthority(roleName));
		return new org.springframework.security.core.userdetails.User(user.getName(), "***********", authorities);
	}



}
