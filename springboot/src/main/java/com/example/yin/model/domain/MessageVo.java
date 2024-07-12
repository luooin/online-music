package com.example.yin.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageVo {
    private String from;
    //json时候格式化为时间格式
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;
    private String message;

    private int onlineCount; // 在线用户数量
    private List<String> onlineUsers; // 在线用户名列表
}

