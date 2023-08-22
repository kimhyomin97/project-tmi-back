package com.tmi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response<Type> {
    private String result;
    private String message;
    private Type data;
}
