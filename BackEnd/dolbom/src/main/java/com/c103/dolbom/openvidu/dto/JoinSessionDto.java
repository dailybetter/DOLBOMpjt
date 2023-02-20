package com.c103.dolbom.openvidu.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class JoinSessionDto {

    private String name;
    private String email;
    private String sessionId;
    private Long conferenceId;

}
