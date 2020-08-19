package com.example.service;

import com.example.dao.GroupDao;
import com.example.dao.NoticeDao;
import com.example.dao.UserDao;
import com.example.entity.Group;
import com.example.entity.Notice;
import com.example.entity.User;
import com.example.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private NoticeDao noticeDao;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    public List<Notice> getUserNotice() {
        User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        //        System.out.println("noticeList = " + noticeList);
        return noticeDao.getNotice(user.getName());
    }

    public void readNotice(Integer noticeId) {
        noticeDao.readNotice(noticeId);

    }

    public void permit(Integer noticeId) {
        Notice notice = noticeDao.getNoticeById(noticeId);
        User user = userDao.getUserByName(notice.getUserName());
        Group group = groupDao.getGroupByName(notice.getGroupName());
        if(notice.getOp().equals(1)){ //回应邀请
            noticeDao.saveNotice(new Notice(notice.getUserName(),notice.getGroupAdmin(),
                    notice.getGroupName(),4,"已同意加入",new Date(),0));
            groupDao.saveGroupMem(group.getGroupId(),user.getId());
        }else if(notice.getOp().equals(2)){ //回应申请
            noticeDao.saveNotice(new Notice(notice.getUserName(),notice.getGroupAdmin(),
                    notice.getGroupName(),3,"已批准你加入",new Date(),0));
            groupDao.saveGroupMem(group.getGroupId(),user.getId());
        }
        noticeDao.readNotice(noticeId);
    }
}
