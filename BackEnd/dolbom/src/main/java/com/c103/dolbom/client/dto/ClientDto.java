package com.c103.dolbom.client.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ClientDto{
    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDate birth;
    private String content;
}
