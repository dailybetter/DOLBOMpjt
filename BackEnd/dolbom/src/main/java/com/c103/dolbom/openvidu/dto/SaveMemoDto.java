package com.c103.dolbom.openvidu.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class SaveMemoDto {

    private String originName;
    private String savedName;
    private String path;
    private LocalDateTime saveTime;

}
