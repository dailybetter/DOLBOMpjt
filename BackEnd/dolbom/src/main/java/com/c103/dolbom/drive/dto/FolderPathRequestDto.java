package com.c103.dolbom.drive.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)//스네이크 케이스를 변환
public class FolderPathRequestDto {
    private Long memberClientId;
    private String path;
}
