package com.tmi.controller;

import com.tmi.dto.MailDto;
import com.tmi.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mail")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/mail")
    public void sendMail(@RequestBody MailDto mailDto){
        log.info("mail send test");
        System.out.println("address : " + mailDto.getAddress());
        mailService.sendMail(mailDto);
    }

}
