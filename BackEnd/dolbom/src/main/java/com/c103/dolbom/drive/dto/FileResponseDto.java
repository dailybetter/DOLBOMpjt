package com.c103.dolbom.drive.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileResponseDto {
    private Long fileId;
    private String fileName;
}
