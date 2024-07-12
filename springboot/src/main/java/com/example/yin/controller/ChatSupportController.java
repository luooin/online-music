package com.example.yin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ChatSupportController {
    @GetMapping("/api/data")
    public List<String> getData() {
        List<String> dataList = new ArrayList<>();
        dataList.add("name");
        dataList.add("Value 2");
        dataList.add("Value 3");
        return dataList;
    }
}
