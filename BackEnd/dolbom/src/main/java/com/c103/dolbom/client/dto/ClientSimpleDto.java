package com.c103.dolbom.client.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClientSimpleDto {
    private Long id;
    private String name;
    private String phone;
    private String email;

}
