package com.example.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DiscussResult {
    private Discuss discuss;
    private User user;
    private String fileName;

    public DiscussResult(Discuss discuss, User user, String fileName) {
        this.discuss = discuss;
        this.user = user;
        this.fileName = fileName;
    }
}
