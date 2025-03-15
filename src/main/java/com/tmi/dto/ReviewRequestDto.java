package com.tmi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {
    private Long restaurantId;
    private Long memberId;
    private Double rating;
    private String comment;
}
