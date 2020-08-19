package com.example.service;

import com.example.config.JwtConfig;
import com.example.dao.*;
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
	private RecentDao recentDao;
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
		File file = fileDao.getFileById(fileId); //文档
		Role role = roleDao.getAuthByFileId(fileId); //文档权限
		System.out.println("role = " + role);
		String userName = jwtTokenUtil.getUsernameFromRequest(request);
		User user = userDao.getUserByName(userName);
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
		if(recentDao.findRecent(user.getId(), fileId)==null)
			recentDao.saveRecent(new Recent(user.getId(), fileId, new Date()));
		return new DocResult(file,role,map);//文档、文档权限、角色Token
	}

	// ******************************************************************************

	public void newFile(String fileName, String fileBody,String teamName) throws RuntimeException {
		if (fileName.equals(""))
			throw new RuntimeException("标题为空");
		User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
		Integer newFileId = fileDao.getLastFileId()+1;
		if(teamName != null) {
			Group group = groupDao.getGroupByName(teamName);
			File file = new File(newFileId,fileName, fileBody, new Date(), 0, group.getGroupId());
			fileDao.saveFile(file);
			Role role = new Role(file.getFileId());
			if(roleDao.getAuthByFileId(file.getFileId())==null)
				roleDao.saveAuthByFileId(role);
		}
		else {
			File file = new File(newFileId,fileName, fileBody, new Date(), user.getId(), 0);
			fileDao.saveFile(file);
			Role role = new Role(file.getFileId());
			if(roleDao.getAuthByFileId(file.getFileId())==null)
			//		System.out.println("role = " + role);
				roleDao.saveAuthByFileId(role);
			//		System.out.println("saveEnd");
		}
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
		fileDao.recoverFile(fileId);

	}
	public void foreverDeleted(Integer fileId) throws RuntimeException {
		File file = fileDao.getFileById(fileId);
		if(file == null)
			throw new RuntimeException("文件未删除");
		fileDao.foreverDeleted(fileId);
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
	public List<File> getTeamFile(String teamName) {
		Group group = groupDao.getGroupByName(teamName);
		return fileDao.getFileByGroupId(group.getGroupId());
	}
	public List<File> getHistoryFile(Integer fileId) {
		List<File> fileList = fileDao.getHistoryFile(fileId);
		System.out.println("fileList = " + fileList);
		return fileList;
	}
	public File getHistoryFileById(Integer fileId, Integer modifyCnt) {
		File file = fileDao.getHistoryFileById(fileId,modifyCnt);
		System.out.println("file = " + file);
		return file;
	}
	public List<File> getRelativeFile() {
		User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
		List<File> fileList = fileDao.getFileByUserId(user.getId());
		List<File> tmp = fileDao.getGroupFileByUserId(user.getId());
		List<File> tmp2 = fileDao.getGroupFileByAdminId(user.getId());
		fileList.addAll(tmp);
		fileList.addAll(tmp2);
		System.out.println("fileList = " + fileList);
		return fileList;
	}
	public List<File> getRecentFile() {
		User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
		return fileDao.getRecentFileByUserId(user.getId());
	}
	//////////////////////////////////////////////////////////////////////////////////////

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
		String roleName = "OTHER";
		if (file.getUserId() != 0) { // 个人文档
			if (file.getUserId().equals(userId))
				roleName = "USER";
		} else { // 团队文档
			Group group = groupDao.getGroupById(file.getGroupId());
			if (group.getAdminId().equals(userId))
				roleName = "USER";
			else {
				List<User> members = userDao.getMemberById(file.getGroupId());
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


	public void updateAuth(Integer fileId, String role, String auth) {
		if(role.equals("other")){
			if(auth.equals("RD")) roleDao.updateOtherAuth(fileId,1,1);
			else if(auth.equals("R")) roleDao.updateOtherAuth(fileId,1,0);
			else if(auth.equals("N")) roleDao.updateOtherAuth(fileId,0,0);
		}else if(role.equals("group")){
			if(auth.equals("RDW")) roleDao.updateGroupAuth(fileId,1,1);
			else if(auth.equals("RD")) roleDao.updateGroupAuth(fileId,1,0);
			else if(auth.equals("R")) roleDao.updateGroupAuth(fileId,0,0);
		}
	}



}
