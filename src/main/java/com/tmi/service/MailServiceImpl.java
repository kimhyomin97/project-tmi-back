package com.tmi.service;


import com.tmi.dto.MailDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class MailServiceImpl implements MailService{

    private JavaMailSender javaMailSender;

    public void sendMail(MailDto mailDto){
        log.info("sendMail memSq : {}", mailDto.getMemSq());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getContent());

        javaMailSender.send(message);
    }
}
