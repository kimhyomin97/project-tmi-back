package com.tmi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
public class MailDto {
    private Long idx;

    private String memSq;

    private String address;

    private String title;

    private String content;
}
