package com.example.dao;

import com.example.entity.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface NoticeDao {

    Notice getNoticeById(Integer noticeId);

    void saveNotice(Notice notice);

    List<Notice> getNotice(String name);

    void readNotice(Integer noticeId);
}
