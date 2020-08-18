package com.example.entity;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class Recent {
	private Integer recentId;
	private Integer userId;
	private Integer fileId;
	private Date recentTime;
	
	public Recent(Integer userId, Integer fileId, Date recentTime) {
		this.userId = userId;
		this.fileId = fileId;
		this.recentTime = recentTime;
	}
	
}
