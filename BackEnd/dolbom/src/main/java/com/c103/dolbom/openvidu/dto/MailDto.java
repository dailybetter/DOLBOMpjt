package com.c103.dolbom.openvidu.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MailDto {

    private String email;
    private String title;
    private String link;
    private Long conferenceId;
    private String sessionId;

}
