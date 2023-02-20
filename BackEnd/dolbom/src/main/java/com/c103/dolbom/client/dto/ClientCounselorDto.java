package com.c103.dolbom.client.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)//스네이크 케이스를 변환
public class ClientCounselorDto {
    Long clientId;
    Long counselorId;
}
