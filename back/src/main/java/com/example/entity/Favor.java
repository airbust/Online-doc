package com.example.entity;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class Favor {
	private Integer favorId;
	private Integer userId;
	private Integer fileId;
	private Date favorTime;
	
	public Favor(Integer userId, Integer fileId, Date favorTime) {
		this.userId = userId;
		this.fileId = fileId;
		this.favorTime = favorTime;
	}
	
}
