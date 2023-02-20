package com.c103.dolbom.openvidu.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateSessionResDto {

    private Long conferenceId;
    private String sessionId;

}
