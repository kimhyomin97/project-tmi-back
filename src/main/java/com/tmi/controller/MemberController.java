package com.tmi.controller;


import com.tmi.dto.Member;
import com.tmi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody Member member){
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Member member){
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        // responseEntity, status code 검색 필요
        // back 코드 이관 필요
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

//    @PostMapping("/logout")

    @PostMapping("/deleteid")
    public ResponseEntity<Map<String, Object>> deleteid(@RequestBody Member member){
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }
}
