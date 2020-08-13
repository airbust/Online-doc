package com.example.entity;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class Discuss {
	private Integer discussId;
	private Integer userId;
	private Integer fileId;
	private String discussBody;
	private Date discussTime;
	
	public Discuss(Integer userId, Integer fileId, String discussBody, Date discussTime) {
		this.userId = userId;
		this.fileId = fileId;
		this.discussBody = discussBody;
		this.discussTime = discussTime;
	}
	
}
